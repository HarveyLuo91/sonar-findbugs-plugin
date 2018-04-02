package edu.bit.cs.jlint;


import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

public class JlintReportParser implements ReportedInfoProcessor {

    public static Collection<? extends ReportedBugInfo> getReportedBugs() {
        ArrayList ReportedBugs = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(JlintReportParser.class.getClassLoader().getResourceAsStream("file/jlint.txt")));
            String fileRead = br.readLine();
            while (fileRead != null) {
                if (fileRead.contains("Verification completed")) {
                    fileRead = br.readLine();
                    continue;
                }
                int index;
                if (fileRead.charAt(1) == ':') {
                    index = fileRead.indexOf(":", fileRead.indexOf(":") + 1);
                } else {
                    index = fileRead.indexOf(":");
                }
                if (index > -1) {
                    String classPath = fileRead.substring(0, index);
                    int startIndex_bugLineNumber = index + 1;
                    int endIndex_bugLineNumber = fileRead.indexOf(':', startIndex_bugLineNumber);
                    String bugLineNumber = fileRead.substring(startIndex_bugLineNumber, endIndex_bugLineNumber);
                    String errorMessage = fileRead.substring(endIndex_bugLineNumber + 1, fileRead.length()).trim();
                    String className = getClassName(classPath);
                    String sourcePath = classPath;
                    JlintReportedBug bugInstance = new JlintReportedBug(errorMessage, className, Integer.valueOf(bugLineNumber), sourcePath);
                    ReportedBugs.add(bugInstance);
                }
                fileRead = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReportedBugs;
    }

    @Override
    public Collection<? extends ReportedBugInfo> getReportedBugs(BufferedReader br) {
        ArrayList ReportedBugs = new ArrayList();
        try {
            String fileRead = br.readLine();
            String errorMessage;
            String className;
            String sourcePath;
            while (fileRead != null) {

                if (fileRead.contains("Verification completed")) {
                    fileRead = br.readLine();
                    continue;
                }
                int index;
                if (fileRead.charAt(1) == ':') {
                    index = fileRead.indexOf(":", fileRead.indexOf(":") + 1);
                } else {
                    index = fileRead.indexOf(":");
                }
                if (index > -1) {
                    String classPath = fileRead.substring(0, index);
                    int startIndex_bugLineNumber = index + 1;
                    int endIndex_bugLineNumber = fileRead.indexOf(':', startIndex_bugLineNumber);
                    String bugLineNumber = fileRead.substring(startIndex_bugLineNumber, endIndex_bugLineNumber);

                    errorMessage = fileRead.substring(endIndex_bugLineNumber + 1, fileRead.length()).trim();
                    className = getClassName(classPath);
                    sourcePath = classPath;
                    JlintReportedBug bugInstance = new JlintReportedBug(errorMessage, className, Integer.valueOf(bugLineNumber), sourcePath);
                    ReportedBugs.add(bugInstance);
                }
                fileRead = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReportedBugs;
    }

    private static String getClassName(String classPath) {
        // the first \ is an escape sequence character for the char to look for is '\'
        int start_index = classPath.lastIndexOf('/');
        String className = classPath.substring(start_index + 1, classPath.length()).trim();

        return className;

    }

}

