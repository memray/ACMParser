package org.whuims.commons.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LineWritingCache {
	private static int cacheSize = 100;
	private static LineWritingCache instance = new LineWritingCache();
	private String outputPath = "DEFAULT_OUTPUTPATH";
	private StringBuffer sb = new StringBuffer();
	private int lineSize=1;

	private LineWritingCache() {
		super();
	}

	public static LineWritingCache getInstance() {
		return instance;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public int write(String text) throws IOException {
		this.sb.append(text);
		lineSize++;
		if (lineSize > cacheSize) {
			flush();
		}
		return lineSize;
	}

	public void flush() throws IOException {
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(this.outputPath), true)));
		writer.write(this.sb.toString());
		writer.close();
		this.sb=new StringBuffer();
		this.lineSize=1;
	}

}
