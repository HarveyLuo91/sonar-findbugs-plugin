package edu.bit.cs.infer;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class inferReportParser {

    //this is for testing
    public static void main(String[] args) {
        ArrayList<inferReportedBug> reportedBugs = inferReportParser.get_Reported_Infer_Bugs() ;
        for (inferReportedBug bug: reportedBugs) {
            System.out.println(bug.toString());
        }
    }



    //Create List for holding reported bugs objects
    private static ArrayList<inferReportedBug> reportedBugs = new ArrayList<inferReportedBug>();



    public static ArrayList<inferReportedBug> get_Reported_Infer_Bugs() {
        //Delimiters used in the CSV file
        final String COMMA_DELIMITER = ",";
        try {
            //Reading the csv file
            BufferedReader br = new BufferedReader(new InputStreamReader(inferReportParser.class.getClassLoader().getResourceAsStream("file/infer_report.csv")));

            br.readLine(); //first line header(i.e the header) is here now
            //Read to skip the header
            String line =   br.readLine();
            //Reading from the second line

            while(line != null){

                //System.out.println(line);
                String[] current_bug_details = line.split(COMMA_DELIMITER);

               if(current_bug_details.length > 0){
                   inferReportedBug bugInstance = new inferReportedBug(current_bug_details[0],current_bug_details[1],current_bug_details[2],
                            current_bug_details[3],current_bug_details[4],current_bug_details[5],current_bug_details[6],
                            current_bug_details[7],current_bug_details[8],current_bug_details[9],current_bug_details[10],current_bug_details[11],
                            current_bug_details[12],current_bug_details[13],current_bug_details[14],current_bug_details[15]);
                    reportedBugs.add(bugInstance);
                }
                line = br.readLine(); //read again to continue
            }



        }catch (Exception ee){
            ee.printStackTrace();
        }
        return reportedBugs;
    }


}

