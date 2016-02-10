package org.whuims.lex;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class LexReader {
	private String filePath;

	public LexReader(String filePath) {
		super();
		this.filePath = filePath;
	}

	public void read() throws IOException {
		List<String> lines = FileUtils.readLines(new File(this.filePath));
		String wholeStr = FileUtils.readFileToString(new File(this.filePath));
		wholeStr = cleanBracet(wholeStr);
		System.out.println(wholeStr);
		for (int i = 0; i < lines.size(); i++) {

		}
	}

	private String cleanBracet(String wholeStr) {
		wholeStr = wholeStr.replace("\r\n", "\n");
		char[] chars = wholeStr.toCharArray();
		int inner = 0;
		StringBuilder sb = new StringBuilder();
		for (char ch : chars) {
			if ((ch == '\n') && inner > 0) {
				sb.append(' ');
				continue;
			}
			sb.append(ch);
			if (ch == '{') {
				inner += 1;
			} else if (ch == '}') {
				inner -= 1;
				if (inner == 0) {
					sb.append("\n");
				}
			}

		}
		return sb.toString();
	}

	private void processLine(String line) {
		if (line.startsWith("#")) {
			return;
		}

	}

	public static void main(String[] args) {
		String filePath = "resource/data/scicfg/scirules.in";
		LexReader reader = new LexReader(filePath);
		try {
			reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
