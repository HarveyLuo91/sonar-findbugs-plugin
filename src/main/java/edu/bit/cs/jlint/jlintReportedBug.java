package edu.bit.cs.jlint;


import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;

public class jlintReportedBug {



        //jlintReportParser reportedBugs = new jlintReportParser();

    public static void main(String[] args) {
        ArrayList list = jlintReportParser.parseReportBugsFile();
        System.out.println(list.size());
    }



}
