package edu.bit.cs.Fortify;

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;

public class FortifyReportedBugFromCsv implements ReportedBugInfo {
    public String bug_type;
    public String path;
    public String line_no;

    @Override
    public BUG_TYPE getBugType() {
        if(bug_type.equals("Null Dereference")){
            return BUG_TYPE.NULL_POINTER_EXEPTION;
        }else if(bug_type.equals("Missing Check against Null")){
            return BUG_TYPE.NULL_POINTER_EXEPTION;
        }else if(bug_type.equals("SQL Injection")){
            return BUG_TYPE.INJECTION;
        }else if(bug_type.equals("Unreleased Resource: Streams")){
            return BUG_TYPE.RESOURCE_LEAK;
        }else if(bug_type.equals("Cross-Site Scripting")){
            return BUG_TYPE.CROSS_SITE_SCRIPTING;
        }else if(bug_type.equals("Race Condition: Singleton Member Field")){
            return BUG_TYPE.SYNCHRONIZATION;
        }else if(bug_type.equals("Redundant Null Check")){
            return BUG_TYPE.NULL_POINTER_EXEPTION;
        }else{
            return BUG_TYPE.ANOTHER_TYPE;
        }

    }

    @Override
    public String getBugMessage() {
        return "";
    }

    @Override
    public String getClassName() {
        int index_Classname = path.lastIndexOf("/") + 1;
        String className = path.substring(index_Classname, path.length());
        return className;
    }

    @Override
    public int getBugLineNumber() {
        return Integer.parseInt(line_no);
    }

    @Override
    public String getSourcePath() {
        return path;
    }

    @Override
    public ToolCollection getToolName() {
        return ToolCollection.FORTIFY;
    }
}
