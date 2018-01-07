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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.xpath.internal.SourceTree;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;
import edu.bit.cs.infer.InferReportParser;
import edu.bit.cs.infer.InferReportedBug;
import edu.bit.cs.jlint.*;
import edu.bit.cs.util.CmdExecutor;
import edu.bit.cs.util.ToolCollection;
import edu.umd.cs.findbugs.BugInstance;
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
import org.sonar.plugins.findbugs.rules.*;
import org.sonar.plugins.java.api.JavaResourceLocator;

import javax.tools.Tool;
import javax.xml.bind.SchemaOutputResolver;

public class FindbugsSensor implements Sensor {

    private static final Logger LOG = LoggerFactory.getLogger(FindbugsSensor.class);

    private static final String[] REPOS = {FindbugsRulesDefinition.REPOSITORY_KEY, FbContribRulesDefinition.REPOSITORY_KEY,
            FindSecurityBugsRulesDefinition.REPOSITORY_KEY, FindSecurityBugsJspRulesDefinition.REPOSITORY_KEY
    };

    private List<String> repositories = new ArrayList<String>();

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

        if (!hasActiveFindbugsRules() && !hasActiveFbContribRules() && !hasActiveFindSecBugsRules()) {
            return;
        }

        Collection<ReportedBug> collection = executor.execute(hasActiveFbContribRules(), hasActiveFindSecBugsRules());

