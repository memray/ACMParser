package org.whuims.acm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.parserwrapper.StanfordShallowParser;
import org.whuims.easynlp.phrase.PhraseTokenizer;
import org.whuims.function.cupervised.CuperSpan;

public class SummaryChunkBuilder {
	Map<Long, String> chunkMap = new TreeMap<Long, String>();
	private int start;
	private int end;

	public SummaryChunkBuilder(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}

	public void work() throws SQLException {
		chunk();
		save();
	}

	private void chunk() throws SQLException {
		Connection conn = Mysql.getConn("semsearch");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT * FROM acm_paper WHERE summary is not null and id <" + end + " and id>=" + start);
		// +" and id in (select id from temp_annonated where label=1)"
		int size = 0;
		while (rs.next()) {
			long id = rs.getLong("id");
			String summary = rs.getString("summary");
			System.out.println(id);
			try {
				String summaryChunk = process(summary);
				chunkMap.put(id, summaryChunk);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			size++;
			if (size % 100 == 0) {
				this.save();
				this.chunkMap.clear();
				System.out.println("提交结果，当前" + size);
			}
		}
		rs.close();
		stmt.close();
		conn.close();
	}

	/**
	 * 清理文本，文本中可能存在句号后没有空格的情形，特别是在cnki数据中这种现象尤其多，因此利用规则为cnki文档中的可能的句号后面添加空格。
	 * 
	 * @param abs
	 * @return
	 */
	private String clean(String abs) {
		StringBuilder sb = new StringBuilder();
		char[] chars = abs.toCharArray();
		if (chars.length < 3) {
			return abs;
		}
		for (int i = 0; i < chars.length - 1; i++) {
			sb.append(chars[i]);
			if (i > 0 && Character.isLowerCase(chars[i - 1]) && Character.isUpperCase(chars[i + 1])
					&& (chars[i] == '.' || chars[i] == '!' || chars[i] == '?' || chars[i] == '。')) {
				sb.append(' ');
			}
		}
		sb.append(chars[chars.length - 1]);
		return sb.toString().trim();
	}

	private String process(String summary) {
		List<List<String>> chunks = new ArrayList<List<String>>();
		if (summary == null)
			summary = "";
		summary = this.clean(summary);
		String[] array = org.whuims.easynlp.parserwrapper.SentDetector.createInstance().detect(summary.trim());
		int lineCount = 0;
		for (String str : array) {
			// if(!(str.contains("(") || str.contains(")") || str.contains("[")
			// || str.contains("{"))){
			// continue;
			// }
			Sentence sent = PaperUtils.sentProductOf(str.trim());
			sent = PhraseTokenizer.createInstance().tokenize(sent);
			List<CuperSpan> temp = StanfordShallowParser.createInstance().chunkToList(sent.getTokenArray(),
					sent.getPostagArray());
			List<String> lineArray = new ArrayList<String>();
			int wordCount = 0;
			for (CuperSpan span : temp) {
				lineArray.add(
						span.getText().replace("~", " ") + ":" + span.getType() + "_" + lineCount + "_" + wordCount);
				wordCount++;
			}
			chunks.add(lineArray);
			lineCount++;
		}
		StringBuilder sb = new StringBuilder();
		for (List<String> list : chunks) {
			for (String str : list) {
				sb.append(str).append("\t");
			}
			sb.append("\n");
		}
		return sb.toString().trim();
	}

	private void save() throws SQLException {
		Connection conn = Mysql.getConn("semsearch");
		java.sql.PreparedStatement pstmt = conn.prepareStatement("update acm_paper set summarychunk = ? where id= ?");
		for (Entry<Long, String> entry : this.chunkMap.entrySet()) {
			long id = entry.getKey();
			String value = entry.getValue();
			pstmt.setLong(2, id);
			pstmt.setString(1, value);
			pstmt.executeUpdate();
		}
		pstmt.close();
		conn.close();
	}

	public static void main(String[] args) {
		int start = Integer.parseInt(args[0]);
		int end = Integer.parseInt(args[1]);
		start = 200000;// 148459
		end = 290000;
		// start=133941;
		// end=133942+4;
		SummaryChunkBuilder builder = new SummaryChunkBuilder(start, end);
		try {
			builder.work();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
