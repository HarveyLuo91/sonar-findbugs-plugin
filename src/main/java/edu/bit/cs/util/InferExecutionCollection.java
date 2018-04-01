package edu.bit.cs.util;

import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;
import edu.bit.cs.infer.InferReportParser;
import edu.bit.cs.infer.InferReportedBugFromJson;
import edu.bit.cs.jlint.JlintReportParser;

import java.io.*;
import java.util.Collection;
import java.util.Collections;

public class InferExecutionCollection {

    public static Collection<? extends ReportedBugInfo> JsonCollection() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(csvParser.class.getClassLoader().getResourceAsStream("file/report.json")));
            InferReportParser processor = new InferReportParser();
            return processor.getReportedBugs(br);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
