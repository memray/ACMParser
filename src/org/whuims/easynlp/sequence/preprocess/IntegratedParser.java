package org.whuims.easynlp.sequence.preprocess;

/**
 * this is the main class to do all the preprocessing 
 * for defminer output include the following format:
 * *.word
 * *.pos
 * *.chunk -- shallow parsing tag
 * *.seq -- collapsed shallow parsing sequence
 * *.dep -- whole dependency for the sentence
 * *.parent  -- parent word of the current word
 * *.ptype -- parent dependency type of the current word 
 * *.path -- typed dependency path of the current word to the root
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.whuims.easynlp.parserwrapper.ShallowParser;

import opennlp.tools.util.InvalidFormatException;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

public class IntegratedParser {

	public static void main(String[] args) throws InvalidFormatException,
			IOException {

		String text = "";
		String folder_name = "";

		if (args.length == 1) {
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			StringBuilder sb = new StringBuilder();

			folder_name = (args[0].contains(".")) ? args[0].substring(0,
					args[0].indexOf(".")) : args[0];
			folder_name += "_defminer";

			System.out.println("Creating new folder " + folder_name);
			try {
				String input = Normalizer.normalize(br.readLine(),
						Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
				while (input != null) {
					input = Normalizer.normalize(input, Normalizer.Form.NFD)
							.replaceAll("[^\\p{ASCII}]", "");
					sb.append(input);
					sb.append(' ');
					input = br.readLine();
				}
			} finally {
				br.close();
				text = sb.toString();
			}
		} else {
			System.out
					.println("The program takes exactly one argument, which is the input file. Program exits with status -1.");
			System.exit(-1);
		}
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, regexner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// get shallow parser instance
		ShallowParser shallowParser = ShallowParser.createInstance();

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		ArrayList<String> words;
		ArrayList<String> poss;
		ArrayList<String> nes;
		ArrayList<String> dep_parents; // the parent of the current word based
										// on dependency parsing
		ArrayList<String> parent_types;
		ArrayList<String> dep_paths; // the dependency path of the current word
										// to the root

		// open all the output files
		// TODO: this works only for unix based file system
		System.out.println("Creating new folder " + folder_name);
		new File(folder_name).mkdirs();

		String new_file_name = folder_name.contains("/") ? folder_name
				.substring(folder_name.lastIndexOf("/") + 1) : folder_name;
		File word_file = new File(folder_name + "/" + new_file_name + ".word");
		FileWriter fstream_word = new FileWriter(word_file);
		BufferedWriter out_word = new BufferedWriter(fstream_word);

		File pos_file = new File(folder_name + "/" + new_file_name + ".pos");
		FileWriter fstream_pos = new FileWriter(pos_file);
		BufferedWriter out_pos = new BufferedWriter(fstream_pos);

		File ne_file = new File(folder_name + "/" + new_file_name + ".ne");
		FileWriter fstream_ne = new FileWriter(ne_file);
		BufferedWriter out_ne = new BufferedWriter(fstream_ne);

		File chunk_file = new File(folder_name + "/" + new_file_name + ".chunk");
		FileWriter fstream_chunk = new FileWriter(chunk_file);
		BufferedWriter out_chunk = new BufferedWriter(fstream_chunk);

		File seq_file = new File(folder_name + "/" + new_file_name + ".seq");
		FileWriter fstream_seq = new FileWriter(seq_file);
		BufferedWriter out_seq = new BufferedWriter(fstream_seq);

		File dep_file = new File(folder_name + "/" + new_file_name + ".dep");
		FileWriter fstream_dep = new FileWriter(dep_file);
		BufferedWriter out_dep = new BufferedWriter(fstream_dep);

		File parent_file = new File(folder_name + "/" + new_file_name
				+ ".parent");
		FileWriter fstream_parent = new FileWriter(parent_file);
		BufferedWriter out_parent = new BufferedWriter(fstream_parent);

		// the postag of the parent
		File ptype_file = new File(folder_name + "/" + new_file_name
				+ ".ptype");
		FileWriter fstream_ptype = new FileWriter(ptype_file);
		BufferedWriter out_ptype = new BufferedWriter(fstream_ptype);

		// the dependency path
		File path_file = new File(folder_name + "/" + new_file_name + ".path");

		FileWriter fstream_path = new FileWriter(path_file);
		BufferedWriter out_path = new BufferedWriter(fstream_path);

		for (int i = 0; i < sentences.size(); i++) {
			CoreMap sentence = sentences.get(i);
			words = new ArrayList<String>();
			poss = new ArrayList<String>();
			nes = new ArrayList<String>();
			dep_parents = new ArrayList<String>();
			parent_types = new ArrayList<String>();
			dep_paths = new ArrayList<String>();

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence
					.get(CollapsedCCProcessedDependenciesAnnotation.class);

			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (int j = 0; j < sentence.get(TokensAnnotation.class).size(); j++) {
				CoreLabel token = sentence.get(TokensAnnotation.class).get(j);
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				words.add(word);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				poss.add(pos);
				// this is the NE tag of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
				nes.add(ne);
				String ptype ="";
				
				try {
					IndexedWord iw = dependencies.getNodeByIndex(j + 1);
					IndexedWord parent = dependencies.getParent(iw);
					
					// dependency path features
					List<SemanticGraphEdge> edges = dependencies
							.getIncomingEdgesSorted(iw);
					IndexedWord current_word;
					List<String> dependency_path = new ArrayList<String>();
					List<Integer> seen_words = new ArrayList<Integer>();
					dependency_path.add("TARGET");
					
					if (edges.size() > 0) {
						ptype = edges.get(0).getRelation().toString();
					}
					// iteratively traverse upward to get the dependency types	
					while (edges.size() > 0) { 											
						current_word = edges.get(0).getGovernor();
						if (seen_words.contains(current_word.index())) 
							//entered infinite loop
							break;
						seen_words.add(new Integer(current_word.index()));
						dependency_path.add(edges.get(0).getRelation()
								.toString());
						edges = dependencies
								.getIncomingEdgesSorted(current_word);
					}

					String parent_word = (parent == null) ? "rOOT" : parent
							.word();
					if(ptype=="")
						ptype = "rOOT";
					dep_parents.add(parent_word);
					parent_types.add(ptype);
					String dep_path = StringUtils.join(dependency_path, "-");
					dep_paths.add(dep_path);

				} catch (Exception e) {
					// System.out.println(word + " is not in the graph!");
					dep_parents.add("aBSENT");
					parent_types.add("aBSENT");
					dep_paths.add("aBSENT");

				}
			}

			// write to word & pos file
			out_word.write(StringUtils.join(words, " "));
			out_word.write("\n");
			out_pos.write(StringUtils.join(poss, " "));
			out_pos.write("\n");
			out_ne.write(StringUtils.join(nes, " "));
			out_ne.write("\n");
			out_parent.write(StringUtils.join(dep_parents, " "));
			out_parent.write("\n");
			out_ptype.write(StringUtils.join(parent_types, " "));
			out_ptype.write("\n");
			out_path.write(StringUtils.join(dep_paths, " "));
			out_path.write("\n");

			String[] word_arr = new String[words.size()];
			word_arr = words.toArray(word_arr);
			String[] pos_arr = new String[poss.size()];
			pos_arr = poss.toArray(pos_arr);

			// perform chunking
			String[] shallow_tag_arr = shallowParser.chunk(word_arr, pos_arr);
			out_chunk.write(StringUtils.join(shallow_tag_arr, " "));
			out_chunk.write("\n");

			// get collapsed chunking sequence
			String sequence = shallowParser.chunk_sequence(word_arr,
					shallow_tag_arr);
			out_seq.write(sequence);
			out_seq.write("\n");

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);

			Collection<TypedDependency> typedDependencies = dependencies
					.typedDependencies();
			Iterator<TypedDependency> iterator = typedDependencies.iterator();
			TypedDependency dep;
			while (iterator.hasNext()) {
				dep = iterator.next();
				out_dep.write(dep.toString());
				out_dep.write("\n");
			}
			out_dep.write("\n");
		} //end of for each sentence

		// close all the writer and streams
		out_word.close();
		fstream_word.close();
		out_pos.close();
		fstream_pos.close();
		out_chunk.close();
		fstream_chunk.close();
		out_seq.close();
		fstream_seq.close();
		out_dep.close();
		fstream_dep.close();
		out_ne.close();
		fstream_ne.close();
		out_parent.close();
		fstream_parent.close();
		out_ptype.close();
		fstream_ptype.close();
		out_path.close();
		fstream_path.close();
		
	}
}
