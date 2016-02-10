package org.whuims.easynlp.dict.seedom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// TODO: Auto-generated Javadoc
/**
 * The Class HtmlParser.
 */
public class HtmlParser {

    /** The file path. */
    String filePath;

    /** The dict. */
    SeedomDict dict = null;

    /**
     * Instantiates a new html parser.
     *
     * @param filePath the file path
     * @param dict the dict
     */
    public HtmlParser(String filePath, SeedomDict dict) {
        super();
        this.filePath = filePath;
        this.dict = dict;
    }

    /**
     * Parses the.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void parse() throws IOException {
        String html = FileUtils.readFileToString(new File(this.filePath));
        Document doc = Jsoup.parse(html);
        String path = "div[class$=block-content]";
        Elements section = doc.select(path);
        Elements body = section
                .select("div[class$=field field-name-field-body field-type-text-long field-label-hidden]");
        Elements related = section
                .select("div[class$=field field-name-field-related field-type-node-reference field-label-inline clearfix]");
        Elements headers = section.select("header[class$=article-header]");
        Elements louw = section
                .select("div[class$=field field-name-field-louw-nida field-type-text field-label-inline clearfix]");
        String header = headers.text();
        String desc = body.text();
        String relatedDomain = related.text();
        String louwCode = louw.text();
        //        System.out.println(header);
        //        System.out.println(desc);
        //        System.out.println(relatedDomain);
        //        System.out.println(louwCode);
        String domainID = new File(this.filePath).getName()
                .replace(".htm*", "");
        Domain domain = new Domain(domainID, header, desc, relatedDomain,
                louwCode);

        Elements box = section
                .select("div[class$=field field-name-field-questions field-type-two-text-field field-label-hidden]");
        Elements items = box.select("div[class$=field-item odd]");
        Map<String, String[]> map = new HashMap<String, String[]>();
        for (Element item : items) {
            Elements key = item.select("div[class$=two_field_0]");
            Elements value = item.select("div[class$=two_field_1]");
            String type = key.text();
            String[] words = value.text().split(", ");
            map.put(type, words);
        }
        items = box.select("div[class$=field-item even]");
        for (Element item : items) {
            Elements key = item.select("div[class$=two_field_0]");
            Elements value = item.select("div[class$=two_field_1]");
            String type = key.text();
            String[] words = value.text().split(", ");
            map.put(type, words);
        }
        for (Entry<String, String[]> entry : map.entrySet()) {
            //            System.out.println();
            //            System.out.println(count + "\t" + entry.getKey());
            StringBuilder sb = new StringBuilder();
            for (String str : entry.getValue()) {
                str = str.replace(",", "");
                sb.append(str).append(", ");
            }
            Type type = new Type();
            type.setDomain(domain);
            type.setTypeDesc(entry.getKey());
            List<String> list = new ArrayList<String>();
            for (String word : entry.getValue()) {
                list.add(word);
            }
            type.setWords(list);
            domain.getTypes().add(type);
        }
        dict.addDomain(domain);
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        String filePath = "H:\\Data\\semdom\\www.semdom.org\\v4\\";
        File dir = new File(filePath);
        SeedomDict dict = new SeedomDict();
        for (File file : dir.listFiles()) {

            HtmlParser parser = new HtmlParser(file.getAbsolutePath(), dict);
            try {
                parser.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file = new File("resource/dict/seedomDict.ser");

        ObjectOutputStream oout;
        try {
            oout = new ObjectOutputStream(new FileOutputStream(file));
            oout.writeObject(dict);
            oout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
