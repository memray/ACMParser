package org.whuims.acm.ling.stemmer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.whuims.easynlp.ling.stemmer.porterStemmer.PorterStemmer;

public class PorterStemmerWrapperTest {

	@Test
	public void testStemWord() {
		String word1 = "abandoned";
		String result = PorterStemmer.stemWord(word1);
		assertEquals(result, "abandon");
		try {
			List<String> sourceList = FileUtils.readLines(new File(
					"resource/test/stemmer/test.txt"));
			List<String> targetList=FileUtils.readLines(new File("resource/test/stemmer/out.txt"));
			for(int i=0;i<sourceList.size();i++){
				assertEquals(targetList.get(i),PorterStemmer.stemWord(sourceList.get(i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
