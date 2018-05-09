package edu.bit.cs.assessment;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.bit.cs.BUG_TYPE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JsonParser {

    public static List<TestCaseModel> getTestCases() {
        List<TestCaseModel> graphs = Lists.newArrayList();
        BufferedReader br = new BufferedReader(new InputStreamReader(JsonParser.class.getClassLoader().getResourceAsStream("file/benchmark-for-aminu-testcases.json")));
        try {
            //Reading the json file
            StringBuilder json = new StringBuilder("");
            try {
                String line = br.readLine();
                while (line != null) {
                    json.append(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();

            graphs = gson.fromJson(json.toString(), new TypeToken<List<TestCaseModel>>() {
            }.getType());

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return graphs;
    }

    public static String getUID(String file, int line) {
        return file + "-" + line + "-" + BUG_TYPE.NULL_POINTER_EXEPTION;
    }
}
