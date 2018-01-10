package edu.bit.cs.infer;

import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;

import java.util.List;

public class InferReportedBugFromJson implements ReportedBugInfo {
    /**
     * bug_class : PROVER
     * kind : ERROR
     * bug_type : NULL_DEREFERENCE
     * qualifier : object `null` could be null and is dereferenced by call to `a(...)` at line 5.
     * severity : HIGH
     * visibility : user
     * line : 5
     * column : -1
     * procedure : int InterproceduralMethodOrdering.test1()
     * procedure_id : java.npe.InterproceduralMethodOrdering.test1():int.ea40eef64e3453aa1e7da659c38d256c
     * procedure_start_line : 4
     * file : killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java
     * bug_trace : [{"level":0,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":4,"column_number":-1,"description":"start of procedure test1()","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.test1()"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.test1():int.ea40eef64e3453aa1e7da659c38d256c"}]},{"level":0,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":5,"column_number":-1,"description":"","node_tags":[]},{"level":1,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":20,"column_number":-1,"description":"start of procedure a(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.a(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.a(java.lang.Object):int.e336e73fbc91b02a6f62176a6f6eda16"}]},{"level":1,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":21,"column_number":-1,"description":"","node_tags":[]},{"level":2,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":52,"column_number":-1,"description":"start of procedure z(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.z(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.z(java.lang.Object):int.7e4d932a2c0c054976a928dfb98f9cf2"}]},{"level":2,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":53,"column_number":-1,"description":"","node_tags":[]},{"level":3,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":24,"column_number":-1,"description":"start of procedure b(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.b(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.b(java.lang.Object):int.9b8c631f21c28d335294c76263d58cdd"}]},{"level":3,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":25,"column_number":-1,"description":"","node_tags":[]},{"level":4,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":48,"column_number":-1,"description":"start of procedure y(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.y(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.y(java.lang.Object):int.76e8e0759e4ccf7e31b21464c85869df"}]},{"level":4,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":49,"column_number":-1,"description":"","node_tags":[]},{"level":5,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":28,"column_number":-1,"description":"start of procedure c(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.c(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.c(java.lang.Object):int.1917eabfe425452f7f2da62377588bfe"}]},{"level":5,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":29,"column_number":-1,"description":"","node_tags":[]},{"level":6,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":45,"column_number":-1,"description":"start of procedure x(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.x(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.x(java.lang.Object):int.5f5741d1dad6bd1fd442931f905ca030"}]},{"level":7,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":32,"column_number":-1,"description":"start of procedure d(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.d(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.d(java.lang.Object):int.c89c6bc11fb0f1f43ba092ae4b0e977b"}]},{"level":7,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":33,"column_number":-1,"description":"","node_tags":[]},{"level":8,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":40,"column_number":-1,"description":"start of procedure w(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.w(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.w(java.lang.Object):int.db01e2c7e157cde2257c8878c5aaed6e"}]},{"level":8,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":41,"column_number":-1,"description":"","node_tags":[]},{"level":9,"filename":"killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java","line_number":37,"column_number":-1,"description":"start of procedure e(...)","node_tags":[{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.e(Object)"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.e(java.lang.Object):int.9cee14eab866c8078c1044e4cfb623d2"}]}]
     * key : b891320c3f80cf32f82e24e6e38f8d8b
     * qualifier_tags : [{"tag":"bucket","value":"B1"},{"tag":"line","value":"5"},{"tag":"value","value":"null"},{"tag":"call_procedure","value":"a(...)"}]
     * hash : 369c8a867a584e08eaba1486a88a3892
     * bug_type_hum : Null Dereference
     * censored_reason :
     */

    private static ToolCollection INFER = ToolCollection.INFER;

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
    private String key;
    private String hash;
    private String bug_type_hum;
    private String censored_reason;
    private List<BugTraceBean> bug_trace;
    private List<QualifierTagsBean> qualifier_tags;

    public String getBug_class() {
        return bug_class;
    }

    public void setBug_class(String bug_class) {
        this.bug_class = bug_class;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBug_type() {
        return bug_type;
    }

    public void setBug_type(String bug_type) {
        this.bug_type = bug_type;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getProcedure_id() {
        return procedure_id;
    }

    public void setProcedure_id(String procedure_id) {
        this.procedure_id = procedure_id;
    }

    public int getProcedure_start_line() {
        return procedure_start_line;
    }

    public void setProcedure_start_line(int procedure_start_line) {
        this.procedure_start_line = procedure_start_line;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBug_type_hum() {
        return bug_type_hum;
    }

    public void setBug_type_hum(String bug_type_hum) {
        this.bug_type_hum = bug_type_hum;
    }

    public String getCensored_reason() {
        return censored_reason;
    }

    public void setCensored_reason(String censored_reason) {
        this.censored_reason = censored_reason;
    }

    public List<BugTraceBean> getBug_trace() {
        return bug_trace;
    }

    public void setBug_trace(List<BugTraceBean> bug_trace) {
        this.bug_trace = bug_trace;
    }

    public List<QualifierTagsBean> getQualifier_tags() {
        return qualifier_tags;
    }

    public void setQualifier_tags(List<QualifierTagsBean> qualifier_tags) {
        this.qualifier_tags = qualifier_tags;
    }

    public static class BugTraceBean {
        /**
         * level : 0
         * filename : killbugs-testcases-src/src/main/java/java/npe/InterproceduralMethodOrdering.java
         * line_number : 4
         * column_number : -1
         * description : start of procedure test1()
         * node_tags : [{"tag":"kind","value":"procedure_start"},{"tag":"name","value":"int InterproceduralMethodOrdering.test1()"},{"tag":"name_id","value":"java.npe.InterproceduralMethodOrdering.test1():int.ea40eef64e3453aa1e7da659c38d256c"}]
         */

        private int level;
        private String filename;
        private int line_number;
        private int column_number;
        private String description;
        private List<NodeTagsBean> node_tags;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public int getLine_number() {
            return line_number;
        }

        public void setLine_number(int line_number) {
            this.line_number = line_number;
        }

        public int getColumn_number() {
            return column_number;
        }

        public void setColumn_number(int column_number) {
            this.column_number = column_number;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<NodeTagsBean> getNode_tags() {
            return node_tags;
        }

        public void setNode_tags(List<NodeTagsBean> node_tags) {
            this.node_tags = node_tags;
        }

        public static class NodeTagsBean {
            /**
             * tag : kind
             * value : procedure_start
             */

            private String tag;
            private String value;

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    public static class QualifierTagsBean {
        /**
         * tag : bucket
         * value : B1
         */

        private String tag;
        private String value;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
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
        return INFER;
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