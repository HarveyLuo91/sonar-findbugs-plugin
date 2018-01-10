package edu.bit.cs.infer;


import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class InferReportParser implements ReportedInfoProcessor {


//    public static List<InferReportedBugFromJson> get_Reported_Infer_Bugs_From_Json(String reporterPath) {
//
//        List<InferReportedBugFromJson> reportedBugs = Lists.newArrayList();
//        try {
//            //Reading the json file
//            BufferedReader br = new BufferedReader(new InputStreamReader(InferReportParser.class.getClassLoader().getResourceAsStream(reporterPath)));
//            StringBuilder json = new StringBuilder("");
//            try {
//                String line = br.readLine();
//                while (line != null) {
//                    json.append(line);
//                    line = br.readLine();
//                }
//                System.out.println(json.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Gson gson = new Gson();
//
//            reportedBugs = gson.fromJson(json.toString(), new TypeToken<List<InferReportedBugFromJson>>() {
//            }.getType());
//
//        } catch (Exception ee) {
//            ee.printStackTrace();
//        }
//        return reportedBugs;
//    }


    @Override
    public Collection<? extends ReportedBugInfo> getReportedBugs(BufferedReader br) {
        List<InferReportedBugFromJson> reportedBugs = Lists.newArrayList();
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

            reportedBugs = gson.fromJson(json.toString(), new TypeToken<List<InferReportedBugFromJson>>() {
            }.getType());

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return reportedBugs;
    }
}

