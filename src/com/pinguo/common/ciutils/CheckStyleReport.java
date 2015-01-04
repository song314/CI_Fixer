package com.pinguo.common.ciutils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 * Created by tangsong on 12/10/14.
 */
public class CheckStyleReport {

    public static class Info {
        public String file;

        public LinkedList<Issue> issueList = new LinkedList<Issue>();

        @Override
        public String toString() {
            return "Info{" +
                    "file='" + file + '\'' +
                    ", issueList=" + issueList +
                    '}';
        }

    }

    public static class Issue {
        public String warnType;
        public int line = -1;

        @Override
        public String toString() {
            return "Issue{" +
                    "warnType='" + warnType + '\'' +
                    ", line=" + line +
                    '}';
        }
    }

    public static final String TEST = "/Users/tangsong/Dev/Android studioProjects/Android-Baby360/build/reports/checkstyle/checkstyle-result.xml";

    private LinkedList<CheckStyleReport.Info> mInfoList = new LinkedList<Info>();

    public LinkedList<Info> analyse(String reportPath) throws DocumentException, FileNotFoundException {
        System.out.println(" collect all checkstyle warnings");
        SAXReader reader = new SAXReader();
        InputStream in = new FileInputStream(reportPath);
        Document doc = reader.read(in);
        doc.accept(new AutoVisitor());
        System.out.println(" collect all checkstyle warnings, dones");
        return mInfoList;
    }


    public static void readNode(Element root, String prefix) {
        if (root == null) return;
        // 获取属性
        List<Attribute> attrs = root.attributes();
        if (attrs != null && attrs.size() > 0) {
            System.err.print(prefix);
            for (Attribute attr : attrs) {
                System.err.print(attr.getName() + " = " + attr.getValue() + ", ");
            }
            System.err.println();
        }
        // 获取他的子节点
        List<Element> childNodes = root.elements();
        prefix += "\t";
        for (Element e : childNodes) {
            readNode(e, prefix);
        }
    }

    class AutoVisitor extends VisitorSupport {

        @Override
        public void visit(Attribute node) {
            if (mInfoList.size() <= 0) {
                return;
            }

            final Info info = mInfoList.getLast();
            final String nodeName = node.getName();
            final String parent = node.getParent().getName();

            if ("file".equals(parent)) {
                if ("name".equals(nodeName)) {
                    info.file = node.getValue();
                }
            } else if ("error".equals(parent)) {
                final Issue issue = mInfoList.getLast().issueList.getLast();
                if ("source".equals(nodeName)) {
                    issue.warnType = node.getValue();
                } else if ("line".equals(nodeName)) {
                    issue.line = Integer.parseInt(node.getValue());
                }
            }
        }

        @Override
        public void visit(Element node) {
            if ("file".equals(node.getName())) {
                Info info = new Info();
                mInfoList.add(info);
            } else if ("error".equals(node.getName())) {
                final Issue issue = new Issue();
                mInfoList.getLast().issueList.add(issue);
            }


        }

        @Override
        public void visit(ProcessingInstruction node) {
            System.out.println("PI:" + node.getTarget() + " " + node.getText());
        }
    }
}
