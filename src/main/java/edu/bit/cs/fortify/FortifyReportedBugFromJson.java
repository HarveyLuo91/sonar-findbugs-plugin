package edu.bit.cs.fortify;

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.Constant;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;
import org.sonar.plugins.findbugs.FindbugsSensor;

public class FortifyReportedBugFromJson implements ReportedBugInfo {

    private String path;
    private String number;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
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
        return path;
    }

    @Override
    public int getBugLineNumber() {
        return Integer.parseInt(number);
    }

    @Override
    public String getSourcePath() {
        return ReportedBugInfo.normalizeFilePath(path, Constant.ROOT);
    }

    @Override
    public ToolCollection getToolName() {
        return ToolCollection.FORTIFY;
    }
}
