package edu.bit.cs.fortify;

import java.util.List;

public class FortifyReport {
    private List<FortifyReportedBugFromJson> data;

    public void setData(List<FortifyReportedBugFromJson> data) {
        this.data = data;
    }

    public List<FortifyReportedBugFromJson> getData() {
        return data;
    }
}
