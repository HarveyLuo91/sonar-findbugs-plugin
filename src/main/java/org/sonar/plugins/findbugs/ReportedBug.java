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

  private final String type;
  private final String message;
  private final String className;
  private final int startLine;
  private final String sourceFile;
  private final String classFile;

  private static final Pattern SOURCE_FILE_PATTERN = createSourceFilePattern();

  public ReportedBug(BugInstance bugInstance) {
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
    return type;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public BUG_TYPE getBugType() {
    if(type.equals("NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS") || type.equals("NP_NULL_PARAM_DEREF_NONVIRTUAL") || type.equals("NP_NONNULL_RETURN_VIOLATION")
            || type.equals("NP_DEREFERENCE_OF_READLINE_VALUE")|| type.equals("NP_GUARANTEED_DEREF")|| type.equals("NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD")
            || type.equals("NP_IMMEDIATE_DEREFERENCE_OF_READLINE")|| type.equals("NP_NULL_ON_SOME_PATH_EXCEPTION")|| type.equals("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
            || type.equals("NP_NULL_ON_SOME_PATH_MIGHT_BE_INFEASIBLE")|| type.equals("NP_NULL_PARAM_DEREF")|| type.equals("NP_NULL_ON_SOME_PATH")|| type.equals("NP_LOAD_OF_KNOWN_NULL_VALUE")
            || type.equals("NP_ALWAYS_NULL")|| type.equals("NP_ALWAYS_NULL_EXCEPTION")|| type.equals("NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")){

      return BUG_TYPE.NULL_POINTER_EXEPTION;

    }else if(type.equals("OS_OPEN_STREAM")){

      return BUG_TYPE.RESOURCE_LEAK;

    }else if(type.equals("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING ") || type.equals("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE ")|| type.equals("CUSTOM_INJECTION")
            || type.equals("SQL_INJECTION")|| type.equals("SQL_INJECTION_TURBINE")|| type.equals("SQL_INJECTION_HIBERNATE") || type.equals("SQL_INJECTION_JDO")|| type.equals("SQL_INJECTION_JPA")
            || type.equals("SQL_INJECTION_SPRING_JDBC")|| type.equals("SQL_INJECTION_JDBC")|| type.equals("SCRIPT_ENGINE_INJECTTION")|| type.equals("AWS_QUERY_INJECTION") || type.equals("BEAN_PROPERTY_INJECTION")){

      return BUG_TYPE.INJECTION;

    }else if(type.equals("URL_REWRITING")){

      return BUG_TYPE.BROKEN_AUTHENTICATION;

    }else if(type.equals("UNSAFE_HASH_EQUALS")){

      return BUG_TYPE.SENSITIVE_DATA_EXPOSURE;

    }else if(type.equals("XXE_XMLSTREAMREADER") || type.equals("XXE_SAXPARSER")|| type.equals("XXE_XMLREADER")|| type.equals("XXE_DOCUMENT")
            || type.equals("XXE_DTD_TRANSFORM_FACTORY")){

      return BUG_TYPE.XML_EXTERNAL_ENTITIES;

    }else if(type.equals("LDAP_ANONYMOUS") || type.equals("SERVLET_HEADER_REFERER")
            || type.equals("LDAP_ENTRY_POISONING")|| type.equals("LDAP_INJECTION")
            || type.equals("")){

      return BUG_TYPE.BROKEN_ACCESS_CONTROL;

    }else if(type.equals("PREDICTABLE_RANDOM") || type.equals("SERVLET_CONTENT_TYPE") || type.equals("SERVERLET_SERVER_NAME") || type.equals("SERVERLET_HEADER_REFERER")
            || type.equals("SERVERLET_HEADER_USER_AGENT") || type.equals("SERVERLET_HEADER") || type.equals("JAXWS_ENDPOINT") || type.equals("JAXRS_ENDPOINT")
            || type.equals("WEAK_MESSAGE_DIGEST_MD5") || type.equals("WEAK_MESSAGE_DIGEST_SHA1") || type.equals("SSL_CONTEXT")|| type.equals("FILE_UPLOAD_FILENAME")
            || type.equals("SPRING_CSRF_PROTECTION_DISABLED")|| type.equals("SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING")|| type.equals("LDAP_INJECTION")|| type.equals("EL_INJECTION")
            || type.equals("HARD_CODED_PASSWORD")|| type.equals("HARD_CODED_KEY")|| type.equals("UNSAFE_HASH_EQUALS") || type.equals("HTTP_PARAMETER_POLLUTION")){

      return BUG_TYPE.SAFETY_MISCONFIGURATION;

    } else if(type.equals("XSS_REQUEST_WRAPPER") || type.equals("XSS_JSP_PRINT") || type.equals("XSS_SERVERLET")) {

      return BUG_TYPE.CROSS_SITE_SCRIPTING;

    }else if(type.equals("OBJECT_DESERIALIZATION") || type.equals("JACKSON_UNSAFE_DESERIALIZATION") || type.equals("DESERIALIZATION_GADGET")) {

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
