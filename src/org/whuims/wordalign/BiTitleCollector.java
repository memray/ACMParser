package org.whuims.wordalign;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.nlpcn.commons.lang.standardization.SentencesUtil;
import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.parserwrapper.SentDetector;
import org.whuims.easynlp.phrase.PhraseTokenizer;
import org.whuims.easynlp.tokenizer.ChineseAnsjTokenizer;

public class BiTitleCollector {
	// 88865 307455
	private SentencesUtil su = new SentencesUtil();

	private void collect() throws SQLException, IOException {
		Connection conn = Mysql.getConn("cnki");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from cnki_papers where titleCn like '%基于%的%'");
		StringBuilder cnSb = new StringBuilder();
		StringBuilder enSb = new StringBuilder();
		StringBuilder idSb = new StringBuilder();
		int count = 0;
		while (rs.next()) {
			long id = rs.getLong("id");
			String titleCn = rs.getString("titleCn").trim();
			String titleEn = rs.getString("titleEn").trim();
			if (titleCn.equals("") || titleEn.equals("")) {
				continue;
			}
			// String absCn = rs.getString("absCn");
			// String absEn = rs.getString("absEn");
			// System.out.println("----\t" + id + "\t" + titleEn);
			titleCn = ChineseAnsjTokenizer.createInstance().tokenizeToString(titleCn).trim();
			titleEn = chunk(titleEn);
			if (titleCn.equals("") || titleEn.equals("")) {
				continue;
			}
			cnSb.append(titleCn.toLowerCase()).append("\n");
			enSb.append(titleEn.toLowerCase()).append("\n");
			idSb.append(count).append("\t").append(id).append("\n");
			count++;
			// process(absCn, absEn, cnSb, enSb);
			if (count % 100 == 0)
				System.out.println(count);
		}
		FileUtils.write(new File("f:\\cn.txt"), cnSb.toString().trim());
		FileUtils.write(new File("f:\\en.txt"), enSb.toString().trim());
		FileUtils.write(new File("f:\\id.txt"), idSb.toString());
	}

	private String chunk(String titleEn) {
		Sentence sentEn = PaperUtils.sentProductOf(titleEn);
		sentEn = PhraseTokenizer.createInstance().tokenize(sentEn);
		return sentEn.getText();
		// List<CuperSpan> spans =
		// StanofordShallowParser.createInstance().chunkToList(sentEn.getTokenArray(),
		// sentEn.getPostagArray());
		// StringBuilder sb = new StringBuilder();
		// for (CuperSpan span : spans) {
		// sb.append(span.getText().replace(" ", "=")).append(" ");
		// }
		// return sb.toString().trim();
	}

	private void process(String absCn, String absEn, StringBuilder cnSb, StringBuilder enSb) {
		List<String> cnList = su.toSentenceList(absCn);
		String[] enList = SentDetector.createInstance().detect(absEn);
		if (cnList.size() == enList.length) {
			for (int i = 0; i < cnList.size(); i++) {
				cnSb.append(cnList.get(i)).append("\n");
				enSb.append(enList[i]).append("\n");
			}
		}
	}

	public static void main(String[] args) {
		BiTitleCollector col = new BiTitleCollector();
		try {
			col.collect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
