package org.whuims.crawler;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.whuims.acm.db.Mysql;

public class ACLAbstractCrawler {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	List<String> ids = new ArrayList<String>();

	public static void main(String[] args) {
		ACLAbstractCrawler crawler = new ACLAbstractCrawler();
		crawler.crawl();
	}

	public void crawl() {
		loadIDs();
		downloadPages();
	}

	private void downloadPages() {
		int count = 0;
		for (String id : this.ids) {
			if(count<=5000){
				download(id);
			}
			System.out.println(count + "\t" + id);
			count++;
		}
	}

	public void download(String id) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String urlString = "http://clair.eecs.umich.edu/aan/paper.php?paper_id="
				+ id;
		try {
			HttpGet httpget = new HttpGet(urlString);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			String responseBody = httpclient.execute(httpget, responseHandler);
			FileUtils.write(new File("d:/acl_metadata/"// + id.charAt(0) + "/"
					+ id + ".html"), responseBody.trim(), false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadIDs() {
		try {
			conn = Mysql.getConn("semsearch");
			stmt = conn.createStatement();
			String sql = "select acl_id from acl_paper";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				this.ids.add(rs.getString("acl_id"));
				System.out.println(rs.getString("acl_id"));
			}
			System.out.println(this.ids.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
