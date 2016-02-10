package org.whuims.acm.db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.whuims.easynlp.util.Config;

public class Mysql {
	public static final String HOST = Config.getProp("mysql_host");
	public static final String PORT = Config.getProp("mysql_port");
	public static final String DRIVER = "org.gjt.mm.mysql.Driver";
	public static final String USERNAME = Config.getProp("username");
	public static final String PASSWORD = Config.getProp("passw");

	public static Connection getConn(String databaseName) {
		try {
			Class.forName(DRIVER).newInstance();
			return DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + databaseName + "?user="
					+ USERNAME + "&password=" + PASSWORD + "&useUnicode=true&characterEncoding=utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Connection getConn(String host, String databaseName, String userName, String passWord) {
		try {
			Class.forName(DRIVER).newInstance();
			return DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + databaseName + "?user=" + userName
					+ "&password=" + passWord + "&useUnicode=true&characterEncoding=utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
