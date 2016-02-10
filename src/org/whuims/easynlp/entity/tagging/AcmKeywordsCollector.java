package org.whuims.easynlp.entity.tagging;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

import edu.stanford.nlp.util.MutableInteger;

public class AcmKeywordsCollector {
	public static Map<String, MutableInteger> count(List<String> words) {
		Map<String, MutableInteger> counter = new HashMap<String, MutableInteger>();
		for (String w : words) {
			MutableInteger initValue = new MutableInteger(1);
			// 利用 HashMap 的put方法弹出旧值的特性
			MutableInteger oldValue = counter.put(w, initValue);
			if (oldValue != null) {
				initValue.set(oldValue.intValue() + 1);
			}
		}
		return counter;
	}

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		conn = Mysql.getConn("acm_tsinghua");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select keywords from paper");
			List<String> list = new ArrayList<String>();
			while (rs.next()) {
				String keywordLine = rs.getString("keywords");
				if (keywordLine != null) {
					String keywords[] = keywordLine.split("[,;]+");
					for (String str : keywords) {
						str = str.trim();
						if (!str.equals("")) {
							list.add(str);
						}
					}
				}
			}
			rs.close();
			stmt.close();
			conn.close();
			Map<String, MutableInteger> map = count(list);
			StringBuilder sb = new StringBuilder();
			for (Entry<String, MutableInteger> entry : map.entrySet()) {
				if (entry.getKey().split("\\s").length > 1)
					sb.append(entry.getKey()).append("\t").append(entry.getValue()).append("\n");
			}
			FileUtils.write(new File("f:\\keywords_big2.txt"), sb.toString());
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
