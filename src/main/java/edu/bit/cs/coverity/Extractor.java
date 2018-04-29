package edu.bit.cs.coverity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Extractor {

    static Set<String> files = Sets.newHashSet();

    public static void main(String[] args) {

        String path = "C:\\Users\\Luo\\program\\benchmark-for-npe\\src\\main\\java";
        getFile(path, "");
        System.out.println(files);
        System.out.println(files.size());



//        List<TestCaseModel> models = Lists.newArrayList();
//        for (String file : files) {
//            if (file.endsWith("T") || file.endsWith("F")) {
//                models.addAll(processTest(file));
//            }
//        }
//        System.out.println(models.size());
//        System.out.println(models.toString());

//        Location annotation = extra.luo.abstract_inferface_extends.Test001_1F.class.getAnnotation(Location.class);
//        System.out.println(Test001_1F.class.getName());
//        if (annotation != null) {
//            System.out.println(annotation.line());
//            System.out.println(annotation.var());
//        }
//        for (Annotation annotation : Test001_1F.class.getAnnotations()) {
////            System.out.println("get");
//            System.out.println(annotation.);
//        }

    }



    private static void getFile(String path, String packageName) {
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            String cur_packageName = packageName;
            if (array[i].isFile())//如果是文件
            {
                String fileName = packageName + array[i].getName();
                files.add(fileName.substring(0, fileName.length() - 5));

            } else if (array[i].isDirectory())//如果是文件夹
            {
                cur_packageName = cur_packageName + array[i].getName() + ".";
                getFile(array[i].getPath(), cur_packageName);
            }
        }
    }

}
