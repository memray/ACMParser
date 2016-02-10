package org.whuims.easynlp.exverb;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExVerbLineExtractorTest {

	@Test
	public void testExtract() {
		String line1 = "I don’t have anything to say to you.";
		String line2 = "Discriminative learning methods , such as Maximum Entropy Markov Models ( McCallum et al. , 2000 ) , Projection Based Markov Models ( Punyakanok and Roth , 2000 ) , Conditional Random Fields ( Lafferty et al. , 2001 ) , Sequence AdaBoost ( Altun et al. , 2003a ) , Sequence Perceptron ( Collins , 2002 ) , Hidden Markov Support Vector Machines ( Altun et al. , 2003b ) and Maximum-Margin Markov Networks ( Taskar et al. , 2004 ) , overcome the limitations of HMMs .";
		String line3 = "Electronically available multi-modal data is unprecedented in terms of its volume, variety, velocity, (and veracity).";
		String line4 = "Mining scientific data for patterns and relationships has been a common practice for decades , and the very simple use of self-mutating genetic algorithms is nothing new , either . ";
		String line5 = "The development of compositional distributional models of semantics reconciling the empirical aspects of distributional semantics with the compositional aspects of formal semantics is a popular topic in the contemporary literature.";

		ExVerbLineExtractor extractor = new ExVerbLineExtractor(line1);
		ExtractionResult result = extractor.extract();
		String resultText = "[ I ] [ do n't have ] [ anything ] [ to say ] [ you ]";
		assertEquals(result.getParsedChunkGraph().getChunkedText(), resultText);

		extractor = new ExVerbLineExtractor(line5);
		result = extractor.extract();
		resultText = "[ The development ] [ compositional distributional models ] [ semantics ] [ reconciling ] [ the empirical aspects ] [ distributional semantics ] [ the compositional aspects ] [ formal~semantics ] [ is ] [ a popular topic ] [ the contemporary literature ]";
		System.out.println(result.getParsedChunkGraph().getChunkedText());
		assertEquals(result.getParsedChunkGraph().getChunkedText(), resultText);
	}
}
