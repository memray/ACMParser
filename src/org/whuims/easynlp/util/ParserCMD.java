package org.whuims.easynlp.util;

import java.util.Scanner;

import org.whuims.easynlp.parserwrapper.StanfordParser;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class ParserCMD {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String line=scanner.nextLine();
        while(line!=null){
            Tree tree=StanfordParser.createInstance().parse(line);
            tree.pennPrint();
            GrammaticalStructure gs=StanfordParser.createInstance().pack(tree);
            for(TypedDependency td:gs.allTypedDependencies()){
                System.out.println(td.gov()+"\t"+td.dep()+"\t"+td.reln().getLongName());
            }
            System.out.println("---------------------------------------------");
            line=scanner.nextLine();
        }
    }

}
