package edu.bit.cs.infer;


import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InferReportParser {

    //this is for testing
    public static void main(String[] args) {
        List<InferReportedBugFromJson> reportedBugs = InferReportParser.get_Reported_Infer_Bugs_From_Json();
        for (InferReportedBugFromJson bug : reportedBugs) {
            System.out.println(bug.toString());
        }
    }

    public static List<InferReportedBugFromJson> get_Reported_Infer_Bugs_From_Json() {

        List<InferReportedBugFromJson> reportedBugs = Lists.newArrayList();
        try {
            //Reading the json file
            BufferedReader br = new BufferedReader(new InputStreamReader(InferReportParser.class.getClassLoader().getResourceAsStream("file/report.json")));
            StringBuilder json = new StringBuilder("");
            try {
                String line = br.readLine();
                while(line != null){
                    json.append(line);
                    line = br.readLine();
                }
                System.out.println(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson  = new Gson();

            reportedBugs = gson.fromJson(json.toString(),new TypeToken<List<InferReportedBugFromJson>>(){}.getType());

//            while (line != null) {
//
//                //System.out.println(line);
//                String[] current_bug_details = line.split(COMMA_DELIMITER);
//
//
//                if (current_bug_details.length > 0) {
//                    //handle the source path
//                    int start_index = current_bug_details[8].lastIndexOf("/java");
//                    current_bug_details[8] = current_bug_details[8].substring(start_index + 1);
//
//                    InferReportedBug bugInstance = new InferReportedBug(current_bug_details[0], current_bug_details[1], current_bug_details[2],
//                            current_bug_details[3], current_bug_details[4], current_bug_details[5], current_bug_details[6],
//                            current_bug_details[7], current_bug_details[8], current_bug_details[9], current_bug_details[10], current_bug_details[11],
//                            current_bug_details[12], current_bug_details[13], current_bug_details[14], current_bug_details[15]);
//                    reportedBugs.add(bugInstance);
//                }
//                line = br.readLine(); //read again to continue
//            }
//
//
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return reportedBugs;
    }


    public static ArrayList<InferReportedBug> get_Reported_Infer_Bugs() {

        //Create List for holding reported bugs objects
        ArrayList<InferReportedBug> reportedBugs = new ArrayList<InferReportedBug>();
        //Delimiters used in the CSV file
        final String COMMA_DELIMITER = ",";
        try {
            //Reading the csv file
            BufferedReader br = new BufferedReader(new InputStreamReader(InferReportParser.class.getClassLoader().getResourceAsStream("file/report.csv")));

            br.readLine(); //first line header(i.e the header) is here now
            //Read to skip the header
            String line = br.readLine();
            //Reading from the second line

            while (line != null) {

                //System.out.println(line);
                String[] current_bug_details = line.split(COMMA_DELIMITER);


                if (current_bug_details.length > 0) {
                    //handle the source path
                    int start_index = current_bug_details[8].lastIndexOf("/java");
                    current_bug_details[8] = current_bug_details[8].substring(start_index + 1);

                    InferReportedBug bugInstance = new InferReportedBug(current_bug_details[0], current_bug_details[1], current_bug_details[2],
                            current_bug_details[3], current_bug_details[4], current_bug_details[5], current_bug_details[6],
                            current_bug_details[7], current_bug_details[8], current_bug_details[9], current_bug_details[10], current_bug_details[11],
                            current_bug_details[12], current_bug_details[13], current_bug_details[14], current_bug_details[15]);
                    reportedBugs.add(bugInstance);
                }
                line = br.readLine(); //read again to continue
            }


        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return reportedBugs;
    }


}

