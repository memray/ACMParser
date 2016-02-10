package org.whuims.tools.aanLoader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;

public class AanCitationLoader {
    String filePath = "";
    Connection conn = null;
    Statement stmt = null;

    public AanCitationLoader(String filePath) {
        super();
        this.filePath = filePath;
    }

    public void load() throws IOException, SQLException {
        this.conn = Mysql.getConn("semsearch");
        this.stmt=conn.createStatement();
        List<String> lines = FileUtils.readLines(new File(this.filePath));
        for(String line:lines){
            line=line.trim();
            String[] array=line.split("==>");
            String source=array[0].trim();
            String target=array[1].trim();
            String sql="insert into acl_citation (source,target) values (\""+source+"\",\""+target+"\")";
            stmt.execute(sql);
        }
        stmt.close();
        conn.close();
    }

    public static void main(String[] args) {
        String filePath = "H:\\Data\\ACL\\aanrelease2013\\aan\\release\\2012\\networks\\paper-citation-nonself-network.txt";
        AanCitationLoader loader = new AanCitationLoader(filePath);
        try {
            loader.load();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

}
