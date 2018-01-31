package edu.bit.cs.assessment;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.bit.cs.ReportedBugInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

public class CsvParser {
    //    public static final List<ReportedBugInfo> FAKEBUGS = parseFakeBugs();
    public static final Set<String> FAKEBUGS = parseFakeBugs();

//    public static void main(String[] args) {
//
//        for (ReportedBugInfo fakebug : FAKEBUGS) {
//            ((FakeBug) fakebug).printdetails();
//        }
//    }

    public static Set<String> parseFakeBugs() {

        Set<String> fakebugset = Sets.newHashSet();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(CsvParser.class.getClassLoader().getResourceAsStream("file/file.txt")));
            String line = br.readLine();

            while (line != null) {
                System.out.println();
                String tokens[] = line.split(",");

                FakeBug fakebug = new FakeBug();
                fakebug.filename = tokens[0];

                fakebug.lineNumber = tokens[1].replaceFirst("L", "");
                ;
                fakebug.variable = tokens[2];
                if (tokens[3].trim().contains(" ")) {
                    String[] tok = tokens[3].split(" ");

                    for (int i = 0; i < tok.length; i++) {
                        fakebug.tags.add(tok[i]);
                    }
                } else {
                    fakebug.tags.add(tokens[3].trim());
                }

                fakebugset.add(fakebug.getUID());
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        return fakebugs;
        return fakebugset;
    }

}

