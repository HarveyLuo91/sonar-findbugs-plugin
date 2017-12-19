package edu.bit.cs;

public interface ReportedBugInfo {
    String getBugType();
    String getBugMessage();
    String getClassName();
    int getBugLineNumber();
    String getSourcePath();
    String getToolName();
}
