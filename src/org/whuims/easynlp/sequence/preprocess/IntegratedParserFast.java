package org.whuims.easynlp.sequence.preprocess;

/**
 * this script skipped using dependency features to speed up processing
 * but the accuracy will decrease. It is suitable to process large corpus
 * where accuracy is not a big concern. Use IntegratedParser class if otherwise
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.whuims.easynlp.parserwrapper.ShallowParser;

import opennlp.tools.util.InvalidFormatException;

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
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

public class IntegratedParserFast {
	
	public static void main(String[] args) throws InvalidFormatException, IOException{

		String text = "";
		String folder_name = "";
		
		if(args.length == 1){
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			StringBuilder sb = new StringBuilder();
			
			folder_name = (args[0].contains(".")) ? 
					args[0].substring(0,args[0].indexOf(".")) : args[0];
			folder_name += "_defminer";
			
			System.out.println(folder_name);
		    try {
		        String line = br.readLine();
		        while (line != null) {
		            sb.append(line);
		            sb.append(' ');
		            line = br.readLine();
		        }
		    } finally {
		        br.close();
		        text = sb.toString();
		    }
		}else{
			System.out.println("The program takes exactly one argument, which is the input file. Program exits with status -1.");
			System.exit(-1);
		}
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    	    
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    //get shallow parser instance 
	    ShallowParser shallowParser = ShallowParser.createInstance();
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    ArrayList<String> words;
	    ArrayList<String> poss;
	    ArrayList<String> ners;

	    //open all the output files
	    //TODO: this works only for unix based file system
	    System.out.println("Creating new folder " + folder_name);
	    new File(folder_name).mkdirs();
	    
	    String new_file_name = folder_name.contains("/") 
	    		? folder_name.substring(folder_name.lastIndexOf("/")+1) : folder_name;
        File word_file = new File(folder_name + "/" + new_file_name + ".word");
        FileWriter fstream_word = new FileWriter(word_file);
        BufferedWriter out_word = new BufferedWriter(fstream_word);

        File pos_file = new File(folder_name + "/" + new_file_name + ".pos");
        FileWriter fstream_pos = new FileWriter(pos_file);
        BufferedWriter out_pos = new BufferedWriter(fstream_pos);
        
        File chunk_file = new File(folder_name + "/" + new_file_name + ".chunk");
        FileWriter fstream_chunk = new FileWriter(chunk_file);
        BufferedWriter out_chunk = new BufferedWriter(fstream_chunk);
        
        File seq_file = new File(folder_name + "/" + new_file_name + ".seq");
        FileWriter fstream_seq = new FileWriter(seq_file);
        BufferedWriter out_seq = new BufferedWriter(fstream_seq);
          
	    for(int i=0;i<sentences.size(); i++) {
	      CoreMap sentence = sentences.get(i);
	      words = new ArrayList<String>();
	      poss = new ArrayList<String>();
	      
	      // this is the Stanford dependency graph of the current sentence
		  SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		    
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (int j=0; j<sentence.get(TokensAnnotation.class).size();j++) {
	    	CoreLabel token = sentence.get(TokensAnnotation.class).get(j);  
	        // this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        words.add(word);
	        // this is the POS tag of the token
	        String pos = token.get(PartOfSpeechAnnotation.class);
	        poss.add(pos);
	        IndexedWord vertex = new IndexedWord(word, i, j);
	    	//dep_parents.add(dependencies.);
	      }
	      
	      //write to word & pos file
	      out_word.write(StringUtils.join(words, " "));
	      out_word.write("\n");
	      out_pos.write(StringUtils.join(poss, " "));
	      out_pos.write("\n");
	      
	      String[] word_arr = new String[words.size()];
	      word_arr = words.toArray(word_arr);
	      String[] pos_arr = new String[poss.size()];
	      pos_arr = poss.toArray(pos_arr);
	      
	      //perform chunking
	      String [] shallow_tag_arr = shallowParser.chunk(word_arr, pos_arr);
	      out_chunk.write(StringUtils.join(shallow_tag_arr, " "));
	      out_chunk.write("\n");
	      
	      //get collapsed chunking sequence
	      String sequence = shallowParser.chunk_sequence(word_arr, shallow_tag_arr);
	      out_seq.write(sequence);
	      out_seq.write("\n");

	    }
	    
	    //close all the writer and streams
	    out_word.close();
	    fstream_word.close();
	    out_pos.close();
	    fstream_pos.close();
	    out_chunk.close();
	    fstream_chunk.close();
	    out_seq.close();
	    fstream_seq.close();    
	}
}
