package edu.bit.cs.coverity;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;
import edu.bit.cs.coverity.Xml2Json.Error;
import edu.bit.cs.coverity.Xml2Json.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CoverityReportParser implements ReportedInfoProcessor {
    @Override
    public Collection<? extends ReportedBugInfo> getReportedBugs(BufferedReader br) {
//        List<InferReportedBugFromJson> reportedBugs = Lists.newArrayList();
        CoverityReport report = null;
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
            report = gson.fromJson(json.toString(), CoverityReport.class);
            return report.getData();

//            report = gson.fromJson(json.toString(), new TypeToken<List<InferReportedBugFromJson>>() {
//            }.getType());

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return null;
    }

    public static int getIntValue(String str) {
        if (str == null) {
            return 0;
        }
        String valueSection = str.split("[a-zA-Z]")[0].trim();
        if (valueSection.length() == 0) {
            return 0;
        }
        int value = 0;
        try {
            value = Integer.parseInt(valueSection);
        } catch (Exception e) {
            value = 0;
        }
        return value;
    }

    public static void main(String[] args) {
        Map<String, String> filePath = Maps.newHashMap();
        BufferedReader br = new BufferedReader(new InputStreamReader(CoverityReportParser.class.getClassLoader().getResourceAsStream("file/benchmark-generation-clear-coverity.json")));
        Collection<? extends ReportedBugInfo> coverityReportedBugs = new CoverityReportParser().getReportedBugs(br);
        br = new BufferedReader(new InputStreamReader(CoverityReportParser.class.getClassLoader().getResourceAsStream("file/benchmark-generation-clear-coverity-xml2json.json")));
        List<Error> errors = new Parser().getReportedBugs(br);
        for (Error err : errors) {
            filePath.put(err.getNum(), err.getFile());
        }

        for (ReportedBugInfo bug : coverityReportedBugs) {
            String num = bug.getClassName().split("[a-zA-Z]")[0].trim();
            if (!filePath.get(num).contains(bug.getClassName().substring(num.length()))) {
                System.out.println("num:" + num + " name:" + bug.getClassName());
                System.out.println(filePath.get(num));
            }
//            ((CoverityReportedBugFromJson) bug).setName(filePath.get(num));
//            System.out.println(bug.getClassName() + "-" + ((CoverityReportedBugFromJson) bug).getLine());
        }

    }
}

