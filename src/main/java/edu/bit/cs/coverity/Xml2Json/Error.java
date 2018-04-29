package edu.bit.cs.coverity.Xml2Json;

public class Error {
    private String checker;

    private String file;

    private String function;

    private String unmangled_function;

    private String status;

    private String num;

    private String home;

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getChecker() {
        return this.checker;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return this.file;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunction() {
        return this.function;
    }

    public void setUnmangled_function(String unmangled_function) {
        this.unmangled_function = unmangled_function;
    }

    public String getUnmangled_function() {
        return this.unmangled_function;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNum() {
        return this.num;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getHome() {
        return this.home;
    }
}
