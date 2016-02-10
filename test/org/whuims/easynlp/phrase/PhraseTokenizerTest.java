package org.whuims.easynlp.phrase;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;

public class PhraseTokenizerTest {
    @Test
    public void testTokenize() {
        PhraseDictionary dict = new PhraseDictionary();
        dict.addPhrase("support vector machine".split(" "), "noun", 1);
        dict.addPhrase("support vector machine".split(" "), "noun", 1);
        dict.addPhrase("support".split(" "), "noun", 1);
        dict.addPhrase("support vector".split(" "), "noun", 1);
        dict.addPhrase("maximum entropy modeling".split(" "), "noun", 1);
        Sentence sent = PaperUtils
                .sentProductOf("this paper combines support vector machine, maximum entropy modeling");

        PhraseTokenizer tokenizer = new PhraseTokenizer(dict);
        sent = tokenizer.tokenize(sent);
        System.out.println(sent.getText() + "###############");
        assertEquals(sent.getText(),
                "this paper combines support~vector~machine , maximum~entropy~modeling");
        // =======================================================//
        dict.addPhrase("this paper".split(" "), "noun", 1);
        tokenizer = new PhraseTokenizer(dict);
        sent = PaperUtils
                .sentProductOf("this paper combines support vector machine, maximum entropy modeling");
        sent = tokenizer.tokenize(sent);
        assertEquals(sent.getText(),
                "this~paper combines support~vector~machine , maximum~entropy~modeling");
        // =======================================================//
        sent = PaperUtils
                .sentProductOf("this paper combines support vector machine maximum entropy modeling");
        tokenizer = new PhraseTokenizer(dict);
        sent = tokenizer.tokenize(sent);
        assertEquals(sent.getText(),
                "this~paper combines support~vector~machine maximum~entropy~modeling");
        sent = PaperUtils
                .sentProductOf("this paper combines support vector machine maximum entropy modeling");
        tokenizer = new PhraseTokenizer(dict);
        sent = tokenizer.tokenize(sent);
        assertEquals(sent.getText(),
                "this~paper combines support~vector~machine maximum~entropy~modeling");

    }

}
