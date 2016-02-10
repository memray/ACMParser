package org.whuims.easynlp.exverbsimple;

import java.util.Set;
import java.util.TreeSet;

public class Test {
    public static void main(String[] args) {
        Set<Integer> set = new TreeSet<Integer>();
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        Integer three = new Integer(3);
        set.add(one);
        set.add(two);
        set.add(three);
        set.add(one);
        set.add(one);
        for (Integer i : set) {
            System.out.println(i);
        }
    }
}
