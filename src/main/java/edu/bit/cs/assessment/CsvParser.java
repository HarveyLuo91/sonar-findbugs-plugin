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

    public static Map<String, TestCaseModel> F_BUGS = Maps.newHashMap();
    public static Map<String, TestCaseModel> T_BUGS = Maps.newHashMap();

    static {
        getTestCases();
    }

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

    static void getTestCases() {
        List<TestCaseModel> testCases = JsonParser.getTestCases();
        System.out.println(testCases.size());
        for (TestCaseModel test : testCases) {
            String file = test.getError_file();
            String name = file.substring(0, file.length() - 5);
            file = name.replaceAll("\\.", "/") + ".java";
            if (test.getCaseType().equals("T")) {
//                T_BUGS.put(JsonParser.getUID(file, test.getError_line()), Lists.newArrayList());
                test.setInfer(1);
                test.setJlint(1);
                test.setFindbugs(1);
                test.setFortify(1);
//                test.setCoverity(1);
                if (T_BUGS.put(JsonParser.getUID(file, test.getError_line()), test) != null) {
//                    System.out.println("T:" + JsonParser.getUID(file, test.getError_line()));
                }
            } else {
//                F_BUGS.put(JsonParser.getUID(file, test.getError_line()), Lists.newArrayList());
                if (F_BUGS.put(JsonParser.getUID(file, test.getError_line()), test) != null) {
//                    System.out.println("F:" + JsonParser.getUID(file, test.getError_line()));
                }
            }

//            if (maps.put(JsonParser.getUID(file, test.getError_line()), Lists.newArrayList()) != null) {
//                System.out.println(JsonParser.getUID(file, test.getError_line()));
//            }
        }
    }

//    public static void main(String[] args) {
//        for (String key : T_BUGS.keySet()) {
//            System.out.println(key);
//        }
//        System.out.println(T_BUGS.size());
//        for (String key : F_BUGS.keySet()) {
//            System.out.println(key);
//        }
//        System.out.println(F_BUGS.size());
//
//    }

}

