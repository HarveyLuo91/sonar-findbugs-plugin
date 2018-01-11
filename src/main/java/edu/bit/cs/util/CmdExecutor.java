package edu.bit.cs.util;

import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;
import edu.bit.cs.infer.InferReportParser;
import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.jlint.JlintReportParser;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CmdExecutor {
    public static Collection<? extends ReportedBugInfo> exeCmd(String projectPath, ReportedInfoProcessor processor) {
        if (processor instanceof InferReportParser) {
            BufferedReader br;
            try {
                File dir = new File(projectPath);
                Process p = Runtime.getRuntime().exec(genCmdStr("INFER", null), null, dir);
                p.waitFor();
                String reporterPath = projectPath + "/infer-out/report.json";
                System.out.println("reporterPath:" + reporterPath);
                br = new BufferedReader(new FileReader(new File(reporterPath)));
                return processor.getReportedBugs(br);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Collections.EMPTY_LIST;
        } else if (processor instanceof JlintReportParser) {
            BufferedReader br = null;
            try {
                Process p = Runtime.getRuntime().exec(genCmdStr("JLINT", projectPath));
                br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
                return processor.getReportedBugs(br);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    public static String genCmdStr(String toolType, String projectPath) {
        switch (toolType) {
            case "JLINT": {
                return "jlint " + projectPath;
            }
            case "INFER": {
                return "infer -- mvn clean package";
            }
            default: {
                return "";
            }
        }
    }

   // public static void main(String[] args) {
//        String commandStr = "ping www.taobao.com";
//        CmdExecutor cmdExecutor = new CmdExecutor();
//        JlintReportParser jlintReportParser = new JlintReportParser();
//        //String commandStr = "ipconfig";
//
//        cmdExecutor.exeCmd(commandStr, jlintReportParser);

        //String cmd = CmdExecutor.genCmdStr("JLINT", "C:\\Users\\Luo\\program\\testcases-for-npe\\killbugs-testcases-src\\target");
        //Collection<? extends ReportedBugInfo> jlintReportedBugs = CmdExecutor.exeCmd(cmd, new JlintReportParser());
//            List<JlintReportedBug> jlintReportedBugs = JlintReportParser.get_Reported_jlint_Bugs();

        //System.out.println("******************************Jlint size:" + jlintReportedBugs.size());
        //for (ReportedBugInfo bugInstance : jlintReportedBugs) {
          //  if (bugInstance.getBugType().equals(BUG_TYPE.ANOTHER_TYPE.toString())) {
            //    continue;
            //}
//                String className = bugInstance.getClassName();
            //String sourceFile = bugInstance.getSourcePath();
//                String longMessage = bugInstance.getMessage();
            //int line = bugInstance.getBugLineNumber();
            //System.out.println("-------------------jlint sourceFile+line:" + sourceFile + line);
            //List bugInstances;

        //}
//        System.out.println("**********************jlint map size:" + bugs.size());

//    }
}
