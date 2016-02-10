package org.whuims.acm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class TitleChunkBuilder {
	Map<Long, String> chunkMap = new TreeMap<Long, String>();
	private int start;
	private int end;

	public TitleChunkBuilder(int start, int end) {
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
		ResultSet rs = stmt.executeQuery("SELECT * FROM acm_paper WHERE titlechunk is null and title IS NOT NULL "
				+ " and id<=" + this.end + " and id>" + this.start);
		int size = 0;
		while (rs.next()) {
			long id = rs.getLong("id");
			String title = rs.getString("title");
			System.out.println(id);
			try {
				String summaryChunk = process(title);
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

	private String process(String title) {
		StringBuilder sb = new StringBuilder();
		if (title == null)
			title = "";
		Sentence sent = PaperUtils.sentProductOf(title.trim());
		sent = PhraseTokenizer.createInstance().tokenize(sent);
		List<CuperSpan> temp = StanfordShallowParser.createInstance().chunkToList(sent.getTokenArray(),
				sent.getPostagArray());
		int wordCount = 0;
		for (CuperSpan span : temp) {
			String str = span.getText().replace("~", " ") + ":" + span.getType() + "_-1_" + wordCount;
			wordCount++;
			sb.append(str).append("\t");
		}
		return sb.toString().trim();
	}

	private void save() throws SQLException {
		Connection conn = Mysql.getConn("semsearch");
		java.sql.PreparedStatement pstmt = conn.prepareStatement("update acm_paper set titlechunk = ? where id= ?");
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
//		int start = Integer.parseInt(args[0]);
//		int end = Integer.parseInt(args[1]);
		int start = 150000;
		int end = 150003;
		TitleChunkBuilder builder = new TitleChunkBuilder(start, end);
		try {
			builder.work();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
