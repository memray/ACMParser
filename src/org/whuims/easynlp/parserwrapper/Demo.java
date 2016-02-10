package org.whuims.easynlp.parserwrapper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

public class Demo {
	Connection conn = null;
	Statement stmt = null;

	public void work() throws SQLException {
		conn = Mysql.getConn("acm");
		stmt = conn.createStatement();
		String sql = "SELECT article_id,acm_article.art_pub_year, Acm_Article_Title,Acm_Article_Abstract FROM acm.acm_article,acm_conference_name where acm_article.Acm_Conference_id=acm_conference_name.acm_conference_id and acm_conference_name='SIGIR';";
		ResultSet rs = stmt.executeQuery(sql);
		int count = 0;
		while (rs.next()) {
			long id = rs.getLong(1);
			int year = rs.getInt(2);
			String title = rs.getString("Acm_Article_Title");
			String theAbstract = rs.getString("Acm_Article_Abstract");
			System.out.println(id + "\t" + year + "\t" + title);
			count++;
			process(id, year, title, theAbstract);
		}
		rs.close();
		stmt.close();
		conn.close();
		System.out.println(count);
	}

	private void process(long id, int year, String title, String theAbstract) {
		StringBuilder sb = new StringBuilder();
		String postagTitle = StanfordPostagger.createInstance().postag(title);
		String[] array = SentDetector.createInstance().detect(theAbstract,true);
		StringBuilder tempTitleSb = new StringBuilder();
		for (String t : postagTitle.split("\\s+")) {
			tempTitleSb.append(t.substring(0, t.lastIndexOf("_"))).append(" ");
		}
		postagTitle = tempTitleSb.toString().trim();
		postagTitle = postagTitle.replace("-RRB-", ")").replace("-LRB-", "(");
		sb.append(postagTitle).append("\r\n\r\n");
		for (String str : array) {
			String postagStr = StanfordPostagger.createInstance().postag(str);
			StringBuilder tempSb = new StringBuilder();
			for (String t : postagStr.split("\\s+")) {
				tempSb.append(t.substring(0, t.lastIndexOf("_"))).append(" ");
			}
			postagStr = tempSb.toString().trim();
			postagStr = postagStr.replace("-RRB-", ")").replace("-LRB-", "(");
			sb.append(postagStr).append("\r\n");
		}
		try {
			FileUtils.write(new File("d:\\acm\\sigir\\" + year + "\\" + id
					+ ".txt"), sb.toString().trim());
			FileUtils.write(new File("d:\\acm\\sigir\\" + year + "\\" + id
					+ ".ann"), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Demo demo = new Demo();
		try {
			demo.work();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
