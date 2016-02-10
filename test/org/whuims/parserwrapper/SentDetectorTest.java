package org.whuims.parserwrapper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whuims.easynlp.parserwrapper.SentDetector;

public class SentDetectorTest {

	@Test
	public void test() {
		String line = "These rules capture typical US addresses, like: 5500 Main St., Williamsville, NY14221; 12345 Xyz Avenue, Apt. 678, Los Angeles, CA98765-4321.";
		String[] array = SentDetector.createInstance().detect(line);
		assertEquals(array.length,1);
	}

}
