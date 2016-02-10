package org.whuims.easynlp.phrase;

import org.apache.commons.io.FileUtils;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;

import java.io.*;
import java.util.List;

/**
 * Recognize phrases in the fulltext extracted from ACM PDF
 * Created by Memray on 11/18/2015.
 */
public class AcmPdfFulltextPhraseTagger {
    public static void parseDirectory(){
        //        String basepath = "H:\\Dropbox\\PhD@Pittsburgh\\1.Researh\\NSF\\20160125_analyzer_v1\\textbook_analyzer\\data\\acm_paper\\";
        String basepath = "H:\\Dropbox\\PhD@Pittsburgh\\1.Researh\\NSF\\20160125_analyzer_v1\\textbook_analyzer\\data\\";
        String inputpath = basepath + "";
        String outputpath = basepath + "";
        int count = 0;
        File inputDirectory = new File(inputpath);
        for (File inputTextFile : inputDirectory.listFiles()) {
            try {
                System.out.println(++count + "\t:\t"+ inputTextFile.getName());
                String text = FileUtils.readFileToString(inputTextFile, "utf8");
                Sentence sent = PaperUtils.sentProductOf(text);
//                System.out.println(sent.getPostaggedText());
                sent = PhraseTokenizer.createInstance().longestTokenize(sent);
                sent = PhraseRegexTokenizer.createInstance().tokenize(sent);
                // phrases are connected with underline, and wrapped with <term></term>
//                String output = sent.getPhraseWrappedText().toLowerCase().replaceAll("-lsb- ", "[").replaceAll(" -rsb-", "]").replaceAll("-lcb- ", "{").replaceAll(" -rcb-", "}").replaceAll("-lrb- ", "(").replaceAll(" -rrb-", ")").replaceAll(" -lab-", "<").replaceAll(" -rab-", ">");

                // phrases are connected with underline
                String output = sent.getText().toLowerCase().replaceAll("-lsb- ", "[").replaceAll(" -rsb-", "]").replaceAll("-lcb- ", "{").replaceAll(" -rcb-", "}").replaceAll("-lrb- ", "(").replaceAll(" -rrb-", ")").replaceAll(" -lab-", "<").replaceAll(" -rab-", ">");
                String outputFileName = outputpath + inputTextFile.getName();
//                System.out.println(outputFileName);
                FileUtils.writeStringToFile(new File(outputFileName), output, "utf8", false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void parseSingleFile(){
        String basepath = "H:\\Dropbox\\PhD@Pittsburgh\\1.Researh\\NSF\\20160125_analyzer_v1\\textbook_analyzer\\data\\";
        String inputFilePath = basepath + "corpus_textbook+sigir.txt";
        String outputFilepath = basepath + "corpus_textbook+sigir_term_phrase.txt";

        int count = 0;

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFilepath), false),"utf8"));
            List<String> lines = FileUtils.readLines(new File(inputFilePath), "utf8");
            String id,text;
            for(String line : lines){
                System.out.println(++count);
                id = line.substring(0, line.indexOf("\t"));
                text = line.substring(line.indexOf("\t")+1, line.length());
                System.out.println(text);
                Sentence sent = PaperUtils.sentProductOf(text);
    //                System.out.println(sent.getPostaggedText());
                sent = PhraseTokenizer.createInstance().longestTokenize(sent);
                sent = PhraseRegexTokenizer.createInstance().tokenize(sent);
                // phrases are connected with underline, and wrapped with <term></term>
    //                String output = sent.getPhraseWrappedText().toLowerCase().replaceAll("-lsb- ", "[").replaceAll(" -rsb-", "]").replaceAll("-lcb- ", "{").replaceAll(" -rcb-", "}").replaceAll("-lrb- ", "(").replaceAll(" -rrb-", ")").replaceAll(" -lab-", "<").replaceAll(" -rab-", ">");
                // phrases are connected with underline
                text = sent.getTextWithWordAndPhrase().toLowerCase().replaceAll("-lsb- ", "[").replaceAll(" -rsb-", "]").replaceAll("-lcb- ", "{").replaceAll(" -rcb-", "}").replaceAll("-lrb- ", "(").replaceAll(" -rrb-", ")").replaceAll(" -lab-", "<").replaceAll(" -rab-", ">");

                writer.write(id+'\t'+text+"\n");
                System.out.println(text);
                System.out.println("\n-------------------------------------------------------\n\n\n\n\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
//        parseDirectory();
        parseSingleFile();
    }

}
