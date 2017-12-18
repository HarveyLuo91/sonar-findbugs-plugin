package edu.bit.cs.jlint;


import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;

public class jlintReportedBug{
    private final  String type;
    private final  String message;
    private final  String className;
    private final  int bugLineNumber;
    private  final String sourcePath;


    public jlintReportedBug(String type, String message, String className, int bugLineNumber, String sourcePath) {
        this.type = type;
        this.message = message;
        this.className = className;
        this.bugLineNumber = bugLineNumber;
        this.sourcePath = sourcePath;

    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getClassName() {
        return className;
    }

    public int getBugLineNumber() {
        return bugLineNumber;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Bug Type: " + getType() + "\n");
        string.append("Error Message: " + getMessage() + "\n");
        string.append("ClassName: " + getClassName() + "\n");
        string.append("Bug_LineNumber: " + getBugLineNumber() + "\n");
        string.append("Source Path: " + getSourcePath() + "\n");


        return string.toString();
    }
}