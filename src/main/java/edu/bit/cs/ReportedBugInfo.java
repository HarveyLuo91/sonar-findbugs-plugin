package edu.bit.cs;

import com.google.common.base.Preconditions;
import edu.bit.cs.util.ToolCollection;

public interface ReportedBugInfo {

    default String getUID() {
        return this.getSourcePath() + "-" + this.getBugLineNumber() + "-" + this.getBugType();
    }

    default boolean equals(ReportedBugInfo bugInfo) {
        Preconditions.checkNotNull(bugInfo);

        if (this.getUID().equals(bugInfo.getUID())) {
            return true;
        } else {
            return false;
        }
    }

    static String normalizeFilePath(String filePath, String root) {
        String localRoot = root;
        if (filePath.contains("main/java")) {
            localRoot = "main/java";
        }
        int start_index = filePath.lastIndexOf(localRoot);
        if (start_index == -1) {
            return filePath.replace("\\", "/");
        }
        return filePath.substring(start_index + localRoot.length() + 1).replace("\\", "/");
    }

    BUG_TYPE getBugType();
    // String getType();

    String getBugMessage();

    String getClassName();

    int getBugLineNumber();

    String getSourcePath();

    ToolCollection getToolName();

}
