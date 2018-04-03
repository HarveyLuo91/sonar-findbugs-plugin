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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.assessment.Result;
import edu.bit.cs.bit.BitReportParser;
import edu.bit.cs.bit.BitReportedBugInfo;
import edu.bit.cs.infer.InferReportedBugFromJson;
import edu.bit.cs.jlint.JlintReportParser;
import edu.bit.cs.util.Bug_Type_Pair_Analysis;
import edu.bit.cs.util.InferExecutionCollection;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

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

    public static Map<String, List<ReportedBugInfo>> bugs_analysis = new HashMap<String, List<ReportedBugInfo>>();

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

            npe_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "NULL_POINTER_EXEPTION");//NPE
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

        ActiveRule XSS_killbugs_rule = null; //CROSS_SITE_SCRIPTING
        for (String repoKey : getRepositories()) {
            XSS_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "CROSS_SITE_SCRIPTING");
            if (XSS_killbugs_rule != null) {
                break;
            }
        }
        if (XSS_killbugs_rule == null) {
            System.out.println("-----------------XSS_killbugs_rule is null!");
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


        ActiveRule INHR_killbugs_rule = null; //INHER
        for (String repoKey : getRepositories()) {
            INHR_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "INHERITANCE");
            if (INHR_killbugs_rule != null) {
                break;
            }
        }
        if (INHR_killbugs_rule == null) {
            System.out.println("-----------------INHR_killbugs_rule is null!");
            return;
        }

        ActiveRule SYNC_killbugs_rule = null; //SYNC
        for (String repoKey : getRepositories()) {
            SYNC_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "SYNCHRONIZATION");
            if (SYNC_killbugs_rule != null) {
                break;
            }
        }
        if (SYNC_killbugs_rule == null) {
            System.out.println("-----------------SYNC_killbugs_rule is null!");
            return;
        }

        ActiveRule OTHER_killbugs_rule = null; //others
        for (String repoKey : getRepositories()) {
            OTHER_killbugs_rule = ruleFinder.findByInternalKey(repoKey, "ANOTHER_TYPE");
            if (OTHER_killbugs_rule != null) {
                break;
            }
        }
        if (SYNC_killbugs_rule == null) {
            System.out.println("-----------------OTHER_killbugs_rule is null!");
            return;
        }

        Collection<ReportedBug> collection = executor.execute(hasActiveFbContribRules(), hasActiveFindSecBugsRules());
        try {

            Map<String, List<ReportedBugInfo>> bugs = Maps.newHashMap();

            String absolutePath = context.fileSystem().baseDir().getAbsolutePath();

            File binaries = new File(absolutePath, context.settings().getString("sonar.binaries"));
            System.out.println("binaries:" + binaries);

            //Jlint
            //Collection<? extends ReportedBugInfo> jlintReportedBugs = CmdExecutor.exeCmd(binaries.getAbsolutePath(), new JlintReportParser());
            Collection<? extends ReportedBugInfo> jlintReportedBugs = JlintReportParser.getReportedBugs();
            System.out.println("******************************Jlint size:" + jlintReportedBugs.size());
            //loop through the reported bugs
            for (ReportedBugInfo bugInstance : jlintReportedBugs) {
                //System.out.println("-------------------jlint uid:" + bugInstance.getUID());
                List bugInstances;
                if (!bugs.containsKey(bugInstance.getUID())) {
                    bugInstances = Lists.newArrayList();
                } else {
                    bugInstances = bugs.get(bugInstance.getUID());
                }
                bugInstances.add(bugInstance);
                bugs.put(bugInstance.getUID(), bugInstances);
            }

            //Infer
            System.out.println("Infer dir:" + absolutePath);
            //Collection<? extends ReportedBugInfo> inferReportedBugs = CmdExecutor.exeCmd(absolutePath, new InferReportParser());
            Collection<? extends ReportedBugInfo> inferReportedBugs = InferExecutionCollection.JsonCollection();

            System.out.println("***********************Infer size:" + inferReportedBugs.size());
            int jlint_infer_intersection = 0;
            for (ReportedBugInfo bugInstance : inferReportedBugs) {
                //System.out.println("INFER:" + ((InferReportedBugFromJson) bugInstance).toString());
                //System.out.println("-------------------Infer uid:" + bugInstance.getUID());
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
//            }


            //for assessment
            Map<String, Result> interSection = Maps.newHashMap();
            for (int index = 0; index < ToolCollection.getSize(); index++) {
                interSection.put(ToolCollection.getTools(index), new Result(ToolCollection.getTools(index)));
            }

            for (String key : bugs.keySet()) {
                bugs_analysis.put(key, bugs.get(key));
            }

            //BIT Analyzer
            try {

                BitReportParser parser = new BitReportParser();
                Collection<? extends ReportedBugInfo> BitReportedBugs = parser.getReportedBugs();
                for (ReportedBugInfo bugInstance : BitReportedBugs) {

                    System.out.println("BIT:" + ((BitReportedBugInfo) bugInstance).toString());
                    System.out.println("-------------------BIT uid:" + bugInstance.getUID());

                    if (bugs_analysis.containsKey(bugInstance.getUID())) {
                        List<ReportedBugInfo> instances = bugs_analysis.get(bugInstance.getUID());
                        instances.add(bugInstance);
                        bugs_analysis.put(bugInstance.getUID(), instances);
                        bugs.put(bugInstance.getUID(), instances);
                    } else {
                        List<ReportedBugInfo> instances = new ArrayList<ReportedBugInfo>();
                        instances.add(bugInstance);
                        bugs_analysis.put(bugInstance.getUID(), instances);
                        bugs.put(bugInstance.getUID(), instances);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //findbugs
            for (ReportedBug bugInstance : collection) {
                try {
                    if (bugs_analysis.containsKey(bugInstance.getUID())) {
                        List<ReportedBugInfo> instances = bugs_analysis.get(bugInstance.getUID());
                        instances.add(bugInstance);
                        bugs_analysis.put(bugInstance.getUID(), instances);

                    } else {
                        List<ReportedBugInfo> instances = new ArrayList<ReportedBugInfo>();
                        instances.add(bugInstance);
                        bugs_analysis.put(bugInstance.getUID(), instances);
                    }

                    ActiveRule rule = null;
                    for (String repoKey : getRepositories()) {
                        // System.out.println("\n---------------------type:" + bugInstance.getBugType().name() + "\nmessage:" + bugInstance.getMessage());
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
            for (String bugInstanceKey : bugs.keySet()) {

                String[] splitKeyInstance = bugInstanceKey.split("-");
                if (splitKeyInstance[2].equals(BUG_TYPE.NULL_POINTER_EXEPTION.name())) {
                    createIssue(npe_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.RESOURCE_LEAK.name())) {
                    createIssue(RL_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.CROSS_SITE_SCRIPTING.name())) {
                    createIssue(XSS_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.INJECTION.name())) {
                    createIssue(Inj_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.INHERITANCE.name())) {
                    createIssue(INHR_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.SYNCHRONIZATION.name())) {
                    createIssue(SYNC_killbugs_rule, bugs, bugInstanceKey, interSection);
                } else if (splitKeyInstance[2].equals(BUG_TYPE.ANOTHER_TYPE.name())) {
                    createIssue(OTHER_killbugs_rule, bugs, bugInstanceKey, interSection);
                }
            }

            //for assessment
            Result findbugs = new Result("FINDBUGS");
            Result jlint = new Result("JLINT");
            Result infer = new Result("INFER");

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

            System.out.println("\n");
            System.out.println("Name: " + findbugs.getName());
            System.out.println("Bug Number: " + (findbugs.getFp().size() + findbugs.getTp().size()));
            System.out.println("TP: " + findbugs.getTp().size());
            System.out.println("FP: " + findbugs.getFp().size());
            System.out.println("precision: " + findbugs.getPrecision());
            System.out.println("recall: " + findbugs.getRecall());

            System.out.println("\n");
            System.out.println("Name: " + jlint.getName());
            System.out.println("Bug Number: " + (jlint.getFp().size() + jlint.getTp().size()));
            System.out.println("TP: " + jlint.getTp().size());
            System.out.println("FP: " + jlint.getFp().size());
            System.out.println("precision: " + jlint.getPrecision());
            System.out.println("recall: " + jlint.getRecall());

            System.out.println("\n");
            System.out.println("Name: " + infer.getName());
            System.out.println("Bug Number: " + (infer.getFp().size() + infer.getTp().size()));
            System.out.println("TP: " + infer.getTp().size());
            System.out.println("FP: " + infer.getFp().size());
            System.out.println("precision: " + infer.getPrecision());
            System.out.println("recall: " + infer.getRecall());


            Bug_Type_Pair_Analysis.analyze(bugs_analysis);

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
            //  System.out.println("-------------------" + bug.getToolName() + "\nsourcefile:" + bug.getSourcePath());
            index = ToolCollection.addTool(index, bug.getToolName());
        }

        String tools = ToolCollection.getTools(index);

        //for assessment
        Result result = interSection.get(tools);
        result.judge(bugs.get(bugInstanceKey));
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
            System.out.println("-------------------resource is null");
        }
    }

    private String processBugs(Map<String, List<ReportedBugInfo>> bugs, Map<String, Result> interSection, ReportedBugInfo bug) {
        int index = 1;
        if (bugs.containsKey(bug.getUID())) {
            for (ReportedBugInfo bugInfo : bugs.get(bug.getUID())) {
                index = ToolCollection.addTool(index, bugInfo.getToolName());
            }
            index = ToolCollection.addTool(index, ToolCollection.FINDBUGS);

            //for assessment
            System.out.println("result interSection:" + ToolCollection.getTools(index));
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
