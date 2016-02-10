package org.whuims.acm;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.parserwrapper.OpenNLPChunker;
import org.whuims.easynlp.phrase.PhraseDictionary;
import org.whuims.easynlp.phrase.PhraseTokenizer;

public class TestCorpusBuilder {
    Connection conn = null;
    Statement stmt = null;

    public void work() throws SQLException, IOException {
        //
        PhraseDictionary dict = new PhraseDictionary();
        List<String> aclTermLines;
        try {
            aclTermLines = FileUtils.readLines(new File("resource/data/acl/acl_phrase.txt"));
            for (String term : aclTermLines) {
                term = term.toLowerCase();
                dict.addPhrase(term.split("\\s+"), "NNP", 1);
            }
            aclTermLines = FileUtils.readLines(new File("resource/data/acl/common_phrase.txt"));
            for (String term : aclTermLines) {
                term = term.toLowerCase();
                dict.addPhrase(term.split("\\s+"), "NNP", 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        PhraseTokenizer tokenizer = new PhraseTokenizer(dict);

        conn = Mysql.getConn("semsearch");
        stmt = conn.createStatement();
        String sql = "select * from acl_paper";
        ResultSet rs = stmt.executeQuery(sql);
        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            long id = rs.getLong("id");
            String aclID = rs.getString("aclid");
            String title = rs.getString("title");
            Sentence sent = PaperUtils.sentProductOf(title);
            sent = tokenizer.tokenize(sent);
            System.out.println("====================================================");
            System.out.println(sent.getText());
            title = processChunk(sent);
            System.out.println(title);
            sb.append(id).append("\t");
            sb.append(aclID).append("\t");
            sb.append(title).append("\n");
        }
        FileUtils.write(new File("h:\\aclwhole\\acltitle_chunk.txt"), sb.toString());
    }

    private String processChunk(Sentence sent) {
        StringBuilder sb = new StringBuilder();
        String tokens[] = sent.getTokenArray();
        String postags[] = sent.getPostagArray();
        String[] chunks = OpenNLPChunker.createInstance().chunkParse(tokens, postags);
        sb.append("[ ").append(tokens[0]+"/"+postags[0]).append(" ");
        for (int i = 1; i < tokens.length; i++) {
            String token = tokens[i]+"/"+postags[i];
            String chunk = chunks[i];
            if (chunk.startsWith("B-") || chunk.equals("O")) {
                String oldChunk = chunks[i - 1];
                if (!oldChunk.equals("O")) {
                    oldChunk = oldChunk.split("-")[1];
                }
                sb.append("]_").append(oldChunk).append(" ").append(" [ ");
                sb.append(token).append(" ");
            } else {
                sb.append(token).append(" ");
            }
        }
        sb.append("]_");
        if (!chunks[chunks.length - 1].equals("O")) {
            sb.append(chunks[chunks.length - 1].split("-")[1]);
        } else {
            sb.append("O");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        TestCorpusBuilder builder = new TestCorpusBuilder();
        try {
            builder.work();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
