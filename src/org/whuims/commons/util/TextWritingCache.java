package org.whuims.commons.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TextWritingCache {
	private static int cacheSize = 10000;
	private static TextWritingCache instance = new TextWritingCache();
	private String outputPath = "DEFAULT_OUTPUTPATH";
	private StringBuffer sb = new StringBuffer();

	private TextWritingCache() {
		super();
	}

	public static TextWritingCache getInstance() {
		return instance;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public int write(String text) throws IOException {
		this.sb.append(text);
		if (sb.length() > cacheSize) {
			flush();
		}
		return sb.length();
	}

	private void flush() throws IOException {
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(this.outputPath), true)));
		writer.write(this.sb.toString());
		writer.close();
		this.sb=new StringBuffer();
	}

}
