package edu.bit.cs.infer;

import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;

public class InferReportedBug implements ReportedBugInfo{

    private  final String infer_Bug_Class;
    private  final String infer_Bug_Kind;
    private  final String infer_Bug_Type;
    private  final String infer_Bug_Qualifier;
    private  final String infer_Bug_Severety;
    private  final String infer_Bug_Line;
    private  final String infer_Bug_Procedure;
    private  final String infer_Bug_Procedure_Id;
    private  final String infer_Bug_File;
    private  final String infer_Bug_Trace;
    private  final String infer_Bug_Key;
    private  final String infer_Bug_Qualifier_Tag;
    private  final String infer_Bug_Qualifier_Hash;
    private  final String infer_Bug_Id;
    private  final String infer_Bug_Always_Report;
    private  final String infer_Bug_advice;

    public static final ToolCollection INFER = ToolCollection.INFER;



   public InferReportedBug(String infer_Bug_Class, String infer_Bug_Kind, String infer_Bug_Type,
                           String infer_Bug_Qualifier, String infer_Bug_Severety, String infer_Bug_Line, String infer_Bug_Procedure,
                           String infer_Bug_Procedure_Id, String infer_Bug_File, String infer_Bug_Trace, String infer_Bug_Key, String infer_Bug_Qualifier_Tag,
                           String    infer_Bug_Qualifier_Hash, String  infer_Bug_Id, String infer_Bug_Always_Report, String infer_Bug_advice) {

        this.infer_Bug_Class = infer_Bug_Class;
        this.infer_Bug_Kind = infer_Bug_Kind;
        this.infer_Bug_Type = infer_Bug_Type;
        this.infer_Bug_Qualifier = infer_Bug_Qualifier;
        this.infer_Bug_Severety = infer_Bug_Severety;
        this.infer_Bug_Line =  infer_Bug_Line;
        this.infer_Bug_Procedure = infer_Bug_Procedure;
        this.infer_Bug_Procedure_Id = infer_Bug_Procedure_Id;
        this.infer_Bug_File = infer_Bug_File;
        this.infer_Bug_Trace = infer_Bug_Trace;
        this.infer_Bug_Key = infer_Bug_Key;
        this.infer_Bug_Qualifier_Tag = infer_Bug_Qualifier_Tag;
        this.infer_Bug_Qualifier_Hash = infer_Bug_Qualifier_Hash;
        this.infer_Bug_Id = infer_Bug_Id;
        this.infer_Bug_Always_Report = infer_Bug_Always_Report;
        this.infer_Bug_advice = infer_Bug_advice;

    }


    public String getInfer_Bug_Class() {
        return infer_Bug_Class;
    }

    public String getInfer_Bug_Kind() {
        return infer_Bug_Kind;
    }

    public String getInfer_Bug_Type() {
        return infer_Bug_Type;
    }

    public String getInfer_Bug_Qualifier() {
        return infer_Bug_Qualifier;
    }

    public String getInfer_Bug_Severety() {
        return infer_Bug_Severety;
    }

    public String getInfer_Bug_Line() {
        return infer_Bug_Line;
    }

    public String getInfer_Bug_Procedure() {
        return infer_Bug_Procedure;
    }

    public String getInfer_Bug_Procedure_Id() {
        return infer_Bug_Procedure_Id;
    }

    public String getInfer_Bug_File() {
       int start_index = infer_Bug_File.lastIndexOf("/main");
        return infer_Bug_File.substring(start_index+1);
    }

    public String getInfer_Bug_Trace() {
        return infer_Bug_Trace;
    }

    public String getInfer_Bug_Key() {
        return infer_Bug_Key;
    }

    public String getInfer_Bug_Qualifier_Tag() {
        return infer_Bug_Qualifier_Tag;
    }

    public String getInfer_Bug_Qualifier_Hash() {
        return infer_Bug_Qualifier_Hash;
    }

    public String getInfer_Bug_Id() {
        return infer_Bug_Id;
    }

    public String getInfer_Bug_Always_Report() {
        return infer_Bug_Always_Report;
    }

    public String getInfer_Bug_advice() {
        return infer_Bug_advice;
    }
    public String getInfer_Bug_Class_Name() {
        //split the class name from the file name in getInfer_Bug_File()
        String class_path = getInfer_Bug_File();
        int start_index_class_Name = class_path.lastIndexOf('/');
        String class_Name = class_path.substring(start_index_class_Name + 1,class_path.length());
        return class_Name;
    }
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Bug Type: " + getInfer_Bug_Type() + "\n");
        string.append("Error Message: " + getInfer_Bug_Qualifier() + "\n");
        string.append("ClassName: " + getInfer_Bug_Class_Name() + "\n");
        string.append("Bug_LineNumber: " + getInfer_Bug_Line() + "\n");
        string.append("Source Path: " + getInfer_Bug_File() + "\n");


        return string.toString();
    }

    @Override
    public String getBugType() {
        return infer_Bug_Type;
    }

    @Override
    public String getBugMessage() {
        return infer_Bug_Qualifier;
    }

    @Override
    public String getClassName() {
        return getInfer_Bug_Class_Name();
    }

    @Override
    public int getBugLineNumber() {
        return Integer.parseInt(infer_Bug_Line);
    }

    @Override
    public String getSourcePath() {
        return infer_Bug_File;
    }

    @Override
    public ToolCollection getToolName() {
        return INFER;
    }
}