package edu.bit.cs.executor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.Constant;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.findbugs.findBugReportParser;
import edu.bit.cs.jlint.JlintReportParser;
import edu.bit.cs.util.CmdExecutor;
import edu.bit.cs.util.ToolCollection;
import org.sonar.plugins.findbugs.FindbugsSensor;
import org.sonar.plugins.findbugs.ReportedBug;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

public class BugDetector {

    public static void executor(String path){
//    public static void main(String[] args){
        Map<String, List<ReportedBugInfo>> bugs = Maps.newHashMap();
        Constant.ROOT = path.split("/")[path.split("/").length-2];
        //FindBugs
        Collection<? extends ReportedBugInfo> findBugsReportedBugs = CmdExecutor.exeCmd(path, new findBugReportParser());
        System.out.println("******************************findbugs size:" + findBugsReportedBugs.size());
        for (ReportedBugInfo bugInstance : findBugsReportedBugs) {
            List bugInstances;
            if (!bugs.containsKey(bugInstance.getUID())) {
                bugInstances = Lists.newArrayList();
            } else {
                bugInstances = bugs.get(bugInstance.getUID());
            }
            bugInstances.add(bugInstance);
            bugs.put(bugInstance.getUID(), bugInstances);
        }

        System.out.println("**********************findbugs map size:" + bugs.size());

        //JLint
        Collection<? extends  ReportedBugInfo> JlintReportedBugs = CmdExecutor.exeCmd(path+"/target", new JlintReportParser());
        System.out.println("******************************Jlint Bugs size:" + findBugsReportedBugs.size());
        for (ReportedBugInfo bugInstance : JlintReportedBugs) {
            List bugInstances;
            if (!bugs.containsKey(bugInstance.getUID())) {
                bugInstances = Lists.newArrayList();
            } else {
                bugInstances = bugs.get(bugInstance.getUID());
            }
            bugInstances.add(bugInstance);
            bugs.put(bugInstance.getUID(), bugInstances);
        }
        System.out.println("**********************Jlint map size:" + bugs.size());
        JSONObject bugJSon = new JSONObject();
        Iterator bugIt = bugs.entrySet().iterator();
        int num = 0;
        while(bugIt.hasNext()){
            Map.Entry entry = (Map.Entry)bugIt.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            List<ReportedBugInfo>bl = (List< ReportedBugInfo>)value;
            String message = "";
            String bugClass = "";
            String bugType = "";
            String bugPath = "";
            StringBuilder tools = new StringBuilder();
            StringBuilder label = new StringBuilder();
            int linenumber = 0;
            for(ReportedBugInfo b:bl){
                if(b.getToolName().equals(ToolCollection.FINDBUGS)){
                    if(tools.toString().contains("0"))
                        continue;
                    tools.append("0");
                    label.append("[FindBugs]");
                    message = b.getBugMessage();
                    linenumber = b.getBugLineNumber();
                    bugClass = b.getClassName();
                    bugType = b.getBugType().toString();
                    bugPath = b.getSourcePath();
                }else{
                    if(tools.toString().contains("1"))
                        continue;
                    tools.append("1");
                    label.append("[Jlint]");
                    if(message.length()==0){
                        message = b.getBugMessage();
                        linenumber = b.getBugLineNumber();
                        bugClass = b.getClassName();
                        bugType = b.getBugType().toString();
                        bugPath = b.getSourcePath();
                    }
                }
                JSONObject node = new JSONObject();
                node.put("tools", label);
                node.put("message", message);
                node.put("line_number", linenumber);
                node.put("bug_type", bugType);
                node.put("bug_path", bugPath);
                node.put("bug_class", bugClass);
                bugJSon.put("bug"+String.valueOf(num), node);
                num += 1;
            }
        }
        try {
            File writename = new File(path+"/detect_res.txt");
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(bugJSon.toJSONString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
