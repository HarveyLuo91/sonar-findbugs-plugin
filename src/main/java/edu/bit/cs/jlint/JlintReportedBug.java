package edu.bit.cs.jlint;


import edu.bit.cs.ReportedBugInfo;


public class JlintReportedBug implements ReportedBugInfo{
    private final  String type;
    private final  String message;
    private final  String className;
    private final  int bugLineNumber;
    private  final String sourcePath;


    public JlintReportedBug(String type, String message, String className, int bugLineNumber, String sourcePath) {
        this.type = type;
        this.message = message;
        this.className = className;
        this.bugLineNumber = bugLineNumber;
        this.sourcePath = sourcePath;

    }


    @Override
    public String getBugType() {
        return type;
    }

    @Override
    public String getBugMessage() {
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
    public String getToolName() {
        return "[Jlint] ";
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Bug Type: " + getBugType() + "\n");
        string.append("Error Message: " + getBugMessage() + "\n");
        string.append("ClassName: " + getClassName() + "\n");
        string.append("Bug_LineNumber: " + getBugLineNumber() + "\n");
        string.append("Source Path: " + getSourcePath() + "\n");


        return string.toString();
    }
}