package edu.bit.cs.jlint;


import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;


public class JlintReportedBug implements ReportedBugInfo {
    private final String type;
    private final String message;
    private final String className;
    private final int bugLineNumber;
    private final String sourcePath;

    public static final ToolCollection JLINT = ToolCollection.JLINT;

    public JlintReportedBug(String type, String message, String className, int bugLineNumber, String sourcePath) {
        this.type = type;
        this.message = message;
        this.className = className;
        this.bugLineNumber = bugLineNumber;
        this.sourcePath = sourcePath;
    }

    @Override
    public BUG_TYPE getBugType() {
        if(type.contains("NULL")){
            return BUG_TYPE.NULL_POINTER_EXEPTION;
        }else if(type.contains("RESOURCE")){
            return BUG_TYPE.RESOURCE_LEAK;
        }else{
            return BUG_TYPE.ANOTHER_TYPE;
        }

    }
   // @Override
    //public String getType(){
      //  return this.type;
    //}
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
    public ToolCollection getToolName() {
        return JLINT;
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