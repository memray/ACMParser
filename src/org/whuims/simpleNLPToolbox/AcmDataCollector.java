package org.whuims.simpleNLPToolbox;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.parserwrapper.SentDetector;
import org.whuims.easynlp.tokenizer.StanfordPTBTokenizer;

import edu.stanford.nlp.util.StringUtils;

public class AcmDataCollector {
	List<String> summaries = new ArrayList<String>();
	List<String> titles = new ArrayList<String>();
	StringBuilder sumSb = new StringBuilder();

	public AcmDataCollector() {
		init();
	}

	private void init() {
		try {
			Connection conn = Mysql.getConn("semsearch");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select id,title,summary from acm_paper");
			int count = 0;
			while (rs.next()) {
				String title = rs.getString("title");
				String summary = rs.getString("summary");
				if (title == null)
					title = "";
				if (summary == null)
					summary = "";
				
				for (String str : SentDetector.createInstance().detect(summary)) {
					String array[] = StanfordPTBTokenizer.tokenizeToArray(str);
					String text = StringUtils.join(array, " ");
					this.summaries.add(text);
					sumSb.append(text).append("\r\n");
				}

				count++;
				if (count % 1000 == 0) {
					System.out.println(count);
				}
			}
			FileUtils.write(new File("f:\\summarySents.txt"), sumSb.toString().trim());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<String> getSummaries() {
		return this.summaries;
	}

	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
		set.add("we");
		set.add("I");
		set.add("We");
		AcmDataCollector col = new AcmDataCollector();
		StringBuilder sb = new StringBuilder();
		for (String line : col.getSummaries()) {
			String[] array = line.trim().split("\\s+");
			for (int i = 0; i < array.length; i++) {
				if (set.contains(array[i]) && i < array.length - 1) {
					sb.append(array[i + 1]).append("\r\n");
				}
			}
		}
		try {
			FileUtils.write(new File("f:\\we.txt"), sb.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
