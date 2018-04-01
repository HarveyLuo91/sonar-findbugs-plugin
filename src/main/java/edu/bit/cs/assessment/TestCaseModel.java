package edu.bit.cs.assessment;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.List;

public class TestCaseModel {
    private String mainClass;
    private List<String> context = Lists.newArrayList();
    private String error_file;
    private int error_line;
    private int findbugs = 0;
    private int jlint = 0;
    private int infer = 0;
    //    private List<String> tags = Lists.newArrayList();
    private String caseType;

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(List<String> context) {
        this.context = context;
    }

    public String getError_file() {
        return error_file;
    }

    public void setError_file(String error_file) {
        this.error_file = error_file;
    }

    public int getError_line() {
        return error_line;
    }

    public void setError_line(int error_line) {
        this.error_line = error_line;
    }

    public int getFindbugs() {
        return findbugs;
    }

    public void setFindbugs(int findbugs) {
        this.findbugs = findbugs;
    }

    public int getJlint() {
        return jlint;
    }

    public void setJlint(int jlint) {
        this.jlint = jlint;
    }

    public int getInfer() {
        return infer;
    }

    public void setInfer(int infer) {
        this.infer = infer;
    }

//    public List<String> getTags() {
//        return tags;
//    }

//    public void setTags(List<String> tags) {
//        this.tags = tags;
//    }

    public String getCaseType() {
        return caseType;
    }

    public void addContext(String context) {
        this.context.add(context);
    }

    public void addContexts(List<String> contexts) {
        this.context.addAll(contexts);
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
