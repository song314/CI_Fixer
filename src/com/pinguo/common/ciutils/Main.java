package com.pinguo.common.ciutils;

import com.pinguo.common.ciutils.fix.CheckStyleFixer;
import org.dom4j.DocumentException;

import java.io.*;
import java.util.LinkedList;

public class Main {

    /** Stop instances being created. */
    private Main()
    {
    }


    public static void main(String[] args) {

//        JenkinsAnalyser jenkinsAnalyser = new JenkinsAnalyser();

        if (args != null && args.length > 0) {
//            jenkinsAnalyser.setHomePath(args[0]);
        } else {
//            jenkinsAnalyser.setHomePath(new File("").getCanonicalPath());
//            String path = new File(jenkinsAnalyser.getClass().getResource("").getPath()).getAbsolutePath().split("!")[0].split("file:")[1];
//            jenkinsAnalyser.setHomePath(new File(path).getParent());
//            jenkinsAnalyser.setHomePath("/Users/tangsong/Dev/open_source/CI_Reporter/test");
        }


        CheckStyleFixer fixer = new CheckStyleFixer();
        if (args != null && args.length > 0) {
            fixer.fix(args[0]);
        } else {
            fixer.fix("/Users/tangsong/Downloads/IDEA_Puligns/checkstyle-result.xml");
        }
        System.err.println("finish.....");

    }

}
