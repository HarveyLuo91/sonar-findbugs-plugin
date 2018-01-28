package edu.bit.cs.assessment;

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;

import java.util.ArrayList;

public class FakeBug implements ReportedBugInfo{

    String filename;
    String lineNumber;
    String variable;
    ArrayList<String> tags = new ArrayList<>();

    void printdetails() {
        System.out.println("\n");
        System.out.println("File Name: " + this.filename);
        System.out.println("Line Number: " + this.lineNumber);
        System.out.println("Variable: " + this.variable);
        System.out.println("UID: " + this.getUID());
        System.out.print("tags: ");
        for (String tag : this.tags) {
            System.out.print(tag + " ");
        }
    }

//    String getUID() {
//        return this.filename + "-" + this.lineNumber + "-" + BUG_TYPE.NULL_POINTER_EXEPTION;
//    }

    @Override
    public BUG_TYPE getBugType() {
        return BUG_TYPE.NULL_POINTER_EXEPTION;
    }

    @Override
    public String getBugMessage() {
        return "this is a fake bug";
    }

    @Override
    public String getClassName() {
        return filename;
    }

    @Override
    public int getBugLineNumber() {
        return Integer.parseInt(lineNumber);
    }

    @Override
    public String getSourcePath() {
        return filename;
    }

    @Override
    public ToolCollection getToolName() {
        return ToolCollection.NONE;
    }
}
