package edu.bit.cs.coverity.Xml2Json;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Parser {
    public List<Error> getReportedBugs(BufferedReader br) {
//        List<InferReportedBugFromJson> reportedBugs = Lists.newArrayList();
        Root xml2json = null;
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
            xml2json = gson.fromJson(json.toString(), Root.class);
            return xml2json.getCoverity().getError();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return null;
    }
}
