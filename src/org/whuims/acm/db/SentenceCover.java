package org.whuims.acm.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.parserwrapper.SentDetector;

public class SentenceCover {
	Connection conn = null;
	Statement stmt = null;

	public void cover() throws SQLException, IOException {
		conn = Mysql.getConn("acm");
		stmt = conn.createStatement();
		ResultSet rs = stmt
				.executeQuery("select article_id,Acm_Article_Title, Acm_Article_Abstract from acm_article");
		int count=0;
		while (rs.next()) {
			count++;
			if(count%1000==0){
				System.out.println(count);
			}
			long id = rs.getLong("article_id");
			String title = rs.getString("Acm_Article_Title");
			String theAbstract = rs.getString("Acm_Article_Abstract");
			process(id, title, theAbstract);
		}
	}

	private void process(long id, String title, String theAbstract)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(id+" is a zznumber").append("\r\n");
		if (title != null) {
			sb.append(title).append("\r\n");
		}
		if (theAbstract != null) {
			for (String str : SentDetector.createInstance().detect(theAbstract)) {
				sb.append(str).append("\r\n");
			}
		}
		FileUtils.write(new File("H:\\acm_sents_withIDSeparator.txt"), sb.toString(),true);
	}

	public static void main(String[] args) {
		SentenceCover cover = new SentenceCover();
		try {
			cover.cover();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
