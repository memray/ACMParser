package org.whuims.easynlp.phrase;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Each phrase is stored in a PhraseNode object
 * All the phrases share the same first word are stored in a same PhraseNode
 */
public class PhraseNode {
    String word;

    Map<String, Integer> types = new TreeMap<String, Integer>();

    // 后续节点
    private Map<String, PhraseNode> map = new HashMap<String, PhraseNode>();

    /**
     * Add a new phrase into our dict by iteration, adding word by word
     * This function would deal with the ith word in the phrase
     * @param words the whole string[] of the phrase
     * @param i the current index of word to be added
     * @param type postag
     * @param freq frequency of this phrase
     * @return
     */
    public boolean add(String[] words, int i, String type, int freq) {
        this.word = words[i];
        // if i reaches the length of phrases, process complete
        if (words.length == i + 1) {
            this.types.put(type, freq);
            return true;
        }
        i++;
        // map stores the (i+1)th word, make a linked list by internal variables
        if (!map.containsKey(words[i])) {
            map.put(words[i], new PhraseNode());
        }
        PhraseNode node = map.get(words[i]);
        // deal with the next word
        return node.add(words, i, type, freq);
    }

    public PhraseNode find(String word) {
        return this.map.get(word);
    }

    /**
     * Check whether this phrase(words) is stored in our dict, finding word by word
     * @param words
     * @param depth
     * @return
     */
    public PhraseNode find(String[] words, int depth) {
        depth++;
        //
        if (depth > words.length) {
            return null;
        } else if (depth == words.length) {
            return this;
        }
        if (this.map.containsKey(words[depth])) {
            return this.map.get(words[depth]).find(words, depth);
        }
        return null;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<String, Integer> getTypes() {
        return types;
    }

    public void setTypes(Map<String, Integer> types) {
        this.types = types;
    }

}
