package org.whuims.graph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

public class Test {
	public static void main(String[] args) {
		Multimap<String, String> map = HashMultimap.create();
		map.put("a", "1");
		map.put("a", "2");
		System.out.println();
		map.put("a", "3");
		System.out.println(map.size());
		map.put("a", "4");
		System.out.println(map.size());
		map.put("b", "1");
		System.out.println(map.get("a").size());
	}
}
