package edu.bit.cs.util;

import java.io.File;

public class FileCounter {
    static int count=0;
    public static void main(String[] args) {
        getFile("C:\\Users\\Luo\\program\\benchmark-generation-clear\\src\\main\\java\\testcases");
        System.out.println("共有"+count+"个文件");
    }

    public static void getFile(String filepath) {
        //com.bizwink.cms.util.Convert con = new com.bizwink.cms.util.Convert();
        File file = new File(filepath);
        File[] listfile = file.listFiles();
        for (int i = 0; i < listfile.length; i++) {
            //System.out.println("****** = "+listfile[i].getPath().toString());
            if (!listfile[i].isDirectory()) {
                //com.bizwink.cms.util.Convert con = new com.bizwink.cms.util.Convert();
                String temp=listfile[i].toString().substring(7,listfile[i].toString().length()) ;
                System.out.println("temp=="+temp);
                //con.convertFile(listfile[i].toString(), "D:\\newtest"+temp, 0, 3);
                count++;
                System.out.println("文件"+count+"---path=" + listfile[i]);
            } else {
                getFile(listfile[i].toString());
            }
        }
    }
}