package org.whuims;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

public class Downloader {
	Set<String> idSet = new HashSet<String>();

	public Downloader() {
		super();
		init();
	}

	private void init() {
		Connection conn = null;
		Statement stmt = null;
		conn = Mysql.getConn("semsearch");
		String sql = "select acl_id from acl_paper";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String aclID = rs.getString("acl_id");
				this.idSet.add(aclID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void work() {
		StringBuilder sb = new StringBuilder();
		for (String str : this.idSet) {
			sb.append("http://clair.eecs.umich.edu/aan/paper.php?paper_id=").append(str);
			sb.append("\n");
		}
		try {
			FileUtils.write(new File("H:\\urls.txt"), sb.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Downloader d = new Downloader();
		d.work();
	}
}
