package org.whuims.easynlp.entity.aclloader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;

/**
 * 一些数据的信息。
 * 
 * @author Qikai
 *
 */
public class PaperTermCal {
    private String filePath = "resource/data/acl/_all_annotation_map_to_acl_arc_id";
    private Multimap<String, String> paperTermsMap = ArrayListMultimap.create();
    private Multimap<String, String> termPapersMap = ArrayListMultimap.create();
    private Set<String> paperIDSet = new HashSet<String>();
    private Set<String> termIDSet = new HashSet<String>();

    public PaperTermCal(String filePath) {
        super();
        this.filePath = filePath;
    }

    public PaperTermCal() {
        super();
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void load() throws IOException {
        List<String> lines = FileUtils.readLines(new File(this.filePath));
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            String[] array = line.split("\t");
            String paperID = array[0].trim();
            String termID = array[1].trim();
            this.paperTermsMap.put(paperID, termID);
            this.termPapersMap.put(termID, paperID);
            this.paperIDSet.add(paperID);
            this.termIDSet.add(termID);
        }

    }

    /**
     * 返回词汇数量。
     * 
     * @return
     */
    public int getTermSize() {
        return this.termIDSet.size();
    }

    /**
     * 返回数据集中已标注的文档数量。
     * 
     * @return
     */
    public int getPaperSize() {
        return this.paperIDSet.size();
    }

    public Collection<String> getTerms(String paperID) {
        return this.paperTermsMap.get(paperID);
    }

    public static void main(String[] args) {
        String filePath = "resource/data/acl/_all_annotation_map_to_acl_arc_id";
        PaperTermCal rp = new PaperTermCal(filePath);
        TecTermCal tt=new TecTermCal();
        try {
            tt.load();
            rp.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("文档数量：\t" + rp.getPaperSize());
        System.out.println("词汇数量：\t" + rp.getTermSize());
        for (String str : rp.getTerms("E06-1009")) {
            System.out.println(tt.getTermString(str)+"\t"+tt.getTermCat(str));
        }
        StringBuilder sb=new StringBuilder();
        for(String str:tt.getTermList()){
            sb.append(str).append("\r\n");
        }
        try {
            FileUtils.write(new File("h:/acl_term.txt"), sb.toString().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
