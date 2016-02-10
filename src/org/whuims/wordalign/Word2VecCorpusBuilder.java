package org.whuims.wordalign;

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
import org.whuims.easynlp.phrase.PhraseTokenizer;

public class Word2VecCorpusBuilder {
	public void work() throws SQLException {
		Connection conn = Mysql.getConn("semsearch");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from acm_paper");
		StringBuilder sb = new StringBuilder();
		while (rs.next()) {
			String title = rs.getString("title");
			String summary = rs.getString("summary");
			if (title != null) {
				sb.append(process(title));
			}
			if (summary != null) {
				sb.append(process(summary));
			}
			sb.append("\n");
		}
		try {
			FileUtils.write(new File("f:\\corpus\\finegrained_phrase.txt"), sb.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String process(String text) {
		text = text.trim();
		Sentence sent = PaperUtils.sentProductOf(text);
		sent=PhraseTokenizer.createInstance().tokenize(sent);
		return sent.getText();
	}

	public static void main(String[] args) {
		Word2VecCorpusBuilder builder = new Word2VecCorpusBuilder();
		try {
			builder.work();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
