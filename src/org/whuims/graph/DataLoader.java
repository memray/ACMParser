package org.whuims.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class DataLoader {
	private String filePath = "";
	private CatTokenMat mat = null;

	public DataLoader(String filePath) {
		super();
		this.filePath = filePath;
	}

	public void load() throws IOException {
		mat = new CatTokenMat("dbpedia_domain");
		BufferedReader reader = new BufferedReader(new FileReader(new File(this.filePath)));
		String line = reader.readLine();
		int count = 0;
		while (line != null) {
			count++;
			if (count % 300000 == 0) {
				System.out.println(count);
			}
			line = line.trim();
			String[] array = line.split(" ");
			if (array.length == 4) {
				String instance = array[0].trim();
				String cat = array[2].trim();
				String instanceID = TokenIDMap.getInstanceID(instance);
				String catID = TokenIDMap.getCatID(cat);
				// System.out.println(instance+"\t"+instanceID+"\t---\t"+cat+"\t"+catID);
				mat.addPair(catID, instanceID);
			} else {
			}
			line = reader.readLine();
		}
		reader.close();
	}

	public CatTokenMat getMat() {
		return mat;
	}

	public void setMat(CatTokenMat mat) {
		this.mat = mat;
	}

	public static void main(String[] args) {
		DataLoader loader = new DataLoader("g:\\categories.slimmed.txt");
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collection<String> col = loader.getMat().findCats("i806699");
		System.out.println(col.size());
		for (String str : col) {
			System.out.println(str);
		}
	}

}
