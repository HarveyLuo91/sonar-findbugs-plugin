package edu.bit.cs.util;

import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;
import edu.bit.cs.jlint.BUG_TYPE;
import edu.bit.cs.jlint.JlintReportParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CmdExecutor {
    public static Collection<? extends ReportedBugInfo> exeCmd(String commandStr, ReportedInfoProcessor processor) {
//        List reportedBugs = Lists.newArrayList();
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
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
        return Collections.EMPTY_LIST;
    }

    public static String genCmdStr(String toolType, String projectPath) {
        switch (toolType) {
            case "JLINT": {
                return "jlint " + projectPath;
            }
            case "INFER": {
                return null;
            }
            default: {
                return "";
            }
        }
    }

    public static void main(String[] args) {
//        String commandStr = "ping www.taobao.com";
//        CmdExecutor cmdExecutor = new CmdExecutor();
//        JlintReportParser jlintReportParser = new JlintReportParser();
//        //String commandStr = "ipconfig";
//
//        cmdExecutor.exeCmd(commandStr, jlintReportParser);

        String cmd = CmdExecutor.genCmdStr("JLINT", "C:\\Users\\Luo\\program\\testcases-for-npe\\killbugs-testcases-src\\target");
        Collection<? extends ReportedBugInfo> jlintReportedBugs = CmdExecutor.exeCmd(cmd, new JlintReportParser("killbugs-testcases-src"));
//            List<JlintReportedBug> jlintReportedBugs = JlintReportParser.get_Reported_jlint_Bugs();

        System.out.println("******************************Jlint size:" + jlintReportedBugs.size());
        for (ReportedBugInfo bugInstance : jlintReportedBugs) {
            if (bugInstance.getBugType().equals(BUG_TYPE.ANOTHER_TYPE.toString())) {
                continue;
            }
//                String className = bugInstance.getClassName();
            String sourceFile = bugInstance.getSourcePath();
//                String longMessage = bugInstance.getMessage();
            int line = bugInstance.getBugLineNumber();
            System.out.println("-------------------jlint sourceFile+line:" + sourceFile + line);
            List bugInstances;

        }
//        System.out.println("**********************jlint map size:" + bugs.size());

    }
}
