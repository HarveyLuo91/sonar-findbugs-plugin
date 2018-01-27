package edu.bit.cs.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class csvParser {

    public static void main(String[] args) {
        ArrayList<fakeBug> fakebugs = parseFile();
        for (fakeBug fakebug : fakebugs) {
            fakebug.printdetails();
        }
    }
    public static ArrayList<fakeBug> parseFile(){
        ArrayList<fakeBug> fakebugs = new ArrayList<fakeBug>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(csvParser.class.getClassLoader().getResourceAsStream("file/file.txt")));
            String line = br.readLine();

            while(line !=null){
                System.out.println();
                String tokens[] = line.split(",");

                fakeBug fakebug = new fakeBug();
                fakebug.filename = tokens[0];

                fakebug.lineNumber = tokens[1].replaceFirst("L","");;
                fakebug.variable = tokens[2];
                if(tokens[3].trim().contains(" ")){
                    String[] tok = tokens[3].split( " ");

                    for (int i = 0; i < tok.length ; i++) {
                        fakebug.tags.add(tok[i]);
                    }
                }
                else{
                    fakebug.tags.add(tokens[3].trim());
                }


                //fakebug.printdetails();
                fakebugs.add(fakebug);
                line = br.readLine();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return fakebugs;
    }

}

class fakeBug{

    protected String filename;
    protected String lineNumber;
    protected String variable;
    protected ArrayList<String> tags =  new ArrayList<>();

    protected void printdetails(){
        System.out.println("\n");
        System.out.println("File Name: "+ this.filename);
        System.out.println("Line Number: "+ this.lineNumber);
        System.out.println("Variable: "+ this.variable);
        System.out.print("tags: ");
        for (String tag: this.tags) {
            System.out.print(tag + " ");
        }
    }

}
