package org.whuims.easynlp.entity.acmloader.lucene;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class FindAcmDuplicateSummary {
	public static void main(String[] args) {
		Set<Long> set = new TreeSet<Long>();
		Multimap<String, Long> map = HashMultimap.create();
		Connection conn = Mysql.getConn("semsearch");
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select id,summary from acm_paper");
			while (rs.next()) {
				Long id = rs.getLong("id");
				String summary = rs.getString("summary");
				if (summary == null)
					summary = "";
				map.put(summary, id);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		for (String str : map.keySet()) {
			Collection<Long> col = map.get(str);
			if (col.size() > 1) {
				System.out.println(col.size());
				for (Long id : col) {
					sb.append(id).append("\r\n");
				}
			}
		}
		try {
			FileUtils.write(new File("g:\\duplicate.log"), sb.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
		conn=Mysql.getConn("semsearch");
		

	}
}
