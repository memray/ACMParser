package org.whuims.easynlp.parserwrapper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.function.cupervised.CuperSpan;

import edu.stanford.nlp.util.StringUtils;

public class BiaozhuDataFetcher {
	public static void main(String[] args) {
		try {
			Connection conn = Mysql.getConn("semsearch");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from temp_annonated where label=1 or label=12");
			while (rs.next()) {
				Long id = rs.getLong("id");
				String title = rs.getString("title");
				String summary = rs.getString("summary");
				if (summary == null)
					continue;
				// String[] array = title.split(" [Uu]sing ");
				StringBuilder sb = new StringBuilder();
				sb.append(title).append("\r\n\r\n");
				for (Sentence sent : PaperUtils.paraProductOf(summary).getSentences()) {
					List<CuperSpan> spans = ShallowParser.createInstance().chunkToList(sent.getTokenArray(),
							sent.getPostagArray());
					for (CuperSpan span : spans) {
						sb.append(StringUtils.join(span.getWords(), "_")).append(" ");
					}
					sb.append("\r\n\r\n");
				}
				FileUtils.write(new File("f:\\annonate\\" + id + ".txt"), sb.toString().trim());
				FileUtils.write(new File("f:\\annonate\\" + id + ".ann"), "");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
