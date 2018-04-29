package edu.bit.cs.fortify;

import com.google.gson.Gson;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;
import edu.bit.cs.coverity.CoverityReportParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

public class FortifyReportParser implements ReportedInfoProcessor {

    @Override
    public Collection<? extends ReportedBugInfo> getReportedBugs(BufferedReader br) {
        FortifyReport report = null;
        try {
            //Reading the json file
            StringBuilder json = new StringBuilder("");
            try {
                String line = br.readLine();
                while (line != null) {
                    json.append(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            report = gson.fromJson(json.toString(), FortifyReport.class);
            return report.getData();

//            report = gson.fromJson(json.toString(), new TypeToken<List<InferReportedBugFromJson>>() {
//            }.getType());

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(CoverityReportParser.class.getClassLoader().getResourceAsStream("file/benchmark-generation-clear-fortify.json")));
        Collection<? extends ReportedBugInfo> fortifyReportedBugs = new FortifyReportParser().getReportedBugs(br);
        System.out.println(fortifyReportedBugs.size());
        for (ReportedBugInfo bug : fortifyReportedBugs) {
            System.out.println(bug.getUID());
        }
    }
}
