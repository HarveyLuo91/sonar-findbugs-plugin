package edu.bit.cs.coverity.Xml2Json;

import java.util.List;

public class Coverity {
    private String title;

    private List<Error> error;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setError(List<Error> error) {
        this.error = error;
    }

    public List<Error> getError() {
        return this.error;
    }
}