        try {

            Map<String, List<ReportedBugInfo>> bugs = Maps.newHashMap();

            String absolutePath = context.fileSystem().baseDir().getAbsolutePath();

            //Jlint
            String root = context.settings().getString("sonar.root");
            File binaries = new File(absolutePath, context.settings().getString("sonar.binaries"));
            System.out.println("binaries:" + binaries);
            String cmd = CmdExecutor.genCmdStr("JLINT", binaries.getAbsolutePath());
            Collection<? extends ReportedBugInfo> jlintReportedBugs = CmdExecutor.exeCmd(cmd, new JlintReportParser(root));
            System.out.println("******************************Jlint size:" + jlintReportedBugs.size());
            for (ReportedBugInfo bugInstance : jlintReportedBugs) {
                if (bugInstance.getBugType().equals(BUG_TYPE.ANOTHER_TYPE.toString())) {
                    continue;
                }
                String sourceFile = bugInstance.getSourcePath();
                int line = bugInstance.getBugLineNumber();
                System.out.println("-------------------jlint sourceFile+line:" + sourceFile + line);
                List bugInstances;
                if (!bugs.containsKey(sourceFile + line)) {
                    bugInstances = Lists.newArrayList();
                } else {
                    bugInstances = bugs.get(sourceFile + line);
                }
                bugInstances.add(bugInstance);
                bugs.put(sourceFile + line, bugInstances);
            }
            System.out.println("**********************jlint map size:" + bugs.size());

            //Infer
            List<InferReportedBug> list = InferReportParser.get_Reported_Infer_Bugs();
            System.out.println("***********************Infer size:" + list.size());
            int jlint_infer_intersection = 0;
            for (InferReportedBug bugInstance : list) {
                if (!bugInstance.getInfer_Bug_Type().equals("NULL_DEREFERENCE")) {
                    continue;
                }
//                String className = bugInstance.getClassName();
                String sourceFile = bugInstance.getSourcePath();
//                String longMessage = bugInstance.getMessage();
                int line = bugInstance.getBugLineNumber();
                System.out.println("-------------------Infer sourceFile+line:" + sourceFile + line);
                List bugInstances;
                if (!bugs.containsKey(sourceFile + line)) {
                    bugInstances = Lists.newArrayList();
                } else {
                    jlint_infer_intersection = jlint_infer_intersection + 1;
                    bugInstances = bugs.get(sourceFile + line);
                }
                bugInstances.add(bugInstance);
                bugs.put(sourceFile + line, bugInstances);
            }
            System.out.println("**********************Infer map size:" + bugs.size() + "--jlint_infer_intersection:" + jlint_infer_intersection);

            Map<String, Integer> interSection = Maps.newHashMap();
            for (int index = 0; index < ToolCollection.getSize(); index++) {
                interSection.put(ToolCollection.getTools(index), 0);
            }

            for (ReportedBug bugInstance : collection) {
                try {
                    ActiveRule rule = null;
                    for (String repoKey : getRepositories()) {
                        System.out.println("---------------------type:" + bugInstance.getType() + "message:" + bugInstance.getMessage());
                        rule = ruleFinder.findByInternalKey(repoKey, bugInstance.getType());
                        if (rule != null) {
                            break;
                        }
                    }
                    if (rule == null) {
                        // ignore violations from report, if rule not activated in Sonar
                        System.out.println("----------------Findbugs rule '{}' is not active in Sonar." + bugInstance.getType());
                        LOG.warn("Findbugs rule '{}' is not active in Sonar.", bugInstance.getType());
                        continue;
                    }

                    String className = bugInstance.getClassName();
                    String sourceFile = bugInstance.getSourceFile();
                    StringBuilder longMessage = new StringBuilder(bugInstance.getMessage());
                    int line = bugInstance.getStartLine();

                    System.out.println("findbugs sourcefile:" + sourceFile);
                    //Regular Java class mapped to their original .java
                    InputFile resource = byteCodeResourceLocator.findSourceFile(sourceFile, this.fs);
                    if (resource != null) {
                        String tools = processBugs(bugs, interSection, sourceFile + line);
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

                    //        //If the class was an outer class, the source file will not be analog to the class name.
                    //        //The original source file is available in the class file metadata.
                    //        resource = byteCodeResourceLocator.findJavaOuterClassFile(className, classFile, this.fs);
                    //        if (resource != null) {
                    //          insertIssue(rule, resource, line, longMessage);
                    //          continue;
                    //        }

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
                                    String tools = processBugs(bugs, interSection, sourceFile + line);
                                    longMessage.insert(0, tools + "- ");
                                    insertIssue(rule, resource, line, longMessage.toString(), bugInstance);
                                    continue;
                                }
                            } else {
                                //SMAP was not found or unparsable.. The orgininal source file will be guess based on the class name
                                resource = byteCodeResourceLocator.findTemplateFile(className, this.fs);
                                if (resource != null) {
                                    String tools = processBugs(bugs, interSection, sourceFile + line);
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
                    String bugInstanceDebug = String.format("[BugInstance type=%s, class=%s, line=%s]", bugInstance.getType(), bugInstance.getClassName(), bugInstance.getStartLine());
                    LOG.warn("An error occurs while processing the bug instance " + bugInstanceDebug, e);
                    //Continue to the bug without aborting the report
                }
            }


            ActiveRule rule = null;
            for (String repoKey : getRepositories()) {
                rule = ruleFinder.findByInternalKey(repoKey, "NULL_POINTER_DETECTED_BY_JLINT");
                if (rule != null) {
                    break;
                }
            }
            if (rule == null) {
                System.out.println("-----------------rule is null!");
                return;
            }
            for (List<ReportedBugInfo> restBugs : bugs.values()) {
//                String className = bugInstance.getClassName();
                String sourceFile = "";
                StringBuilder longMessage = new StringBuilder("");
                int index = 0;
                int line = 0;
                for (ReportedBugInfo bug : restBugs) {
                    System.out.println("-------------------" + bug.getToolName() + " sourcefile:" + bug.getSourcePath());
                    index = ToolCollection.addTool(index, bug.getToolName());
                }
                String tools = ToolCollection.getTools(index);
                int value = interSection.get(tools);
                interSection.put(tools, value + 1);
                longMessage.append(tools);
                for (ReportedBugInfo bug : restBugs) {
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

            for (int index = 0; index < ToolCollection.getSize(); index++) {
                System.out.println(ToolCollection.getTools(index) + ":" + interSection.get(ToolCollection.getTools(index)));
            }

        } finally {
            classMappingWriter.flush();
            classMappingWriter.close();
        }
    }

    private String processBugs(Map<String, List<ReportedBugInfo>> bugs, Map<String, Integer> interSection, String key) {
        int index = 1;
        if (bugs.containsKey(key)) {
            for (ReportedBugInfo bugInfo : bugs.get(key)) {
                index = ToolCollection.addTool(index, bugInfo.getToolName());
            }
            bugs.remove(key);
            index = ToolCollection.addTool(index, ToolCollection.FINDBUGS);
        }
        String tools = ToolCollection.getTools(index);
        int value = interSection.get(tools);
        interSection.put(tools, value + 1);
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
//        NewIssue newIssue = sensorContext.newIssue().forRule(rule.ruleKey());
//
//        NewIssueLocation location = newIssue.newLocation()
//                .on(resource)
//                .at(resource.selectLine(line > 0 ? line : 1))
//                .message("-" + message);
//
//        newIssue.at(location); //Primary location
//        newIssue.save();
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
