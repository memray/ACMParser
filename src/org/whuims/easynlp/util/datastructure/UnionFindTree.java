package org.whuims.easynlp.util.datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class UnionFindTree<T> {
	List<Integer> fathers;
	Map<T, Integer> map = new HashMap<T, Integer>();// 索引每个元素在father数组中的位置
	Map<Integer, T> idMap = new HashMap<Integer, T>();

	public UnionFindTree(T[] ts) {
		super();
		init(ts);
	}

	public UnionFindTree() {
		this.fathers = new ArrayList<Integer>();
	}

	public Map<T, List<T>> findAllGroupMaps() {
		Map<T, List<T>> result = new TreeMap<T, List<T>>();
		for (Entry<T, Integer> entry : map.entrySet()) {
			T t = entry.getKey();
			T label = this.find(t);
			if (!result.containsKey(this.find(t))) {
				result.put(label, new ArrayList<T>());
			}
			result.get(label).add(t);
		}
		return result;
	}

	public List<List<T>> findAllGroups() {
		Map<T, List<T>> map = this.findAllGroupMaps();
		List<List<T>> result = new ArrayList<List<T>>();
		for (Entry<T, List<T>> entry : map.entrySet()) {
			result.add(entry.getValue());
		}
		return result;
	}

	public Integer addElement(T t) {
		if (map.containsKey(t)) {
			return map.get(t);
		} else {
			int value = map.size();
			this.map.put(t, value);
			this.fathers.add(value);
			this.idMap.put(value, t);
		}
		return map.get(t);
	}

	private void init(T[] ts) {
		this.fathers = new ArrayList<Integer>();
		for (int i = 0; i < ts.length; i++) {
			this.fathers.add(i);
			this.map.put(ts[i], i);
			this.idMap.put(i, ts[i]);
		}
	}

	public T find(T t) {
		int location = map.get(t);
		int root = findInt(location);
		return this.idMap.get(root);
	}

	public boolean isSameGroup(T t1, T t2) {
		if (!this.map.containsKey(t1) || !this.map.containsKey(t2)) {
			return false;
		}
		int l1 = map.get(t1);
		int l2 = map.get(t2);
		if (findInt(l1) == findInt(l2)) {
			return true;
		} else {
			return false;
		}
	}

	private int findInt(int location) {
		if (location != fathers.get(location)) {
			fathers.set(location, findInt(fathers.get(location)));
		}
		return fathers.get(location);
	}

	public void union(T t1, T t2) {
		if (!this.map.containsKey(t1)) {
			this.addElement(t1);
		}
		if (!this.map.containsKey(t2)) {
			this.addElement(t2);
		}
		if (t1 == t2 || t1.equals(t2)) {
			return;
		}
		int tl1 = map.get(t1);
		int tl2 = map.get(t2);
		int f1 = this.findInt(tl1);
		int f2 = this.findInt(tl2);
		if (f1 == f2) {
			return;
		} else {
			if (t1.hashCode() > t2.hashCode()) {
				fathers.set(f2, f1);
			} else {
				fathers.set(f1, f2);
			}
		}
	}

	public static void main(String[] args) {
		Integer[] intArray = new Integer[] { 1, 2, 3, 4, 5, 12, 15, 26, 38, 50 };
		UnionFindTree<Integer> ut = new UnionFindTree<Integer>();
		ut.addElement(1);
		ut.addElement(12);
		System.out.println(ut.find(1));
		ut.union(1, 12);
		ut.addElement(2);
		ut.union(1, 1);
		System.out.println(ut.find(1));
		ut.addElement(15);
		ut.union(1, 15);
		ut.addElement(26);
		ut.union(2, 26);
		ut.addElement(38);
		ut.addElement(3);
		ut.union(3, 38);
		ut.addElement(5);
		ut.addElement(50);
		ut.addElement(50);
		ut.union(5, 50);
		System.out.println("==============================");
		System.out.println(ut.find(1));
		System.out.println(ut.find(12));
		System.out.println(ut.find(15));
		System.out.println("==============================");
		System.out.println(ut.find(2));
		System.out.println(ut.find(26));
		System.out.println("==============================");
		System.out.println(ut.find(5));
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		List<List<Integer>> list=ut.findAllGroups();
		for(List<Integer> tempList:list){
			for(Integer i:tempList){
				System.out.print(i+", ");
			}
			System.out.println();
		}
	}
}
