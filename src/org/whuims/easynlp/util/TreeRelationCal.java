package org.whuims.easynlp.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.parserwrapper.StanfordParser;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class TreeRelationCal {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(new File(args[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : lines) {
            if (line.endsWith(" is a zznumber")) {
                System.out.println("#########\t" + line.split(" ")[0]);
                continue;
            }
            Tree tree = StanfordParser.createInstance().parse(line);
            GrammaticalStructure gs = StanfordParser.createInstance().pack(tree);
            for (TypedDependency td : gs.allTypedDependencies()) {
                System.out.println(td.gov() + "\t" + td.dep() + "\t"
                        + td.reln().getShortName());
                if (!map.containsKey(td.reln().getShortName())) {
                    map.put(td.reln().getShortName(), 0);
                }
                int count = map.get(td.reln().getShortName());
                count++;
                map.put(td.reln().getShortName(), count);
            }
            System.out.println("");
        }
        for (Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

}
