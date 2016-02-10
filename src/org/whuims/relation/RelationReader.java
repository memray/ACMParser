package org.whuims.relation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class RelationReader {
	private String filePath = "";

	public RelationReader(String filePath) {
		super();
		this.filePath = filePath;
	}

	public void read() throws IOException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<String> lines = FileUtils.readLines(new File(this.filePath));
		for (String line : lines) {
			if (line.startsWith("###") || line.equals("")) {
				continue;
			}
			String[] array = line.split("\t");
			if (array.length < 3) {
				continue;
			}
			String rel = array[1].trim().toLowerCase();
			if (!map.containsKey(rel)) {
				map.put(rel, 0);
			}
			int count = map.get(rel);
			count++;
			map.put(rel, count);

		}
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() < 5) {
				continue;
			}
			sb.append(entry.getKey()).append("#").append(entry.getValue())
					.append("\r\n");
		}
		FileUtils.write(new File("h:\\rel_count.txt"), sb.toString().trim());
	}

	public static void main(String[] args) {
		String filePath = "h:/parsed_sent.txt";
		RelationReader reader = new RelationReader(filePath);
		try {
			reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
