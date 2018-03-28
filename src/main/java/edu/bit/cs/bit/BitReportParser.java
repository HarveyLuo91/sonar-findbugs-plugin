package edu.bit.cs.bit;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;
import edu.bit.cs.infer.InferReportedBugFromJson;

import java.io.*;
import java.util.Collection;
import java.util.List;

public class BitReportParser implements ReportedInfoProcessor {

    public BitReportParser() {

    }

    @Override
    public Collection<? extends ReportedBugInfo> getReportedBugs(BufferedReader br) {
        List<BitReportedBugInfo> reportedBugs = Lists.newArrayList();

        try{
            //Reading the  file
            String bug = br.readLine();

            while (bug != null) {
                String[] tokens = bug.split(" ");
                BitReportedBugInfo bitReportedBugInfo = new BitReportedBugInfo(tokens[0],tokens[1], tokens[2]);
                reportedBugs.add(bitReportedBugInfo);
                bug = br.readLine();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return reportedBugs;
    }
}

