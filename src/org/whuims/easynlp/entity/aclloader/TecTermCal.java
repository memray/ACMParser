package org.whuims.easynlp.entity.aclloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class TecTermCal {
    private String filePath = "resource/data/acl/_all_annotated_candid_term";
    private BiMap<String, String> idTermMap = HashBiMap.create();
    private Map<String, Integer> idCatMap = new HashMap<String, Integer>();
    private List<String> termList = new ArrayList<String>();

    public TecTermCal(String filePath) {
        super();
        this.filePath = filePath;
    }

    public TecTermCal() {
        super();
    }

    public void load() throws IOException {
        List<String> lines = FileUtils.readLines(new File(this.filePath));
        for (String line : lines) {
            String[] array = line.split("\t");
            if (array.length != 3) {
                continue;
            }
            String termID = array[0].trim();
            String termString = array[1].trim();
            int cat = Integer.parseInt(array[2].trim());
            this.idTermMap.put(termID, termString);
            this.idCatMap.put(termID, cat);
            if (cat > 0) {
                this.termList.add(termString);
            }
        }
    }

    public String getTermString(String tecID) {
        return this.idTermMap.get(tecID);
    }

    public int getTermCat(String tecID) {
        return this.idCatMap.get(tecID);
    }

    public static void main(String[] args) {
        TecTermCal tt = new TecTermCal();
        try {
            tt.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(tt.getTermString("453"));
    }

    public List<String> getTermList() {
        return termList;
    }

    public void setTermList(List<String> termList) {
        this.termList = termList;
    }
}
