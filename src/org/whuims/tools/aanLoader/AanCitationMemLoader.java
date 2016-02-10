package org.whuims.tools.aanLoader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class AanCitationMemLoader {
    String filePath = "";
    public static Map<String, Set<String>> map = new HashMap<String, Set<String>>();

    public AanCitationMemLoader(String filePath) {
        super();
        this.filePath = filePath;
        try {
            load();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getCitations(String label) {
        return map.get(label);
    }

    private void load() throws IOException, SQLException {
        List<String> lines = FileUtils.readLines(new File(this.filePath));
        for (String line : lines) {
            line = line.trim();
            String[] array = line.split("==>");
            String source = array[0].trim();
            String target = array[1].trim();
            if (!map.containsKey(source)) {
                map.put(source, new HashSet<String>());
            }
            map.get(source).add(target);
        }
        System.out.println("加载完成");
    }

    public static void main(String[] args) {
        String filePath = "H:\\Data\\ACL\\aanrelease2013\\aan\\release\\2012\\networks\\paper-citation-nonself-network.txt";
        AanCitationMemLoader loader = new AanCitationMemLoader(filePath);

    }

}
