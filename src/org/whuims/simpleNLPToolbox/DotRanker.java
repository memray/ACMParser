package org.whuims.simpleNLPToolbox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.whuims.commons.util.Pair;

public class DotRanker {
	private String filePath;

	public DotRanker(String filePath) {
		super();
		this.filePath = filePath;
	}

	public void rank() throws IOException {
		List<String> lines = null;
		lines = FileUtils.readLines(new File(this.filePath));

		List<Pair> list = new ArrayList<Pair>();
		for (String line : lines) {
			int index = line.length()+10000;
			if (line.contains(":")) {
				index = line.indexOf(":");
			}
			Pair pair = new Pair(line, index);
			list.add(pair);
		}
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (Pair pair : list) {
			System.out.println(pair.getKey());
			sb.append(pair.getKey()).append("\r\n");
		}
		FileUtils.write(new File(this.filePath + ".sort"), sb.toString());
	}

	public static void main(String[] args) {
		DotRanker ra = new DotRanker("resource/data/sequence/title.txt");
		try {
			ra.rank();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
