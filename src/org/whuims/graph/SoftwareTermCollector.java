package org.whuims.graph;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class SoftwareTermCollector {
	private String filePath;
	private CatTokenMat mat = null;
	private Set<String> ommittedCatSet = new HashSet<String>();
	private Set<String> ommittedInstanceSet = new HashSet<String>();
	private Multiset<String> multiCatSet = HashMultiset.create();
	private Multiset<String> multiInstanceSet = HashMultiset.create();

	public SoftwareTermCollector(String filePath) {
		super();
		this.filePath = filePath;
		DataLoader loader = new DataLoader(this.filePath);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mat = loader.getMat();
		try {
			for (String str : FileUtils.readLines(new File("seeds.txt"))) {
				multiInstanceSet.add(str.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void collect() {
		int addCount = 1;
		while (addCount > 0) {
			// 使用seeds内的元素获取cat
			for (String s : multiInstanceSet.elementSet()) {
				if (ommittedInstanceSet.contains(s)) {
					continue;
				}
				String instanceID = TokenIDMap.getInstanceID(s);
//				System.out.println(s+"\t对应ID\t"+instanceID);
				Collection<String> catIDs = mat.findCats(instanceID);
//				System.out.println(catIDs.size());
				for (String catID : catIDs) {
//					System.out.println(catID+"\t"+TokenIDMap.getCatTokenByID(catID));
					multiCatSet.add(catID);
				}
				ommittedInstanceSet.add(s);
			}
			addCount = 0;
			for (String catID : multiCatSet.elementSet()) {
				if (ommittedCatSet.contains(catID)) {
					continue;
				}
				Collection<String> instanceIDs = mat.findIntances(catID);
				for (String instanceID : instanceIDs) {
					if (!multiInstanceSet.contains(instanceID)) {
						addCount++;
					}
					multiInstanceSet.add(instanceID);
				}
				ommittedCatSet.add(catID);
			}
		}
		StringBuilder catSb = new StringBuilder();
		for (String catID : multiCatSet.elementSet()) {
			catSb.append(TokenIDMap.getCatTokenByID(catID)).append("\t").append(multiCatSet.count(catID))
					.append("\r\n");
		}
		try {
			FileUtils.write(new File("cat.txt"), catSb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		catSb = null;
		StringBuilder instanceSb = new StringBuilder();
		for (String instanceID : multiInstanceSet.elementSet()) {
//			System.out.println(TokenIDMap.getInstanceTokenByID(instanceID));
			instanceSb.append(TokenIDMap.getInstanceTokenByID(instanceID)).append("\t")
					.append(multiInstanceSet.count(instanceID)).append("\r\n");
		}
		try {
			FileUtils.write(new File("instance.txt"), instanceSb.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		SoftwareTermCollector collector = new SoftwareTermCollector(args[0]);
		collector.collect();
	}

}
