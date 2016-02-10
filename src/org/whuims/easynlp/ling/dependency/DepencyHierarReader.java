package org.whuims.easynlp.ling.dependency;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

public class DepencyHierarReader {
	Connection conn = null;
	Statement stmt = null;

	private void read() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		conn = Mysql.getConn("semsearch");
		try {
			stmt = conn.createStatement();
			java.sql.ResultSet rs = stmt
					.executeQuery("select * from penn_dependence");
			while (rs.next()) {
				map.put(rs.getString("label"), rs.getInt("id"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File(
					"resource/data/exverb/dep_hierar.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Stack<DepBi> stack = new Stack<DepBi>();
		for (String line : lines) {
			int index = line.lastIndexOf("\t") + 1;
			DepBi item = new DepBi(line.trim(), index);
			if (stack.isEmpty()) {
				stack.push(item);
			} else {
				if (item.getOffset() > stack.peek().getOffset()) {
					System.out.println("update penn_dependence set parent ='"
							+ map.get(stack.peek().getDep())
							+ "' where label='" + line.trim() + "';");
					stack.push(item);
				} else {
					while (!stack.isEmpty()
							&& stack.peek().getOffset() >= item.getOffset()) {
						stack.pop();
					}
					if (!stack.isEmpty())
						System.out
								.println("update penn_dependence set parent ='"
										+ map.get(stack.peek().getDep())
										+ "' where label='" + line.trim() + "';");
					stack.push(item);
				}

			}

		}
	}

	public static void main(String[] args) {
		DepencyHierarReader reader = new DepencyHierarReader();
		reader.read();
	}

	class DepBi {
		private String dep;
		private int offset;

		public DepBi(String dep, int offset) {
			super();
			this.dep = dep;
			this.offset = offset;
		}

		public String getDep() {
			return dep;
		}

		public int getOffset() {
			return offset;
		}

	}
}
