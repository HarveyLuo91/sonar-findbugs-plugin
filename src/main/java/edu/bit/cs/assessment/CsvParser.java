package edu.bit.cs.assessment;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class CsvParser {
    //    public static final List<FakeBug> FAKEBUGINSTANCES = parseFakeBugs();
//    public static final Set<String> FAKEBUGS = getFakeBugs();
    public static final Map<String, Integer> TAGS = Maps.newHashMap();
    public static final Map<String, List<String>> FAKEBUGS = parseFakeBugs();

//    public static void main(String[] args) {
//
//        for (FakeBug fakebug : FAKEBUGINSTANCES) {
//            ((FakeBug) fakebug).printdetails();
//        }
//    }

    private static Map<String, List<String>> parseFakeBugs() {
//        ArrayList<FakeBug> fakebugs = Lists.newArrayList();
//        Set<String> fakebugset = Sets.newHashSet();
        Map<String, List<String>> bugs = Maps.newHashMap();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(CsvParser.class.getClassLoader().getResourceAsStream("file/file.txt")));
            String line = br.readLine();

            while (line != null) {
                System.out.println();
                String tokens[] = line.split(",");

                FakeBug fakebug = new FakeBug();
                fakebug.filename = tokens[0];

                fakebug.lineNumber = tokens[1].replaceFirst("L", "");
                fakebug.variable = tokens[2];
                if (tokens[3].trim().contains(" ")) {
                    String[] tok = tokens[3].split(" ");

                    for (int i = 0; i < tok.length; i++) {
                        fakebug.tags.add(tok[i]);
                    }
                } else {
                    fakebug.tags.add(tokens[3].trim());
                }


                //fakebug.printdetails();
//                fakebugs.add(fakebug);
                bugs.put(fakebug.getUID(), fakebug.tags);
                for (String tag : fakebug.tags) {
                    if (!TAGS.containsKey(tag)) {
                        TAGS.put(tag, 1);
                    } else {
                        TAGS.put(tag, TAGS.get(tag) + 1);
                    }
                }
//                fakebugset.add(fakebug.getUID());
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        return fakebugs;
//        return fakebugset;
        return bugs;
    }

//    static Set<String> getFakeBugs() {
//        Set<String> bugSet = Sets.newHashSet();
//        for (FakeBug bug : FAKEBUGINSTANCES) {
//            bugSet.add(bug.getUID());
//        }
//        return bugSet;
//    }
}

