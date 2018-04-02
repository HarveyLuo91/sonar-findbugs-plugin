package edu.bit.cs.Fortify;

import com.google.common.collect.Lists;
import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

public class FortifyReportParser {

    public static Collection<ReportedBugInfo> getReportedBugs() {

        List<ReportedBugInfo> reportedBugs = Lists.newArrayList();
        BufferedReader br1 = new BufferedReader(new InputStreamReader(FortifyReportParser.class.getClassLoader().getResourceAsStream("file/Fortify.csv")));
        try {
            String line1 = br1.readLine();
            while (line1 != null) {
                System.out.println();
                String tokens[] = line1.split(",");

                FortifyReportedBugFromCsv bugInstance = new FortifyReportedBugFromCsv();
                bugInstance.bug_type = tokens[0];
                bugInstance.path = tokens[1];
                bugInstance.line_no = tokens[2];
                reportedBugs.add(bugInstance);
                line1 = br1.readLine();
            }
            return reportedBugs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportedBugs;
    }

    public static void main(String[] args) {
        for (ReportedBugInfo bugInstance:FortifyReportParser.getReportedBugs()) {
            if(bugInstance.getBugType() == BUG_TYPE.INJECTION)
            System.out.println(bugInstance.getUID());
        }
    }
}
