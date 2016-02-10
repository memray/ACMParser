package org.whuims.easynlp.entity.acmloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.SerializationUtils;
import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.commonentity.PaperBunch;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Section;
import org.whuims.easynlp.entity.commonentity.SimplePaper;

public class AcmPaperBunchLoader {
    public PaperBunch load() {
        PaperBunch bunch = new PaperBunch("ACM", 250000);
        try {
            loadBunch(bunch);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bunch;
    }

    public PaperBunch loadFromSer(String filePath) {
        PaperBunch bunch = null;
        try {
            bunch = SerializationUtils
                    .deserialize(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bunch;
    }

    public PaperBunch loadFromSerGZip(String filePath) {
        PaperBunch bunch = null;
        try {
            bunch = SerializationUtils.deserialize(new GZIPInputStream(
                    new FileInputStream(filePath)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bunch;
    }

    private void loadBunch(PaperBunch bunch) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        conn = Mysql.getConn("acm");
        stmt = conn.createStatement();
        ResultSet rs = stmt
                .executeQuery("select article_id,Acm_Article_Title, Acm_Article_Abstract from acm_article limit 200000,100000");
        int count = 0;
        while (rs.next()) {
            count++;
            if (count % 1000 == 0) {
                System.out.println(count);
            }
            int id = rs.getInt("article_id");
            String title = rs.getString("Acm_Article_Title");
            String theAbstract = rs.getString("Acm_Article_Abstract");
            SimplePaper paper = new SimplePaper();
            Section abSection = new Section();
            abSection.setTitle(null);
            abSection.getParagraphs()
                    .add(PaperUtils.paraProductOf(theAbstract));
            paper.setTitleRawText(title);
            paper.setTitle(PaperUtils.sentProductOf(title));
            paper.setAbstract(abSection);
            paper.setAbstractRawText(theAbstract);
            paper.setId(id);
            bunch.addPaper(paper);
        }
    }

    public static void main(String[] args) {
        AcmPaperBunchLoader loader = new AcmPaperBunchLoader();
        //        PaperBunch bunch = loader.loadFromSerGZip("H:/Data/Acm/acm.serial.gz");
        PaperBunch bunch = loader.load();
        System.out.println(bunch.getBunchName());
        System.out.println(bunch.size());
        SimplePaper paper = (SimplePaper) bunch.getPapers().get(0);
        System.out.println();
        System.out.println(paper.getAbstractRawText());
        System.out.println("");
        System.out.println(paper.getAbstractText());
        System.out.println();
        System.out.println(paper.getTitleRawText());
        System.out.println();
        System.out.println(paper.getTitle().getText());
        System.out.println(paper.getTitle().getPostaggedText());
        try {
            SerializationUtils.serialize(bunch, new FileOutputStream(
                    "h://Data/ACM/acm.ser3"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
