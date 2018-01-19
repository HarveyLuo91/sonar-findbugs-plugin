package edu.bit.cs;

import edu.bit.cs.util.ToolCollection;

public interface ReportedBugInfo {
    BUG_TYPE getBugType();
   // String getType();

    String getBugMessage();

    String getClassName();

    int getBugLineNumber();

    String getSourcePath();

    ToolCollection getToolName();
}
