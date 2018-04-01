package edu.bit.cs.Fortify;

import com.google.common.collect.Lists;
import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FortifyReportParser  {

    public Collection<ReportedBugInfo> getReportedBugs() {

        List<ReportedBugInfo> reportedBugs = Lists.newArrayList();
        BufferedReader br1 =  new BufferedReader(new InputStreamReader(FortifyReportParser.class.getClassLoader().getResourceAsStream("file/Fortify1.csv")));
        BufferedReader  br2 =  new BufferedReader(new InputStreamReader(FortifyReportParser.class.getClassLoader().getResourceAsStream("file/Fortify2.csv")));
        try{
            String line1 = br1.readLine();

            while(line1 !=null){
                System.out.println();
                String tokens[] = line1.split(",");

                FortifyReportedBugFromCsv bugInstance = new FortifyReportedBugFromCsv();
                bugInstance.bug_type = tokens[0];
                bugInstance.path = tokens[1];
                bugInstance.line_no = tokens[2];
                reportedBugs.add(bugInstance);
                line1 = br1.readLine();
            }

            String line2 = br2.readLine();
            while(line2 !=null){
                System.out.println();
                String tokens[] = line2.split(",");

                FortifyReportedBugFromCsv bugInstance = new FortifyReportedBugFromCsv();
                bugInstance.bug_type = tokens[0];
                bugInstance.path = tokens[1];
                bugInstance.line_no = "0";
                reportedBugs.add(bugInstance);
                line2 = br2.readLine();
            }
            return reportedBugs;
        }catch(Exception e){
            e.printStackTrace();
        }
        return reportedBugs;
    }

    public static void main(String[] args) {
        FortifyReportParser parser = new FortifyReportParser();

        Collection<ReportedBugInfo> bugs = parser.getReportedBugs();
        System.out.println(bugs.size());
        for (ReportedBugInfo bug:bugs) {
            if(bug.getBugType() !=  BUG_TYPE.ANOTHER_TYPE){
                System.out.println(bug.getUID());
                System.out.println(bug.getBugType());
                System.out.println(bug.getSourcePath());
                System.out.println(bug.getToolName());
            }

        }
    }

}
