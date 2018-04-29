package edu.bit.cs.coverity;

import java.util.List;

public class CoverityReport {
    private List<CoverityReportedBugFromJson> data;

    public void setData(List<CoverityReportedBugFromJson> data) {
        this.data = data;
    }

    public List<CoverityReportedBugFromJson> getData() {
        return data;
    }
}
