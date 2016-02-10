package org.whuims.tools.aanLoader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.whuims.acm.db.Mysql;

public class AanLoader {
    Set<String> set=new HashSet<String>();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String filePath = "";

    public AanLoader(String filePath) {
        super();
        this.filePath = filePath;
    }

    public void load() throws IOException, SQLException {
        conn = Mysql.getConn("semsearch");
        List<String> lines = FileUtils.readLines(new File(this.filePath));
        AANPaper paper = new AANPaper();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.equals("")) {
                process(paper);
            }
            if (line.startsWith("id")) {
                paper = new AANPaper();
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setId(temp);
            } else if (line.startsWith("author")) {
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setAuthor(temp);
            } else if (line.startsWith("title")) {
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setTitle(temp);
            } else if (line.startsWith("venue")) {
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setVenue(temp);
            } else if (line.startsWith("year")) {
                String temp = StringUtils.substringAfter(line, "{").trim();
                temp = StringUtils.substringBeforeLast(temp, "}");
                paper.setYear(temp);
            }

            System.out.println(line);
        }
    }

    private void process(AANPaper paper) throws NumberFormatException,
            SQLException {
        String sqlString = "INSERT INTO `semsearch`.`acl_paper` (`id`, `title`, `author`, `venue`, `year`) VALUES (?, ?, ?, ?, ?);";
        pstmt = conn.prepareStatement(sqlString);
        if(set.contains(paper.getId())){
            return;
        }else{
            set.add(paper.getId());
        }
        pstmt.setString(1, paper.getId());
        pstmt.setString(2, paper.getTitle());
        pstmt.setString(3, paper.getAuthor());
        pstmt.setString(4, paper.getVenue());
        pstmt.setInt(5, Integer.parseInt(paper.getYear()));
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static void main(String[] args) {
        String filePath = "H:\\Data\\ACL\\aanrelease2013\\aan\\release\\2012\\acl-metadata.txt";
        AanLoader loader = new AanLoader(filePath);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
