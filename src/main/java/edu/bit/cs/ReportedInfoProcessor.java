package edu.bit.cs;

import java.io.BufferedReader;
import java.util.Collection;

public interface ReportedInfoProcessor {
    Collection<? extends ReportedBugInfo> getReportedBugs(BufferedReader br);
}
