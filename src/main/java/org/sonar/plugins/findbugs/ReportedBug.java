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

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;
import edu.umd.cs.findbugs.BugInstance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportedBug implements ReportedBugInfo{

  private final String bugpattern;
  private final String message;
  private final String className;
  private final int startLine;
  private final String sourceFile;
  private final String classFile;
  private final String type;
  private static final Pattern SOURCE_FILE_PATTERN = createSourceFilePattern();

  public ReportedBug(BugInstance bugInstance) {
    this.bugpattern = bugInstance.getBugPattern().getType();
    this.type = bugInstance.getType();
    this.message = bugInstance.getMessageWithoutPrefix();
    this.className = bugInstance.getPrimarySourceLineAnnotation().getClassName();
    this.startLine = bugInstance.getPrimarySourceLineAnnotation().getStartLine();
    this.sourceFile = bugInstance.getPrimarySourceLineAnnotation().getSourcePath();
    Matcher m = SOURCE_FILE_PATTERN.matcher(sourceFile);
    if (m.find()) {
      this.classFile = m.group(1).replaceAll("/",".");
    }
    else {
      this.classFile = className;
    }
  }

  public String getType() {
    return bugpattern;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public BUG_TYPE getBugType() {

    if(bugpattern.equals("NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS") || bugpattern.equals("NP_NULL_PARAM_DEREF_NONVIRTUAL") || bugpattern.equals("NP_NONNULL_RETURN_VIOLATION")
            || bugpattern.equals("NP_DEREFERENCE_OF_READLINE_VALUE")|| bugpattern.equals("NP_GUARANTEED_DEREF")|| bugpattern.equals("NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD")
            || bugpattern.equals("NP_IMMEDIATE_DEREFERENCE_OF_READLINE")|| bugpattern.equals("NP_NULL_ON_SOME_PATH_EXCEPTION")|| bugpattern.equals("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
            || bugpattern.equals("NP_NULL_ON_SOME_PATH_MIGHT_BE_INFEASIBLE")|| bugpattern.equals("NP_NULL_PARAM_DEREF")|| bugpattern.equals("NP_NULL_ON_SOME_PATH")|| bugpattern.equals("NP_LOAD_OF_KNOWN_NULL_VALUE")
            || bugpattern.equals("NP_ALWAYS_NULL")|| bugpattern.equals("NP_ALWAYS_NULL_EXCEPTION")|| bugpattern.equals("NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")){

      return BUG_TYPE.NULL_POINTER_EXEPTION;

    }else if(bugpattern.equals("OS_OPEN_STREAM")){

      return BUG_TYPE.RESOURCE_LEAK;

    }else if(bugpattern.equals("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING ") || bugpattern.equals("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE ")|| bugpattern.equals("CUSTOM_INJECTION")
            || bugpattern.equals("SQL_INJECTION")|| bugpattern.equals("SQL_INJECTION_TURBINE")|| bugpattern.equals("SQL_INJECTION_HIBERNATE") || bugpattern.equals("SQL_INJECTION_JDO")|| bugpattern.equals("SQL_INJECTION_JPA")
            || bugpattern.equals("SQL_INJECTION_SPRING_JDBC")|| bugpattern.equals("SQL_INJECTION_JDBC")|| bugpattern.equals("SCRIPT_ENGINE_INJECTTION")|| bugpattern.equals("AWS_QUERY_INJECTION") || bugpattern.equals("BEAN_PROPERTY_INJECTION")){

      return BUG_TYPE.INJECTION;

    }else if(bugpattern.equals("URL_REWRITING")){

      return BUG_TYPE.BROKEN_AUTHENTICATION;

    }else if(bugpattern.equals("UNSAFE_HASH_EQUALS")){

      return BUG_TYPE.SENSITIVE_DATA_EXPOSURE;

    }else if(bugpattern.equals("XXE_XMLSTREAMREADER") || bugpattern.equals("XXE_SAXPARSER")|| bugpattern.equals("XXE_XMLREADER")|| bugpattern.equals("XXE_DOCUMENT")
            || bugpattern.equals("XXE_DTD_TRANSFORM_FACTORY")){

      return BUG_TYPE.XML_EXTERNAL_ENTITIES;

    }else if(bugpattern.equals("LDAP_ANONYMOUS") || bugpattern.equals("SERVLET_HEADER_REFERER")
            || bugpattern.equals("LDAP_ENTRY_POISONING")|| bugpattern.equals("LDAP_INJECTION")){

      return BUG_TYPE.BROKEN_ACCESS_CONTROL;

    }else if(bugpattern.equals("PREDICTABLE_RANDOM") || bugpattern.equals("SERVLET_CONTENT_TYPE") || bugpattern.equals("SERVERLET_SERVER_NAME") || bugpattern.equals("SERVERLET_HEADER_REFERER")
            || bugpattern.equals("SERVERLET_HEADER_USER_AGENT") || bugpattern.equals("SERVERLET_HEADER") || bugpattern.equals("JAXWS_ENDPOINT") || bugpattern.equals("JAXRS_ENDPOINT")
            || bugpattern.equals("WEAK_MESSAGE_DIGEST_MD5") || bugpattern.equals("WEAK_MESSAGE_DIGEST_SHA1") || bugpattern.equals("SSL_CONTEXT")|| bugpattern.equals("FILE_UPLOAD_FILENAME")
            || bugpattern.equals("SPRING_CSRF_PROTECTION_DISABLED")|| bugpattern.equals("SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING")|| bugpattern.equals("LDAP_INJECTION")|| bugpattern.equals("EL_INJECTION")
            || bugpattern.equals("HARD_CODED_PASSWORD")|| bugpattern.equals("HARD_CODED_KEY")|| bugpattern.equals("UNSAFE_HASH_EQUALS") || bugpattern.equals("HTTP_PARAMETER_POLLUTION")){

      return BUG_TYPE.SAFETY_MISCONFIGURATION;

    } else if(bugpattern.equals("XSS_REQUEST_WRAPPER") || bugpattern.equals("XSS_JSP_PRINT") || bugpattern.equals("XSS_SERVERLET")) {

      return BUG_TYPE.CROSS_SITE_SCRIPTING;

    }else if(bugpattern.equals("OBJECT_DESERIALIZATION") || bugpattern.equals("JACKSON_UNSAFE_DESERIALIZATION") || bugpattern.equals("DESERIALIZATION_GADGET")) {

      return BUG_TYPE.INSECURE_DESERIALIZATION;

    }else {

      return BUG_TYPE.ANOTHER_TYPE;

    }

  }

  @Override
  public String getBugMessage() {
    return message;
  }

  public String getClassName() {
    return className;
  }

  @Override
  public int getBugLineNumber() {
    return startLine;
  }

  @Override
  public String getSourcePath() {
    return sourceFile;
  }

  @Override
  public ToolCollection getToolName() {
    return ToolCollection.INFER.FINDBUGS;
  }

  public int getStartLine() {
    return startLine;
  }

  public String getSourceFile() { return sourceFile; }

  public String getClassFile() { return classFile; }

  private static Pattern createSourceFilePattern() {
    StringBuffer extensions = new StringBuffer();

    for (int i = 0; i < FindbugsPlugin.SUPPORTED_JVM_LANGUAGES_EXTENSIONS.length; i++) {
      String extension = FindbugsPlugin.SUPPORTED_JVM_LANGUAGES_EXTENSIONS[i];
      extensions.append(extension);
      if(i< FindbugsPlugin.SUPPORTED_JVM_LANGUAGES_EXTENSIONS.length - 1 ) {
        extensions.append("|");
      }
    }

    String pattern = "^(.*)\\.(" + extensions + ")$";
    return Pattern.compile(pattern);
  }
}
