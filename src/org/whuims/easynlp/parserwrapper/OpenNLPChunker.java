package org.whuims.easynlp.parserwrapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;

/**
 * OpenNLP Chunker的一个封装
 * 
 * @author Qikai
 *
 */
public class OpenNLPChunker {
	private static OpenNLPChunker instance = new OpenNLPChunker();
	static InputStream modelIn = null;
	static ChunkerModel model = null;
	private static ChunkerME chunker = null;

	private OpenNLPChunker() {

	}

	public static OpenNLPChunker createInstance() {
		return instance;
	}

	public String[] chunkParse(String[] tokens, String[] postags) {
		return chunker.chunk(tokens, postags);
	}

	static {
		try {
			modelIn = new FileInputStream("resource/model/en-chunker.bin");
			model = new ChunkerModel(modelIn);
			chunker = new ChunkerME(model);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		String line = "When searching for optimal paths in a network, algorithms like A*-search need an approximation of the minimal costs between the current node and a target node.";
		Sentence sent = PaperUtils.sentProductOf(line);
		String[] tokens = sent.getTokenArray();
		String[] postags = sent.getPostagArray();
		String[] chunks = OpenNLPChunker.createInstance().chunkParse(tokens,
				postags);
		for (int i = 0; i < tokens.length; i++) {
			System.out
					.println(tokens[i] + "\t" + postags[i] + "\t" + chunks[i]);
		}
	}
}
