package org.whuims.easynlp.entity.tagging;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Paragraph;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.phrase.PhraseRegexTokenizer;
import org.whuims.easynlp.phrase.PhraseTokenizer;

public class ACMPaperCollector {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;

	public void collect() throws SQLException, IOException {
		conn = Mysql.getConn("acm_tsinghua");
		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select id,title,summary from paper where paper.title like '%support vector machine%' or paper.title like '% SVM%' ");
		while (rs.next()) {
			long id = rs.getLong("id");
			List<Sentence> sents = new ArrayList<Sentence>();
			String title = rs.getString("title");
			if (title == null)
				title = "";
			title = title.trim();
			String summary = rs.getString("summary");
			if (summary == null)
				summary = "";
			summary = summary.trim();
			Sentence titleSent = PaperUtils.sentProductOf(title);
			sents.add(titleSent);
			String[] blocks = summary.split("[\r\n]");
			for (String block : blocks) {
				Paragraph para = PaperUtils.paraProductOf(block.trim());
				for (Sentence sent : para.getSentences()) {
					sents.add(sent);
				}
			}
			process(id, sents);
		}
		rs.close();
		stmt.close();
		conn.close();
	}

	private void process(long id, List<Sentence> sents) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (Sentence sent : sents) {
			sent = PhraseTokenizer.createInstance().longestTokenize(sent);
			sent = PhraseRegexTokenizer.createInstance().tokenize(sent);
			sb.append(sent.getText()).append("\n");
		}
		FileUtils.write(new File("f:\\svmtext\\" + id + ".txt"), sb.toString());
		new File("f:\\svmtext\\" + id + ".ann").createNewFile();
	}

	public static void main(String[] args) {
		ACMPaperCollector co = new ACMPaperCollector();
		try {
			co.collect();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

}
