package org.whuims.crawler;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

public class AbstractAdder {
	private String dirPath = "";
	Set<String> dict = new HashSet<String>();

	public AbstractAdder(String dirPath) {
		super();
		this.dirPath = dirPath;
	}

	public void work() throws IOException, SQLException {
		List<String> alines = FileUtils.readLines(new File(
				"resource/wordsEn.txt"));
		for (String line : alines) {
			dict.add(line.trim());
		}
		File dir = new File(this.dirPath);
		File[] files = dir.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
			Connection conn = Mysql.getConn("semsearch");
			String id = file.getName().replace(".html", "");
			PreparedStatement pstmt = conn
					.prepareStatement("update acl_paper set abstract=? where acl_id = ?");
			List<String> lines = FileUtils.readLines(file);
			boolean ok = false;
			StringBuilder sb = new StringBuilder();
			for (String line : lines) {
				if (ok && line.trim().equals("</div>")) {
					ok = false;
					break;
				}
				if (ok) {
					sb.append(line);
					sb.append(" ");
				}
				if (line.trim().equals("<div id=\"abstract\">")) {
					ok = true;
				}
			}
			String summary = correct(sb.toString());
//			System.out.println(summary.trim());
			pstmt.setString(1, summary.trim());
			pstmt.setString(2, id);
			pstmt.execute();
			pstmt.close();
			conn.close();
		}
	}

	private String correct(String line) {
		StringBuilder sb = new StringBuilder();
		line = line.trim().replace("<p>", "").replace("</p>", "");
		line = line.replaceAll("\\s+", " ");
		String[] array = line.split("\\s+");
		for (int i = 0; i < array.length; i++) {
			if (array[i].endsWith("-") && i < array.length - 1) {
				String word = array[i].substring(0, array[i].length() - 1)
						+ array[i + 1];
				String anotherWord=word.toLowerCase();
				anotherWord=anotherWord.replaceAll("\\p{Punct}+$", "");
				if (dict.contains(anotherWord)) {
					sb.append(word).append(" ");
					i++;
				}else{
					sb.append(array[i].trim()).append(" ");
				}
			} else {
				sb.append(array[i]).append(" ");
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String dirPath = "D:\\acl_metadata";
		AbstractAdder adder = new AbstractAdder(dirPath);
		try {
			adder.work();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
