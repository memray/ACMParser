package org.whuims.acm;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

public class SimpleTest {
    public static void main(String[] args) {
        File file = new File(
                "H:\\Data\\ACL\\_all_annotation_map_to_acl_arc_id\\_all_annotation_map_to_acl_arc_id");
        int count = 0;
        try {
            Connection conn = Mysql.getConn("semsearch");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from acl_paper");
            Map<String, Long> paperMap = new HashMap<String, Long>();
            while (rs.next()) {
                String aclID = rs.getString("aclid");
                long id = rs.getLong("id");
                paperMap.put(aclID, id);
            }
            rs.close();
            ResultSet rs1 = stmt.executeQuery("select * from sem_term");
            Map<String, Long> termMap = new HashMap<String, Long>();
            while (rs1.next()) {
                String aclID = rs1.getLong("tecid") + "";
                long id = rs1.getLong("id");
                termMap.put(aclID, id);
            }
            rs1.close();

            for (String line : FileUtils.readLines(file)) {
                count++;
                if (count == 1)
                    continue;
                String[] array = line.split("\t");
                String aclID = array[0];
                String tecID = array[1];
                Long paperID = paperMap.get(aclID);
                Long termID = termMap.get(tecID);
                if (paperID == null || termID == null) {
                    System.out.println(aclID + "\t" + tecID);
                }
                String sql = "insert into acl_paper_terms (paperid,termid) values ("
                        + paperID + "," + termID + ")";
                // System.out.println(sql);
//                stmt.execute(sql);
            }
            stmt.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
