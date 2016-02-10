package org.whuims.easynlp.crfprocessor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Seeds {
    private static Set<String> applicationSeeds = new HashSet<String>();
    private static Set<String> methodSeeds = new HashSet<String>();
    public static StringBuilder sb = new StringBuilder();

    static {
        try {
            for (String str : FileUtils.readLines(new File(
                    "resource/data/crfprocessor_seeds/Application.seeds"))) {
                applicationSeeds.add(" " + str.trim().toLowerCase() + " ");
            }
            for (String str : FileUtils.readLines(new File(
                    "resource/data/crfprocessor_seeds/Method.seeds"))) {
                methodSeeds.add(" " + str.trim().toLowerCase() + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getApplicationSeeds() {
        return applicationSeeds;
    }

    public static Set<String> getMethodSeeds() {
        return methodSeeds;
    }

}
