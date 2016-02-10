package org.whuims.easynlp.phrase;

import static org.junit.Assert.*;

import org.junit.Test;

public class PhraseDictionaryTest {
    @Test
    public void addWords() {
        PhraseDictionary dict = new PhraseDictionary();
        dict.addPhrase("support vector machine".split(" "), "noun", 1);
        dict.addPhrase("support vector machines".split(" "), "noun", 1);
        dict.addPhrase("support vector machine".split(" "), "noun", 1);
        dict.addPhrase("support vector".split(" "), "noun", 1);
        dict.addPhrase("support".split(" "), "noun", 1);
        dict.addPhrase("maximum entropy modeling".split(" "), "noun", 1);
        assertEquals(dict.getSize(), 5);
        assertTrue(dict.exists("support vector machine".split(" ")) == 2);
        assertTrue(dict.exists("support vector machines".split(" ")) == 2);
        assertTrue(dict.exists("support vector machine".split(" ")) == 2);
        assertTrue(dict.exists("support vector".split(" ")) == 2);
        assertTrue(dict.exists("support".split(" ")) == 2);
        assertTrue(dict.exists("maximum entropy modeling".split(" ")) == 2);

        assertFalse(dict.exists("support vector machine learning".split(" ")) == 2);
        assertFalse(dict.exists("support machine".split(" ")) == 2);
        assertFalse(dict.exists("support s".split(" ")) == 2);
        assertFalse(dict.exists("maximum entropy".split(" ")) == 2);
        assertFalse(dict.exists("maximum".split(" ")) == 2);
        assertFalse(dict.exists("maxismum entropy".split(" ")) == 2);
        assertFalse(dict.exists("support vector inter machine".split(" ")) == 2);
        assertFalse(dict.exists("semi maximum entropy modeling".split(" ")) == 2);

    }

}
