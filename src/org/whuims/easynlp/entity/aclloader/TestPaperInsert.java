package org.whuims.easynlp.entity.aclloader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

public class TestPaperInsert {
    Connection conn = null;
    long count = 0;

    public TestPaperInsert() {
        super();
        conn = Mysql.getConn("semsearch");
    }

    public void walk(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                walk(file);
            } else {
                process(file);
            }
        }

    }

    private void process(File file) {
        String fileName = file.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf("_"));
        this.count++;
        try {
            String text = FileUtils.readFileToString(file);
            PreparedStatement pstmt = conn
                    .prepareStatement("insert into acl_paper_content values(?,?,?,?)");
            pstmt.setLong(1, count);
            pstmt.setString(2, fileName);
            pstmt.setString(3, "");
            pstmt.setString(4, text);
            pstmt.execute();
            pstmt.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String filePath = "H:\\Data\\ACL\\ACLXml\\00\\A00-1005_cln.xml";
        filePath = "H:\\Data\\ACL\\ACLXml\\";
        File file = new File(filePath);
        TestPaperInsert ti = new TestPaperInsert();
        ti.walk(file);
    }

}
