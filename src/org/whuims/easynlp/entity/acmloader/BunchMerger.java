package org.whuims.easynlp.entity.acmloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.SerializationUtils;
import org.whuims.easynlp.entity.commonentity.PaperBunch;

public class BunchMerger {
    public static void main(String[] args) {
        PaperBunch bunch1 = null;
        PaperBunch bunch2 = null;
        //        PaperBunch bunch3 = null;
        try {
            bunch1 = SerializationUtils.deserialize(new FileInputStream(
                    "h://Data/ACM/acm.ser1"));
            System.out.println("1");
            bunch2 = SerializationUtils.deserialize(new FileInputStream(
                    "h://Data/ACM/acm.ser23"));
            System.out.println("2");
            //            bunch3 = SerializationUtils.deserialize(new FileInputStream(
            //                    "h://Data/ACM/acm.ser2"));
            //            System.out.println("3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bunch1.getPapers().addAll(bunch2.getPapers());
        bunch2 = null;
        //        bunch1.getPapers().addAll(bunch3.getPapers());
        //        bunch3 = null;
        try {
            SerializationUtils.serialize(bunch1, new FileOutputStream(
                    "h://Data/ACM/acm.ser"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
