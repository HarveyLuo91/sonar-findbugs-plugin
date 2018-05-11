package edu.bit.cs.findbugs;

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.Constant;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;
import org.sonar.plugins.findbugs.FindbugsSensor;

public class findbugsReportedBug implements ReportedBugInfo {
    private final String type;
    private final String message;
    private final String className;
    private final int bugLineNumber;
    private final String sourcePath;

    public static final ToolCollection FindBugs = ToolCollection.FINDBUGS;

    public findbugsReportedBug(String type, String message, String className, int bugLineNumber, String sourcePath) {
        this.type = type;
        this.message = message;
        this.className = className;
        this.bugLineNumber = bugLineNumber;
        this.sourcePath = sourcePath;
    }


    @Override
    public BUG_TYPE getBugType() {

        if (type.equals("NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS")
//                || type.equals("NP_NULL_PARAM_DEREF_NONVIRTUAL")
                || type.equals("NP_NONNULL_RETURN_VIOLATION")
                || type.equals("NP_DEREFERENCE_OF_READLINE_VALUE")
                || type.equals("NP_GUARANTEED_DEREF")
                || type.equals("NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD")
                || type.equals("NP_IMMEDIATE_DEREFERENCE_OF_READLINE")
                || type.equals("NP_NULL_ON_SOME_PATH_EXCEPTION")
                || type.equals("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
                || type.equals("NP_NULL_ON_SOME_PATH_MIGHT_BE_INFEASIBLE")
                || type.equals("NP_NULL_PARAM_DEREF")
                || type.equals("NP_NULL_ON_SOME_PATH")
                || type.equals("NP_LOAD_OF_KNOWN_NULL_VALUE")
                || type.equals("NP_ALWAYS_NULL")
                || type.equals("NP_ALWAYS_NULL_EXCEPTION")
                || type.equals("NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")) {

            return BUG_TYPE.NULL_POINTER_EXEPTION;

        } else if (type.equals("OS_OPEN_STREAM")) {

            return BUG_TYPE.RESOURCE_LEAK;

        } else if (type.equals("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
                || type.equals("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE")
                || type.equals("CUSTOM_INJECTION")
                || type.equals("SQL_INJECTION")
                || type.equals("SQL_INJECTION_TURBINE")
                || type.equals("SQL_INJECTION_HIBERNATE")
                || type.equals("SQL_INJECTION_JDO")
                || type.equals("SQL_INJECTION_JPA")
                || type.equals("SQL_INJECTION_SPRING_JDBC")
                || type.equals("SQL_INJECTION_JDBC")
                || type.equals("SCRIPT_ENGINE_INJECTTION")
                || type.equals("AWS_QUERY_INJECTION")
                || type.equals("BEAN_PROPERTY_INJECTION")) {

            return BUG_TYPE.INJECTION;

        } else if (type.equals("URL_REWRITING")) {

            return BUG_TYPE.BROKEN_AUTHENTICATION;

        } else if (type.equals("UNSAFE_HASH_EQUALS")) {

            return BUG_TYPE.SENSITIVE_DATA_EXPOSURE;

        } else if (type.equals("XXE_XMLSTREAMREADER") || type.equals("XXE_SAXPARSER") || type.equals("XXE_XMLREADER") || type.equals("XXE_DOCUMENT")
                || type.equals("XXE_DTD_TRANSFORM_FACTORY")) {

            return BUG_TYPE.XML_EXTERNAL_ENTITIES;

        } else if (type.equals("LDAP_ANONYMOUS") || type.equals("SERVLET_HEADER_REFERER")
                || type.equals("LDAP_ENTRY_POISONING") || type.equals("LDAP_INJECTION")) {

            return BUG_TYPE.BROKEN_ACCESS_CONTROL;

        } else if (type.equals("PREDICTABLE_RANDOM") || type.equals("SERVLET_CONTENT_TYPE") || type.equals("SERVERLET_SERVER_NAME") || type.equals("SERVERLET_HEADER_REFERER")
                || type.equals("SERVERLET_HEADER_USER_AGENT") || type.equals("SERVERLET_HEADER") || type.equals("JAXWS_ENDPOINT") || type.equals("JAXRS_ENDPOINT")
                || type.equals("WEAK_MESSAGE_DIGEST_MD5") || type.equals("WEAK_MESSAGE_DIGEST_SHA1") || type.equals("SSL_CONTEXT") || type.equals("FILE_UPLOAD_FILENAME")
                || type.equals("SPRING_CSRF_PROTECTION_DISABLED") || type.equals("SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING") || type.equals("LDAP_INJECTION") || type.equals("EL_INJECTION")
                || type.equals("HARD_CODED_PASSWORD") || type.equals("HARD_CODED_KEY") || type.equals("UNSAFE_HASH_EQUALS") || type.equals("HTTP_PARAMETER_POLLUTION")) {

            return BUG_TYPE.SAFETY_MISCONFIGURATION;

        } else if (type.equals("XSS_REQUEST_WRAPPER") || type.equals("XSS_JSP_PRINT") || type.equals("XSS_SERVERLET")) {

            return BUG_TYPE.CROSS_SITE_SCRIPTING;

        } else if (type.equals("OBJECT_DESERIALIZATION") || type.equals("JACKSON_UNSAFE_DESERIALIZATION") || type.equals("DESERIALIZATION_GADGET")) {

            return BUG_TYPE.INSECURE_DESERIALIZATION;

        } else {

            return BUG_TYPE.ANOTHER_TYPE;

        }

    }
    // @Override
    //public String getType(){
    //  return this.type;
    //}
    @Override
    public String getBugMessage() {
        return message;
    }

    public String getClassName() {
        return className;
    }

    public int getBugLineNumber() {
        return bugLineNumber;
    }

    public String getSourcePath() {
        return ReportedBugInfo.normalizeFilePath(sourcePath, Constant.ROOT);
    }

    @Override
    public ToolCollection getToolName() {
        return FindBugs;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Bug Type: " + getBugType() + "\n");
        string.append("Error Message: " + getBugMessage() + "\n");
        string.append("ClassName: " + getClassName() + "\n");
        string.append("Bug_LineNumber: " + getBugLineNumber() + "\n");
        string.append("Source Path: " + getSourcePath() + "\n");
        return string.toString();
    }
}
