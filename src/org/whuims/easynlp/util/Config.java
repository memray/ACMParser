package org.whuims.easynlp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Config {
	private static Properties prop = null;
	static {
		try {
			prop = new Properties();
			prop.load(new InputStreamReader(new FileInputStream(
					"resource/config/config.properties"), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProp(String key) {
		return prop.getProperty(key);
	}

}
