package org.whuims.easynlp.entity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.whuims.easynlp.entity.commonentity.Word;

public class WordTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetText() {
        Word word1=new Word("book","n");
        Word word2=new Word("order","v");
        Word word3=new Word(" book","n");
        Word word4=new Word("order ","v");
        assertEquals(word1.getText(),"book");
        assertEquals(word2.getText(),"order");
        assertEquals(word3.getText()," book");
        assertEquals(word4.getText(),"order ");
    }

    @Test
    public void testSetText() {
        Word word1=new Word("","");
        word1.setText("book");
        assertEquals(word1.getText(),"book");
    }

    @Test
    public void testGetPostag() {
        Word word1=new Word("","");
        word1.setPostag("n");
        assertEquals(word1.getPostag(),"n");
    }

    @Test
    public void testSetPostag() {
        Word word1=new Word("","");
        word1.setPostag("n a");
        assertEquals(word1.getPostag(),"n a");
    }

    @Test
    public void testGetRawText() {
        
    }

}
