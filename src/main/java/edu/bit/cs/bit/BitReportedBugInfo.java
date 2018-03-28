package edu.bit.cs.bit;

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;

public class BitReportedBugInfo implements ReportedBugInfo {
    private String bug_type;
    private String line_number;
    private String path;

    public BitReportedBugInfo(String bug_type, String line_number, String path) {
        this.bug_type = bug_type;
        this.line_number = line_number;
        this.path = path;
    }

    @Override
    public BUG_TYPE getBugType() {
        if(bug_type.equals("RL")){
            return BUG_TYPE.RESOURCE_LEAK;
        }else if(bug_type.equals("XSS")){
            return BUG_TYPE.CROSS_SITE_SCRIPTING;
        }else if(bug_type.equals("SQL_Injections")){
            return BUG_TYPE.INJECTION;
        }
        return BUG_TYPE.ANOTHER_TYPE;
    }

    @Override
    public String getBugMessage() {
        return "No message is reported";
    }

    @Override
    public String getClassName() {
        int s_index = path.lastIndexOf('/') + 1;
        String className = path.substring(s_index,path.length());
        return className;
    }

    @Override
    public int getBugLineNumber() {
        return Integer.parseInt(line_number);
    }

    @Override
    public String getSourcePath() {
        return path;
    }

    @Override
    public ToolCollection getToolName() {
        return ToolCollection.BIT;
    }
}
