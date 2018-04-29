package edu.bit.cs.coverity;

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;
import org.sonar.plugins.findbugs.FindbugsSensor;

public class CoverityReportedBugFromJson implements ReportedBugInfo {

    private String line;
    private String name;
    private String error;

    public void setLine(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public BUG_TYPE getBugType() {
        return BUG_TYPE.NULL_POINTER_EXEPTION;
    }

    @Override
    public String getBugMessage() {
        return "";
    }

    @Override
    public String getClassName() {
        return name;
    }

    @Override
    public int getBugLineNumber() {
        return Integer.parseInt(line);
    }

    @Override
    public String getSourcePath() {
        return ReportedBugInfo.normalizeFilePath(name, FindbugsSensor.ROOT);
    }

    @Override
    public ToolCollection getToolName() {
        return ToolCollection.COVERITY;
    }
}
