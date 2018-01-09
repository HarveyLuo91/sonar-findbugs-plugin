package edu.bit.cs.jlint;


import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JlintReportParser implements ReportedInfoProcessor {

    private ArrayList ReportedBugs = new ArrayList();

    @Override
    public Collection<? extends ReportedBugInfo> getReportedBugs(BufferedReader br) {

        try {
            // create a Buffered Reader object instance with a FileReader, remeber to change the way the file is read as a resource
//            BufferedReader br = new BufferedReader(new InputStreamReader(JlintReportParser.class.getClassLoader().getResourceAsStream("file/jlint_npe_repoter.txt")));
            // read the first line from the text file
            String fileRead = br.readLine();
            // loop until all lines are read
            while (fileRead != null) {

                if (fileRead.contains("Verification completed")) {
                    fileRead = br.readLine();
                    continue;
                }

                System.out.println("jlint report bug:" + fileRead);

                int index;
                if (fileRead.charAt(1) == ':') {
                    //find second occurence of :, the number after that is the line number of the reported bug
                    index = fileRead.indexOf(":", fileRead.indexOf(":") + 1);
                } else {
                    index = fileRead.indexOf(":");
                }
//                //find second occurence of :, the number after that is the line number of the reported bug
//                int index = fileRead.indexOf(":", fileRead.indexOf(":") + 1);
                if (index > -1) {

                    //class path starts from the begining to just before the line number that a bug was reported
                    String classPath = fileRead.substring(0, index);
//                    int start_index = classPath.lastIndexOf(root);
//                    classPath = classPath.substring(start_index + root.length() + 1, index).replace("\\", "/");
                    System.out.println("ClassPath: " + classPath);

                    //starts after the class path, they are separated by the index containing :
                    int startIndex_bugLineNumber = index + 1;
                    int endIndex_bugLineNumber = fileRead.indexOf(':', startIndex_bugLineNumber);
                    System.out.println("startIndex:" + startIndex_bugLineNumber + " endIndex:" + endIndex_bugLineNumber);
                    String bugLineNumber = fileRead.substring(startIndex_bugLineNumber, endIndex_bugLineNumber);

                    System.out.println("errorLineNumber: " + bugLineNumber);

                    //message starts immediately after the line number
                    String errorMessage = fileRead.substring(endIndex_bugLineNumber + 1, fileRead.length()).trim();
                    System.out.println("Error Message: " + errorMessage);

                    //Bug Type
                    String bugType = getBugType(errorMessage);
                    System.out.println("Bug Type: " + getClassName(bugType));

                    //Class Name
                    System.out.println("className: " + getClassName(classPath));
                    String className = getClassName(classPath);

                    //sourceFile and ClassFile
                    String sourcePath = classPath;
                    String classFile = classPath;
                    System.out.println("--------------------------------------");
                    JlintReportedBug bugInstance = new JlintReportedBug(bugType, errorMessage, className, Integer.valueOf(bugLineNumber), sourcePath);
                    ReportedBugs.add(bugInstance);

                }

                fileRead = br.readLine();
            }

            // close file stream
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ReportedBugs;
    }

    private static String getClassName(String classPath) {
        // the first \ is an escape sequence character for the char to look for is '\'
        int start_index = classPath.lastIndexOf('/');
        String className = classPath.substring(start_index + 1, classPath.length()).trim();

        return className;

    }

    private static String getBugType(String errorMessage) {

        //Exeption Type
        BUG_TYPE bugType;
        if (errorMessage.contains("NULL") || errorMessage.contains("null")) {
            bugType = BUG_TYPE.NULL_POINTER_EXEPTION;
        } else {
            bugType = BUG_TYPE.ANOTHER_TYPE;
        }
        return bugType.toString();
    }


}

