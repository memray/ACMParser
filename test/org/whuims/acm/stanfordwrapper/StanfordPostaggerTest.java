package org.whuims.acm.stanfordwrapper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whuims.easynlp.parserwrapper.StanfordPostagger;

public class StanfordPostaggerTest {

    @Test
    public void testPostag() {
        String line="1. Introduction";
        String taggedLine=StanfordPostagger.createInstance().postag(line);
        System.out.println();
        assertEquals(taggedLine,"1_LS ._. Introduction_NN");
    }

    @Test
    public void testPostagAndSplit() {
        String line = "1. Introduction";
        String[][] result = StanfordPostagger.createInstance()
                .postagAndSplit(line);
        String[] tokens = result[0];
        assertEquals(tokens[0], "1");
        assertEquals(tokens[1], ".");
        assertEquals(tokens[2], "Introduction");

    }

}
