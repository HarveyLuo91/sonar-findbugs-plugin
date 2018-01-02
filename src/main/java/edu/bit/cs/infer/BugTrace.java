package edu.bit.cs.infer;

import java.util.List;

public class BugTrace {

    private int level;
    private String filename;
    private int line_number;
    private int column_number;
    private String description;
    private List<NodeTag> node_tags;
    public void setLevel(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFilename() {
        return filename;
    }

    public void setLine_number(int line_number) {
        this.line_number = line_number;
    }
    public int getLine_number() {
        return line_number;
    }

    public void setColumn_number(int column_number) {
        this.column_number = column_number;
    }
    public int getColumn_number() {
        return column_number;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setNode_tags(List<NodeTag> node_tags) {
        this.node_tags = node_tags;
    }
    public List<NodeTag> getNode_tags() {
        return node_tags;
    }

}