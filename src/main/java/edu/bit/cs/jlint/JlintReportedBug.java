package edu.bit.cs.jlint;


import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;
import edu.bit.cs.util.csvParser;
import org.sonar.plugins.findbugs.FindbugsSensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class JlintReportedBug implements ReportedBugInfo {
    private final String type;
    private static String message;
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
        if(is_Jlint_INHERIT()){
            return BUG_TYPE.INHERITANCE;
        }else if(is_Jlint_NPE()){
            return BUG_TYPE.NULL_POINTER_EXEPTION;
        }else if(is_Jlint_SYNC()){
            return BUG_TYPE.SYNCHRONIZATION;
        }else{
            return BUG_TYPE.ANOTHER_TYPE;
        }
    }

    public  Boolean is_Jlint_NPE(){
        if(message.contains(" can be invoked with NULL as number parameter and this parameter is used without check for null")){
            return true;
        }else if(message.contains("Value of referenced ") && message.contains(" may be NULL")){
            return true;
        }else if(message.contains("NULL reference can be used")){
            return true;
        }else if(message.contains("Shift count range [min,max] is out of domain")){
            return true;
        }
        return false;
    }

    public  Boolean is_Jlint_SYNC(){

        if(message.contains("invocation of synchronized ") && message.contains(" can cause deadlock")){
            return true;
        }else if(message.contains("invocation of ")&&message.contains(" forms the loop in class dependency graph")){
            return true;
        }else if(message.contains(" is requested while holding") || message.contains(" with other thread holding ") && message.contains(" and requesting")){
            return true;
        }else if(message.contains("Method wait() can be invoked with monitor of other object locked")){
            return true;
        }else if(message.contains("Call sequence to ") || message.contains(" can cause deadlock in wait()")){
            return true;
        }else if(message.contains("Synchronized") && message.contains(" is overridden by non-synchronized method of derived ")){
            return true;
        }else if(message.contains(" can be called from different threads and is not synchronized")){
            return true;
        }else if(message.contains("Field name of class")){
            return true;
        }else if(message.contains(" implementing ’Runnable’ interface is not \u2028synchronized")){
            return true;
        }else if(message.contains("Value of ") || message.contains(" is changed outside synchronization or constructor")){
            return true;
        }else if(message.contains("Value of ") || message.contains(" is changed while (potentially) owning it")){
            return true;
        }else if(message.contains(".wait() is called without synchronizing on ")){
            return true;
        }
        return false;
    }

    public  Boolean is_Jlint_INHERIT(){
       if(message.contains("is not overridden by method with the same name of derived ")){
           return true;
       }else if(message.contains("Component name in class name shadows one in base class name")){
           return true;
       }else if(message.contains(" shadows component of class name")){
           return true;
       }else if(message.contains("Method finalize() doesn’t call super.finalize()")){
           return true;
       }
        return false;
    }

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
        return ReportedBugInfo.normalizeFilePath(sourcePath, FindbugsSensor.ROOT);
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