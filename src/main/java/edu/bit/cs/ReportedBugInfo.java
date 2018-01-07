package edu.bit.cs;

import edu.bit.cs.util.ToolCollection;

public interface ReportedBugInfo {
    String getBugType();
    String getBugMessage();
    String getClassName();
    int getBugLineNumber();
    String getSourcePath();
    ToolCollection getToolName();
}
