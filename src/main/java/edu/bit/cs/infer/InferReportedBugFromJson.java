package edu.bit.cs.infer;

import com.google.gson.Gson;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;

import java.util.List;

public class InferReportedBugFromJson implements ReportedBugInfo {

    private String bug_class;
    private String kind;
    private String bug_type;
    private String qualifier;
    private String severity;
    private String visibility;
    private int line;
    private int column;
    private String procedure;
    private String procedure_id;
    private int procedure_start_line;
    private String file;
    private List<BugTrace> bug_trace;
    private int key;
    private List<QualifierTags> qualifier_tags;
    private int hash;
    private String bug_type_hum;

    public void setBug_class(String bug_class) {
        this.bug_class = bug_class;
    }

    public String getBug_class() {
        return bug_class;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public void setBug_type(String bug_type) {
        this.bug_type = bug_type;
    }

    public String getBug_type() {
        return bug_type;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSeverity() {
        return severity;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure_id(String procedure_id) {
        this.procedure_id = procedure_id;
    }

    public String getProcedure_id() {
        return procedure_id;
    }

    public void setProcedure_start_line(int procedure_start_line) {
        this.procedure_start_line = procedure_start_line;
    }

    public int getProcedure_start_line() {
        return procedure_start_line;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setBug_trace(List<BugTrace> bug_trace) {
        this.bug_trace = bug_trace;
    }

    public List<BugTrace> getBug_trace() {
        return bug_trace;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setQualifier_tags(List<QualifierTags> qualifier_tags) {
        this.qualifier_tags = qualifier_tags;
    }

    public List<QualifierTags> getQualifier_tags() {
        return qualifier_tags;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public int getHash() {
        return hash;
    }

    public void setBug_type_hum(String bug_type_hum) {
        this.bug_type_hum = bug_type_hum;
    }

    public String getBug_type_hum() {
        return bug_type_hum;
    }

    @Override
    public String getBugType() {
        return bug_type;
    }

    @Override
    public String getBugMessage() {
        return qualifier;
    }

    @Override
    public String getClassName() {
        int start_index_class_Name = this.file.lastIndexOf('/');
        String class_Name = this.file.substring(start_index_class_Name + 1, this.file.length());
        return class_Name;
    }

    @Override
    public int getBugLineNumber() {
        return line;
    }

    @Override
    public String getSourcePath() {
        return file;
    }

    @Override
    public ToolCollection getToolName() {
        return null;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Bug Type: " + this.getBug_type() + "\n");
        string.append("Error Message: " + this.getBugMessage() + "\n");
        string.append("ClassName: " + this.getClassName() + "\n");
        string.append("Bug_LineNumber: " + this.getBugLineNumber() + "\n");
        string.append("Source Path: " + this.getSourcePath() + "\n");


        return string.toString();
    }
}