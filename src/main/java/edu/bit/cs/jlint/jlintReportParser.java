package edu.bit.cs.jlint;


import java.util.ArrayList;
import java.io.*;

public class jlintReportParser {


    //this is for testing
    public static void main(String[] args) {
        ArrayList<jlintReportedBug> reportedBugs = jlintReportParser.get_Reported_jlint_Bugs();
        for (jlintReportedBug bug: reportedBugs) {
            System.out.println(bug.toString());
        }
    }

    private static String ReportFilePath;

    public static ArrayList<jlintReportedBug> ReportedBugs = new ArrayList();


    public static ArrayList<jlintReportedBug> get_Reported_jlint_Bugs(){

        try{
            // create a Buffered Reader object instance with a FileReader, remeber to change the way the file is read as a resource
            BufferedReader br = new BufferedReader(new InputStreamReader(jlintReportParser.class.getClassLoader().getResourceAsStream("file/jlint_npe_repoter.txt")));
            // read the first line from the text file
            String fileRead = br.readLine();
            // loop until all lines are read
            while (fileRead != null)
            {

                //find second occurence of :, the number after that is the line number of the reported bug
                int index = fileRead.indexOf(":", fileRead.indexOf(":") + 1);
                if(index > -1){

                //class path starts from the begining to just before the line number that a bug was reported
                String classPath = fileRead.substring(0,index);
                int start_index = classPath.lastIndexOf("\\java");
                classPath = classPath.substring(start_index+1,index);
                System.out.println("ClassPath: " + classPath);

                //starts after the class path, they are separated by the index containing :
                int startIndex_bugLineNumber = index + 1;
                int endIndex_bugLineNumber = fileRead.indexOf(':',startIndex_bugLineNumber);
                String bugLineNumber = fileRead.substring(startIndex_bugLineNumber,endIndex_bugLineNumber);

                System.out.println("errorLineNumber: " + bugLineNumber);

                //message starts immediately after the line number
                String errorMessage = fileRead.substring(endIndex_bugLineNumber+ 1,fileRead.length()).trim();
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
                jlintReportedBug bugInstance = new jlintReportedBug(bugType, errorMessage, className,  Integer.valueOf(bugLineNumber),  sourcePath);
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

    private static String getClassName(String classPath){
        // the first \ is an escape sequence character for the char to look for is '\'
        int start_index = classPath.lastIndexOf('\\');
        String className = classPath.substring(start_index + 1, classPath.length()).trim();

        return className;

    }
    private static String getBugType(String errorMessage){

        //Exeption Type
        BUG_TYPE bugType;
        if(errorMessage.contains("NULL") || errorMessage.contains("null")){
            bugType = BUG_TYPE.NULL_POINTER_EXEPTION;
        }else
        {
            bugType = BUG_TYPE.ANOTHER_TYPE;
        }
        return bugType.toString();
    }



}

