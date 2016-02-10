package org.whuims.graph;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * 实际存储了网络关系
 * 
 * @author Qikai
 *
 */
public class CatTokenMat {
	private String name = "DEFAULT";
	private Multimap<String, String> catToInstanceMap = HashMultimap.create();
	private Multimap<String, String> instanceToCatMap = HashMultimap.create();

	public CatTokenMat(String name) {
		super(); 
		this.name = name;
	}

	public void addPair(String catID, String instanceID) {
		this.catToInstanceMap.put(catID, instanceID);
		this.instanceToCatMap.put(instanceID, catID);
	}

	public Collection<String> findCats(String instanceID) {
		return this.instanceToCatMap.get(instanceID);
	}

	public Collection<String> findIntances(String catID) {
		return this.catToInstanceMap.get(catID);
	}

	public int getCatSize() {
		return this.catToInstanceMap.size();
	}

}
