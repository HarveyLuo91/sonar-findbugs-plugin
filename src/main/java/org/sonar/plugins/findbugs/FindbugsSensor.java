/*
 * SonarQube Findbugs Plugin
 * Copyright (C) 2012 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.findbugs;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.assessment.CsvParser;
import edu.bit.cs.assessment.Result;
import edu.bit.cs.assessment.TestCaseModel;
import edu.bit.cs.infer.InferReportParser;
import edu.bit.cs.jlint.JlintReportParser;
import edu.bit.cs.util.CmdExecutor;
import edu.bit.cs.util.ToolCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.plugins.findbugs.resource.ByteCodeResourceLocator;
import org.sonar.plugins.findbugs.resource.ClassMetadataLoadingException;
import org.sonar.plugins.findbugs.resource.SmapParser;
import org.sonar.plugins.findbugs.rules.FbContribRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsJspRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindbugsRulesDefinition;
import org.sonar.plugins.java.api.JavaResourceLocator;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FindbugsSensor implements Sensor {

    private static final Logger LOG = LoggerFactory.getLogger(FindbugsSensor.class);

    private static final String[] REPOS = {FindbugsRulesDefinition.REPOSITORY_KEY, FbContribRulesDefinition.REPOSITORY_KEY,
            FindSecurityBugsRulesDefinition.REPOSITORY_KEY, FindSecurityBugsJspRulesDefinition.REPOSITORY_KEY
    };

    public static String ROOT = "";

    private List<String> repositories = Lists.newArrayList();

    private RulesProfile profile;
    private ActiveRules ruleFinder;
    private FindbugsExecutor executor;
    private final JavaResourceLocator javaResourceLocator;
    private final ByteCodeResourceLocator byteCodeResourceLocator;
    private final FileSystem fs;
    private final SensorContext sensorContext;
    private PrintWriter classMappingWriter;

    public FindbugsSensor(RulesProfile profile, ActiveRules ruleFinder, SensorContext sensorContext,
                          FindbugsExecutor executor, JavaResourceLocator javaResourceLocator, FileSystem fs, ByteCodeResourceLocator byteCodeResourceLocator) {
        this.profile = profile;
        this.ruleFinder = ruleFinder;
        this.sensorContext = sensorContext;
        this.executor = executor;
        this.javaResourceLocator = javaResourceLocator;
        this.byteCodeResourceLocator = byteCodeResourceLocator;
        this.fs = fs;
        registerRepositories(REPOS);
        File classMappingFile = new File(fs.workDir(), "class-mapping.csv");
        try {
            this.classMappingWriter = new PrintWriter(new FileOutputStream(classMappingFile));
        } catch (FileNotFoundException ignored) {
        }
    }

    private void registerRepositories(String... repos) {
        Collections.addAll(repositories, repos);
    }

    private boolean hasActiveRules(String repoSubstring) {
        return profile.getActiveRules().stream().anyMatch(activeRule ->
                activeRule.getRepositoryKey().contains(repoSubstring)
        );
    }

    private List<String> getRepositories() {
        return repositories;
    }

    private boolean hasActiveFindbugsRules() {
        return hasActiveRules("findbugs");
    }

    private boolean hasActiveFbContribRules() {
        return hasActiveRules("fb-contrib");
    }

    private boolean hasActiveFindSecBugsRules() {
        return hasActiveRules("findsecbugs");
    }

    @Override
    public void execute(SensorContext context) {

        ROOT = context.settings().getString("sonar.root");

        if (!hasActiveFindbugsRules() && !hasActiveFbContribRules() && !hasActiveFindSecBugsRules()) {
            return;
        }

        ActiveRule npe_killbugs_rule = null;
        for (String repoKey : getRepositories()) {

            npe_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "NULL_POINTER_EXEPTION");
            if (npe_killbugs_rule != null) {
                break;
            }
        }
        if (npe_killbugs_rule == null) {
            System.out.println("-----------------npe_killbugs_rule is null!");
            return;
        }
        ActiveRule RL_killbugs_rule = null; //Resource Leak
        for (String repoKey : getRepositories()) {
            RL_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "RESOURCE_LEAK");
            if (RL_killbugs_rule != null) {
                break;
            }
        }
        if (RL_killbugs_rule == null) {
            System.out.println("-----------------RL_killbugs_rule is null!");
            return;
        }
        ActiveRule BAC_killbugs_rule = null; //BROKEN_ACCESS_CONTROL
        for (String repoKey : getRepositories()) {
            BAC_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "BROKEN_ACCESS_CONTROL");
            if (BAC_killbugs_rule != null) {
                break;
            }
        }
        if (BAC_killbugs_rule == null) {
            System.out.println("-----------------BAC_killbugs_rule is null!");
            return;
        }

        ActiveRule BA_killbugs_rule = null; //BROKEN_AUTHENTICATION
        for (String repoKey : getRepositories()) {
            BA_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "BROKEN_AUTHENTICATION");
            if (BA_killbugs_rule != null) {
                break;
            }
        }
        if (BA_killbugs_rule == null) {
            System.out.println("-----------------BA_killbugs_rule is null!");
            return;
        }

        ActiveRule CSS_killbugs_rule = null; //CROSS_SITE_SCRIPTING
        for (String repoKey : getRepositories()) {
            CSS_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "CROSS_SITE_SCRIPTING");
            if (CSS_killbugs_rule != null) {
                break;
            }
        }
        if (CSS_killbugs_rule == null) {
            System.out.println("-----------------CSS_killbugs_rule is null!");
            return;
        }

        ActiveRule Inj_killbugs_rule = null; //INJECTION
        for (String repoKey : getRepositories()) {
            Inj_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "INJECTION");
            if (Inj_killbugs_rule != null) {
                break;
            }
        }
        if (Inj_killbugs_rule == null) {
            System.out.println("-----------------Inj_killbugs_rule is null!");
            return;
        }

        ActiveRule ID_killbugs_rule = null; //INSECURE_DESERIALIZATION
        for (String repoKey : getRepositories()) {
            ID_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "INSECURE_DESERIALIZATION");
            if (ID_killbugs_rule != null) {
                break;
            }
        }
        if (ID_killbugs_rule == null) {
            System.out.println("-----------------RL_killbugs_rule is null!");
            return;
        }

        ActiveRule SM_killbugs_rule = null; //SAFETY_MISCONFIGURATION
        for (String repoKey : getRepositories()) {
            SM_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "SAFETY_MISCONFIGURATION");
            if (SM_killbugs_rule != null) {
                break;
            }
        }
        if (SM_killbugs_rule == null) {
            System.out.println("-----------------SM_killbugs_rule is null!");
            return;
        }

        ActiveRule SDE_killbugs_rule = null; //SENSITIVE_DATA_EXPOSURE
        for (String repoKey : getRepositories()) {
            SDE_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "SENSITIVE_DATA_EXPOSURE");
            if (SDE_killbugs_rule != null) {
                break;
            }
        }
        if (SDE_killbugs_rule == null) {
            System.out.println("-----------------SDE_killbugs_rule is null!");
            return;
        }

        ActiveRule XEE_killbugs_rule = null; //XML_EXTERNAL_ENTITIES
        for (String repoKey : getRepositories()) {
            XEE_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "XML_EXTERNAL_ENTITIES");
            if (XEE_killbugs_rule != null) {
                break;
            }
        }
        if (XEE_killbugs_rule == null) {
            System.out.println("-----------------XML_EXTERNAL_ENTITIES is null!");
            return;
        }

        Collection<ReportedBug> collection = executor.execute(hasActiveFbContribRules(), hasActiveFindSecBugsRules());

        try {

            Map<String, List<ReportedBugInfo>> bugs = Maps.newHashMap();

            String absolutePath = context.fileSystem().baseDir().getAbsolutePath();

            File binaries = new File(absolutePath, context.settings().getString("sonar.binaries"));
            System.out.println("binaries:" + binaries);

            //execute Jlint and get reported bugs
            Collection<? extends ReportedBugInfo> jlintReportedBugs = CmdExecutor.exeCmd(binaries.getAbsolutePath(), new JlintReportParser());
            System.out.println("******************************Jlint size:" + jlintReportedBugs.size());
            //loop through the reported bugs
            for (ReportedBugInfo bugInstance : jlintReportedBugs) {
                //if the bug type is a type we havent checked for, continue
                if (bugInstance.getBugType().equals(BUG_TYPE.ANOTHER_TYPE.toString())) {
                    continue;
                }
                //Normalize the sourse path to the root folder of project
//                System.out.println("-------------------jlint uid:" + bugInstance.getUID());
                List bugInstances;
                //if we don't have current bug as a key in the bugs map, create new instance of buginstances
                if (!bugs.containsKey(bugInstance.getUID())) {
                    bugInstances = Lists.newArrayList();
                } else {
                    //if we already have bug as key, get the bugInstance
                    bugInstances = bugs.get(bugInstance.getUID());
                }
                //add new bugInstance into bug instances, this is possible because infer,jlint and findbugs can all find a bug at the same position
                bugInstances.add(bugInstance);

                bugs.put(bugInstance.getUID(), bugInstances);
            }

            System.out.println("**********************jlint map size:" + bugs.size());


            //Infer
            System.out.println("Infer dir:" + absolutePath);
            Collection<? extends ReportedBugInfo> inferReportedBugs = CmdExecutor.exeCmd(absolutePath, new InferReportParser());
//            BufferedReader br = new BufferedReader(new InputStreamReader(FindbugsSensor.class.getClassLoader().getResourceAsStream("file/report.json")));
//            Collection<? extends ReportedBugInfo> inferReportedBugs = new InferReportParser().getReportedBugs(br);

            System.out.println("***********************Infer size:" + inferReportedBugs.size());
            int jlint_infer_intersection = 0;
            for (ReportedBugInfo bugInstance : inferReportedBugs) {
                if (bugInstance.getBugType().equals(BUG_TYPE.ANOTHER_TYPE)) {
                    continue;
                }
//                System.out.println("INFER:" + bugInstance.toString());
//                System.out.println("-------------------Infer uid:" + bugInstance.getUID());
                List bugInstances;
                if (!bugs.containsKey(bugInstance.getUID())) {
                    bugInstances = Lists.newArrayList();
                } else {
                    jlint_infer_intersection = jlint_infer_intersection + 1;
                    bugInstances = bugs.get(bugInstance.getUID());
                }
                bugInstances.add(bugInstance);
                bugs.put(bugInstance.getUID(), bugInstances);
            }
            System.out.println("**********************Infer map size:" + bugs.size() + "--jlint_infer_intersection:" + jlint_infer_intersection);

//            for (String str : bugs.keySet()) {
//                System.out.println(str);
//                for (ReportedBugInfo bug : bugs.get(str)) {
//                    System.out.println("tool name: " + bug.getToolName());
//                }
//            }

            //for assessment
            Map<String, Result> interSection = Maps.newHashMap();
            for (int index = 0; index < ToolCollection.getSize(); index++) {
                interSection.put(ToolCollection.getTools(index), new Result(ToolCollection.getTools(index)));
            }

            //findbugs
            for (ReportedBug bugInstance : collection) {
                try {
                    if (bugInstance.getBugType().equals(BUG_TYPE.ANOTHER_TYPE)) {
                        continue;
                    }

                    ActiveRule rule = null;
                    for (String repoKey : getRepositories()) {
//                        System.out.println("\n---------------------type:" + bugInstance.getBugType().name() + "\nmessage:" + bugInstance.getMessage());
                        rule = ruleFinder.findByInternalKey(repoKey, bugInstance.getBugType().name());
                        if (rule != null) {
                            break;
                        }
                    }
                    if (rule == null) {
                        // ignore violations from report, if npe_killbugs_rule not activated in Sonar
                        System.out.println("----------------Findbugs npe_killbugs_rule '{}' is not active in Sonar." + bugInstance.getBugType().name());
                        LOG.warn("Findbugs npe_killbugs_rule '{}' is not active in Sonar.", bugInstance.getBugType().name());
                        continue;
                    }

                    String className = bugInstance.getClassName();
                    String sourceFile = bugInstance.getSourceFile();
                    StringBuilder longMessage = new StringBuilder(bugInstance.getMessage());
                    int line = bugInstance.getStartLine();

//                    System.out.println("findbugs sourcefile:" + sourceFile);
                    //Regular Java class mapped to their original .java
                    InputFile resource = byteCodeResourceLocator.findSourceFile(sourceFile, this.fs);
                    if (resource != null) {

                        String tools = processBugs(bugs, interSection, bugInstance);
                        longMessage.insert(0, tools + "- ");
                        insertIssue(rule, resource, line, longMessage.toString(), bugInstance);
                        continue;
                    }

                    //Locate the original class file
                    File classFile = findOriginalClassForBug(bugInstance.getClassFile());
                    if (classFile == null) {
                        LOG.warn("Unable to find the class " + bugInstance.getClassName());
                        continue;
                    }


                    //More advanced mapping if the original source is not Java files
                    if (classFile != null) {
                        //Attempt to load SMAP debug metadata
                        try {
                            SmapParser.SmapLocation location = byteCodeResourceLocator.extractSmapLocation(className, line, classFile);
                            if (location != null) {
                                if (!location.isPrimaryFile) { //Avoid reporting issue in double when a source file was include inline
                                    continue;
                                }

                                //SMAP was found
                                resource = byteCodeResourceLocator.findSourceFile(location.fileInfo.path, fs);
                                if (resource != null) {
                                    String tools = processBugs(bugs, interSection, bugInstance);
                                    longMessage.insert(0, tools + "- ");
                                    insertIssue(rule, resource, line, longMessage.toString(), bugInstance);
                                    continue;
                                }
                            } else {
                                //SMAP was not found or unparsable.. The orgininal source file will be guess based on the class name
                                resource = byteCodeResourceLocator.findTemplateFile(className, this.fs);
                                if (resource != null) {
                                    String tools = processBugs(bugs, interSection, bugInstance);
                                    longMessage.insert(0, tools + "- ");
                                    insertIssue(rule, resource, line, longMessage.toString(), bugInstance);
                                    continue;
                                }
                            }
                        } catch (ClassMetadataLoadingException e) {
                            LOG.warn("Failed to load the class file metadata", e);
                        }
                    }

                    LOG.warn("The class '" + className + "' could not be matched to its original source file. It might be a dynamically generated class.");
                } catch (Exception e) {
                    String bugInstanceDebug = String.format("[BugInstance type=%s, class=%s, line=%s]", bugInstance.getBugType().name(), bugInstance.getClassName(), bugInstance.getStartLine());
                    LOG.warn("An error occurs while processing the bug instance " + bugInstanceDebug, e);
                    //Continue to the bug without aborting the report
                }
            }


            System.out.println("----------------Bugs Keyset Size:" + bugs.keySet().size());
            //for test
            bugs.remove(null);
//            for (String key : bugs.keySet()) {
//                System.out.println(key);
//            }

            for (String bugInstanceKey : bugs.keySet()) {

                String[] splitKeyInstance = bugInstanceKey.split("-");
//                System.out.println("FileName:" + splitKeyInstance[0] + "\nLine Number:" + splitKeyInstance[1] + "\nBugTyp   e:" + splitKeyInstance[2]);

                if (splitKeyInstance[2].equals(BUG_TYPE.NULL_POINTER_EXEPTION.name())) {
                    createIssue(npe_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.RESOURCE_LEAK.name())) {
                    createIssue(RL_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.BROKEN_ACCESS_CONTROL.name())) {
                    createIssue(BAC_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.BROKEN_AUTHENTICATION.name())) {
                    createIssue(BA_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.CROSS_SITE_SCRIPTING.name())) {
                    createIssue(CSS_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.INJECTION.name())) {
                    createIssue(Inj_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.INSECURE_DESERIALIZATION.name())) {
                    createIssue(ID_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.SAFETY_MISCONFIGURATION.name())) {
                    createIssue(SM_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.SENSITIVE_DATA_EXPOSURE.name())) {
                    createIssue(SDE_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.XML_EXTERNAL_ENTITIES.name())) {
                    createIssue(XEE_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else {

                }
            }

            //for assessment
            Result findbugs = new Result("FINDBUGS");
            Result jlint = new Result("JLINT");
            Result infer = new Result("INFER");
            Result findbugs_jlint = new Result("FINDBUGS+JLINT");
            Result findbugs_infer = new Result("FINDBUGS+INFER");
            Result jlint_infer = new Result("JLINT+INFER");


            for (int index = 0; index < ToolCollection.getSize(); index++) {

                Result result = interSection.get(ToolCollection.getTools(index));

                if ((index & ToolCollection.FINDBUGS.getId()) != 0) {
                    findbugs.getTp().putAll(result.getTp());
                    findbugs.getFp().putAll(result.getFp());
                }
                if ((index & ToolCollection.JLINT.getId()) != 0) {
                    jlint.getTp().putAll(result.getTp());
                    jlint.getFp().putAll(result.getFp());
                }
                if ((index & ToolCollection.INFER.getId()) != 0) {
                    infer.getTp().putAll(result.getTp());
                    infer.getFp().putAll(result.getFp());
                }

                System.out.println(result);
            }

            System.out.println(findbugs);
            System.out.println(jlint);
            System.out.println(infer);

            for (String uid : findbugs.getTp().keySet()) {
                TestCaseModel test = CsvParser.F_BUGS.get(uid);
                test.setFindbugs(1);
                CsvParser.F_BUGS.put(uid, test);
            }
            for (String uid : jlint.getTp().keySet()) {
                TestCaseModel test = CsvParser.F_BUGS.get(uid);
                test.setJlint(1);
                CsvParser.F_BUGS.put(uid, test);
            }
            for (String uid : infer.getTp().keySet()) {
                TestCaseModel test = CsvParser.F_BUGS.get(uid);
                test.setInfer(1);
                CsvParser.F_BUGS.put(uid, test);
            }

            for (String uid : findbugs.getFp().keySet()) {
                TestCaseModel test = CsvParser.T_BUGS.get(uid);
                if (test != null) {
                    test.setFindbugs(0);
                    CsvParser.T_BUGS.put(uid, test);
                }
            }
            for (String uid : jlint.getFp().keySet()) {
                TestCaseModel test = CsvParser.T_BUGS.get(uid);
                if (test != null) {
                    test.setJlint(0);
                    CsvParser.T_BUGS.put(uid, test);
                }
            }
            for (String uid : infer.getFp().keySet()) {
                TestCaseModel test = CsvParser.T_BUGS.get(uid);
                if (test != null) {
                    test.setInfer(0);
                    CsvParser.T_BUGS.put(uid, test);
                }
            }

            List<TestCaseModel> testCaseModels = Lists.newArrayList();
            testCaseModels.addAll(CsvParser.F_BUGS.values());
            testCaseModels.addAll(CsvParser.T_BUGS.values());

            System.out.println("-----json size:" + testCaseModels.size());
//            System.out.println(JSON.toJSONString(testCaseModels));

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(new File(binaries.getAbsolutePath() + "/testcases.json"));
                out.write(JSON.toJSONString(testCaseModels).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            List<TestCaseModel> wantedModels = Lists.newArrayList();
            for (TestCaseModel test : CsvParser.F_BUGS.values()) {
                if ((test.getFindbugs() + test.getInfer() + test.getJlint() == 3) || (test.getFindbugs() + test.getInfer() + test.getJlint() == 0)) {
                    continue;
                }
                wantedModels.add(test);
            }
            for (TestCaseModel test : CsvParser.T_BUGS.values()) {
                if ((test.getFindbugs() + test.getInfer() + test.getJlint() == 3) || (test.getFindbugs() + test.getInfer() + test.getJlint() == 0)) {
                    continue;
                }
                wantedModels.add(test);
            }
            System.out.println("-----wanted json size:" + wantedModels.size());

            out = null;
            try {
                out = new FileOutputStream(new File(binaries.getAbsolutePath() + "/wantedtestcases.json"));
                out.write(JSON.toJSONString(wantedModels).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            System.out.println(JSON.toJSONString(wantedModels));
//            findbugs.countTags();
//            System.out.println("FindBugs Tag:");
//            findbugs.printTags();
//            System.out.println("");
//            jlint.countTags();
//            System.out.println("Jlint Tag:");
//            jlint.printTags();
//            System.out.println("");
//            infer.countTags();
//            System.out.println("Infer Tag:");
//            infer.printTags();

//            for (String tag : CsvParser.TAGS.keySet()) {
//                System.out.println(tag);
//                System.out.print("total:" + CsvParser.TAGS.get(tag) + " ");
//                System.out.print("findbugs:" + findbugs.getTags().get(tag) + " ");
//                System.out.print("jlint:" + jlint.getTags().get(tag) + " ");
//                System.out.println("infer:" + infer.getTags().get(tag));
//            }

        } finally {
            classMappingWriter.flush();
            classMappingWriter.close();
        }

    }


    private void createIssue(ActiveRule rule, Map<String, List<ReportedBugInfo>> bugs, String bugInstanceKey, Map<String, Result> interSection) {
        String sourceFile = "";
        StringBuilder longMessage = new StringBuilder("");
        int index = 0;
        int line = 0;

        for (ReportedBugInfo bug : bugs.get(bugInstanceKey)) {
//            System.out.println("-------------------" + bug.getToolName() + "\nsourcefile:" + bug.getSourcePath());
            index = ToolCollection.addTool(index, bug.getToolName());
        }

        String tools = ToolCollection.getTools(index);

        //for assessment
        Result result = interSection.get(tools);
        result.judge(bugs.get(bugInstanceKey));
//        reportedBugInfos.addAll(bugs.get(bugInstanceKey));
//        interSection.put(tools, value + 1);
        longMessage.append(tools);

        for (ReportedBugInfo bug : bugs.get(bugInstanceKey)) {
            sourceFile = bug.getSourcePath();
            line = bug.getBugLineNumber();
            longMessage.append("- " + bug.getBugMessage());
            break;
        }

        InputFile resource = byteCodeResourceLocator.findSourceFile(sourceFile, this.fs);
        if (resource != null) {
            insertIssue(rule, resource, line, longMessage.toString());
        } else {
//            System.out.println("-------------------resource is null");
        }
    }

    private String processBugs(Map<String, List<ReportedBugInfo>> bugs, Map<String, Result> interSection, ReportedBugInfo bug) {
        int index = 1;

        if (bugs.containsKey(bug.getUID())) {
            for (ReportedBugInfo bugInfo : bugs.get(bug.getUID())) {
//                System.out.println(bugInfo.getToolName());
                index = ToolCollection.addTool(index, bugInfo.getToolName());
            }
            index = ToolCollection.addTool(index, ToolCollection.FINDBUGS);

            //for assessment
//            System.out.println("result interSection:" + ToolCollection.getTools(index));
            Result result = interSection.get(ToolCollection.getTools(index));
            result.judge(bugs.get(bug.getUID()));

            bugs.remove(bug.getUID());
        }
        String tools = ToolCollection.getTools(index);

        //for assessment
        Result result = interSection.get(tools);
        result.judge(bug);

//        reportedBugInfos.addAll(bugs.get(key));
//        interSection.put(tools, value + 1);
        return tools;
    }


    protected void insertIssue(ActiveRule rule, InputFile resource, int line, String message) {
        NewIssue newIssue = sensorContext.newIssue().forRule(rule.ruleKey());

        NewIssueLocation location = newIssue.newLocation()
                .on(resource)
                .at(resource.selectLine(line > 0 ? line : 1))
                .message(message);

        newIssue.at(location); //Primary location
        newIssue.save();
    }

    private void insertIssue(ActiveRule rule, InputFile resource, int line, String message, ReportedBug bugInstance) {

        insertIssue(rule, resource, line, message);

        writeDebugMappingToFile(bugInstance.getClassName(), bugInstance.getStartLine(), resource.relativePath(), line);
    }


    private void writeDebugMappingToFile(String classFile, int classFileLine, String sourceFile, int sourceFileLine) {
        if (classMappingWriter != null) {
            classMappingWriter.println(classFile + ":" + classFileLine + "," + sourceFile + ":" + sourceFileLine);
        }
    }

    /**
     * @param className Class name
     * @return File handle of the original class file analyzed
     */
    private File findOriginalClassForBug(String className) {
        String sourceFile = byteCodeResourceLocator.findSourceFileKeyByClassName(className, javaResourceLocator);
        if (sourceFile == null) {
            return null;
        }

        return new File(sourceFile);
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguages(FindbugsPlugin.SUPPORTED_JVM_LANGUAGES);
        descriptor.name("FindBugs Sensor");
    }

}
