package edu.bit.cs.assessment.findbugs;

import com.google.common.collect.Sets;
import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.assessment.CsvParser;
import edu.bit.cs.assessment.XmlParser;
import edu.bit.cs.bit.BitReportParser;

import java.io.*;
import java.util.*;

public class Findbugs_Assement {


    private HashMap<String, Set<String>> rules_Findbugs;

    Findbugs_Assement(){
        rules_Findbugs = new HashMap<String, Set<String>>();

        Set<FindBugs_Rule> rules = XmlParser.parseFindBugsRules("src/main/resources/org/sonar/plugins/findbugs/rules-findbugs.xml");
        rules.addAll(XmlParser.parseFindBugsRules("src/main/resources/org/sonar/plugins/findbugs/rules-findsecbugs.xml"));
        rules.addAll(XmlParser.parseFindBugsRules("src/main/resources/org/sonar/plugins/findbugs/rules-jsp.xml"));
        rules.addAll(XmlParser.parseFindBugsRules("src/main/resources/org/sonar/plugins/findbugs/rules-fbcontrib.xml"));

        rules_Findbugs.put("INJECTION",this.getRules("src/main/java/edu/bit/cs/assessment/findbugs/INJECTION"));
        rules_Findbugs.put("NULL_POINTER_EXCEPTION",this.getRules("src/main/java/edu/bit/cs/assessment/findbugs/NULL_POINTER_EXCEPTION"));
        rules_Findbugs.put("RESOURCE_LEAK",this.getRules("src/main/java/edu/bit/cs/assessment/findbugs/RESOURCE_LEAK"));
        rules_Findbugs.put("SYNCHRONIZATION",this.getRules("src/main/java/edu/bit/cs/assessment/findbugs/SYNCHRONIZATION"));
        rules_Findbugs.put("CROSS_SITE_SCRIPTING",this.getRules("src/main/java/edu/bit/cs/assessment/findbugs/CROSS_SITE_SCRIPTING"));
        //rules_Findbugs.put("INHERITANCE",this.getRules("src/main/java/edu/bit/cs/assessment/findbugs/INHERITANCE"));
    }


    private static void judgeType() {
        Set<FindBugs_Rule> rules = XmlParser.parseFindBugsRules("src/main/resources/org/sonar/plugins/findbugs/rules-findbugs.xml");
        rules.addAll(XmlParser.parseFindBugsRules("src/main/resources/org/sonar/plugins/findbugs/rules-fbcontrib.xml"));
        rules.addAll(XmlParser.parseFindBugsRules("src/main/resources/org/sonar/plugins/findbugs/rules-findsecbugs.xml"));
        for (FindBugs_Rule g: rules) {
            if(g.judgetype().equals(BUG_TYPE.INHERITANCE)){
                    System.out.println(g.getRule_configKey());
            }
        }
    }

    private void generateTypeString(){
        for (String type: this.rules_Findbugs.keySet()) {
            Set<String> rule_set = this.rules_Findbugs.get(type);
            System.out.println("\n"+type );
            int i =0;
            for(String rule : rule_set){
                //System.out.println(i + " : " + rule);
                i = i+1;
                System.out.print("type.equals(\"" + rule + "\")" + "||" );
            }

        }

    }

    private Set<String> getRules(String fileName){

        Set<String> bugs = Sets.newHashSet();
        int type_index = fileName.lastIndexOf('/') + 1;
        String type = fileName.substring(type_index);
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                bugs.add(strLine);
            }
                in.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            this.rules_Findbugs.put(type,bugs);
            return bugs;
    }

    public static void main(String[] args) {

        Findbugs_Assement cs = new Findbugs_Assement();
        //judgeType();

        //cs.generateTypeString();

        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(Findbugs_Assement.class.getClassLoader().getResourceAsStream("file/BitDetector.txt")));


            BitReportParser parser = new BitReportParser();
            Collection<? extends ReportedBugInfo> BitReportedBugs = parser.getReportedBugs(br);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

