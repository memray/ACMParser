package org.whuims.simpleNLPToolbox;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.phrase.PhraseRegexTokenizer;
import org.whuims.easynlp.phrase.PhraseTokenizer;

public class TitleLoader {
	public static void main(String[] args) throws SQLException {
		StringBuilder sb=new StringBuilder();
		Connection conn = null;
		Statement stmt = null;
		conn = Mysql.getConn("semsearch");
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from acl_paper");
		int count = 0;
		while (rs.next()) {
			count++;
			if (count % 1000 == 0) {
				System.out.println(count);
			}
			long id = rs.getLong("id");
			String aclID = rs.getString("acl_id");
			String title = rs.getString("title");
			Sentence sent=PaperUtils.sentProductOf(title);
			sent = PhraseTokenizer.createInstance().longestTokenize(sent);
			sent = PhraseRegexTokenizer.createInstance().tokenize(sent);
			title=sent.getText();
			sb.append(aclID).append(" ").append(title).append("\n");
		}
		try {
			FileUtils.write(new File("resource/data/sequence/title.txt"), sb.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
