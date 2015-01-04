package com.pinguo.common.ciutils.fix;

import com.pinguo.common.ciutils.utils.AppendToFile;
import com.pinguo.common.ciutils.CheckStyleReport;
import org.dom4j.DocumentException;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 *
 * Created by tangsong on 12/15/14.
 */
public class CheckStyleFixer extends AbsCiFixer{

    public final static String[] MODIFIERS = {"public", "protected", "private", "abstract", "static", "final",
    "transient", "volatile", "synchronized", "native", "strictfp"};
    public final static Set<String> MODIFIERS_SET = new HashSet<String>();
    static {
        for (String s : MODIFIERS) {
            MODIFIERS_SET.add(s);
        }
    }


    @Override
    public void fix(String resultPath) {
        System.out.println("            fix all checkstyle warnings, start");
        try {
            final LinkedList<CheckStyleReport.Info> list = new CheckStyleReport().analyse(resultPath);
            for (CheckStyleReport.Info info : list) {
                List<CheckStyleReport.Issue> issueList = info.issueList;
                for (CheckStyleReport.Issue issue : issueList) {
                    if (issue.warnType != null) {
                        if(this.fix(info.file, issue.warnType, issue.line)) {
                            System.out.println("   fix : " + info.toString());
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("            fix all checkstyle warnings, done");
    }

    private boolean fix(String path, String type, int line) {
//        final File root
        //按方法A追加文件
//        AppendToFile.appendMethodA(fileName, content);
        if ("com.puppycrawl.tools.checkstyle.checks.NewlineAtEndOfFileCheck".equals(type)) {
            AppendToFile.appendMethodA(path, "\r\n");
            System.out.println(" auto : " + path + " , type = " + type);
            return true;
        } else if ("com.puppycrawl.tools.checkstyle.checks.modifier.ModifierOrderCheck".equals(type)) {
            fixModifierOrder(path, line);
            return true;
        }
        return false;

        //显示文件内容
//        ReadFromFile.readFileByLines(path);
//        //按方法B追加文件
//        AppendToFile.appendMethodB(fileName, content);
//        AppendToFile.appendMethodB(fileName, "append end. \n");
//        //显示文件内容
//        ReadFromFile.readFileByLines(fileName);
    }

    private void fixModifierOrder(String path, int line) {
        BufferedReader reader = null;
        FileWriter fw = null;
        try {
            final LinkedList<String> contentList = new LinkedList<String>();
            reader = readContent(path, contentList);

            replaceContent(line, contentList);

            fw = writeContent(path, contentList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }

            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e1) {
                }
            }

        }
    }

    private FileWriter writeContent(String path, LinkedList<String> contentList) throws IOException {
        FileWriter fw = new FileWriter(path);
        for (String s : contentList) {
            fw.write(s);
        }
        return fw;
    }

    private void replaceContent(int line, LinkedList<String> contentList) {
        int index = line - 1;
        String str = contentList.get(index);
        String[] wordArray = str.split(" ");

        HashSet<String> modifierSet = new HashSet<String>();
        StringBuilder oldModifier = new StringBuilder();
        for (String w : wordArray) {
            if (CheckStyleFixer.MODIFIERS_SET.contains(w)) {
                modifierSet.add(w);
                oldModifier.append(w).append(" ");
            }
        }

        StringBuilder newModifier = new StringBuilder();
        for (String modifier : CheckStyleFixer.MODIFIERS) {
            if (modifierSet.contains(modifier)) {
                newModifier.append(modifier).append(" ");
            }
        }

        str = str.replace(oldModifier.toString(), newModifier.toString());
        contentList.set(index, str);
    }

    private BufferedReader readContent(String path, LinkedList<String> contentList) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            // 显示行号
            contentList.add(tempString + "\r\n");
        }
        return reader;
    }
}
