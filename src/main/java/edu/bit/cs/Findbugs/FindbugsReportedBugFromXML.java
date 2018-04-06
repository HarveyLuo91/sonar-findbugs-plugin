package edu.bit.cs.Findbugs;

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.util.ToolCollection;
import edu.umd.cs.findbugs.BugInstance;
import org.sonar.plugins.findbugs.FindbugsPlugin;
import org.sonar.plugins.findbugs.ReportedBug;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindbugsReportedBugFromXML implements ReportedBugInfo {

    private final String message;
    private final String startLine;
    private final String sourcePath;
    private final String type;
    //private static final Pattern SOURCE_FILE_PATTERN = createSourceFilePattern();

    public FindbugsReportedBugFromXML(String message, String sourcePath, String startLine, String type) {

        this.type = type;
        this.message = message;
        this.startLine = startLine;
        this.sourcePath = sourcePath;
    }


    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public BUG_TYPE getBugType() {

        if(type.equals("BROKEN_ACCESS_CONTROL")||type.equals("ML_SYNC_ON_FIELD_TO_GUARD_CHANGING_THAT_FIELD")
                ||type.equals("UTAO_TESTNG_ASSERTION_ODDITIES_USE_ASSERT_NULL")||type.equals("HCP_HTTP_REQUEST_RESOURCES_NOT_FREED_FIELD")
                ||type.equals("OI_OPTIONAL_ISSUES_CHECKING_REFERENCE")||type.equals("BC_NULL_INSTANCEOF")||type.equals("OI_OPTIONAL_ISSUES_USES_ORELSEGET_WITH_NULL")
                ||type.equals("INSECURE_DESERIALIZATION")||type.equals("NP_BOOLEAN_RETURN_NULL")||type.equals("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
                ||type.equals("NP_NULL_ON_SOME_PATH_EXCEPTION")||type.equals("NP_OPTIONAL_RETURN_NULL")||type.equals("SENSITIVE_DATA_EXPOSURE")
                ||type.equals("NP_EQUALS_SHOULD_HANDLE_NULL_ARGUMENT")||type.equals("NP_SYNC_AND_NULL_CHECK_FIELD")||type.equals("RESOURCE_LEAK")
                ||type.equals("AIOB_ARRAY_STORE_TO_NULL_REFERENCE")||type.equals("WEAK_HOSTNAME_VERIFIER")||type.equals("NP_CLONE_COULD_RETURN_NULL")
                ||type.equals("SAFETY_MISCONFIGURATION")||type.equals("EC_NULL_ARG")||type.equals("NP_UNWRITTEN_FIELD")||type.equals("NP_CLOSING_NULL")
                ||type.equals("CCI_CONCURRENT_COLLECTION_ISSUES_USE_PUT_IS_RACY")||type.equals("HCP_HTTP_REQUEST_RESOURCES_NOT_FREED_LOCAL")
                ||type.equals("SNG_SUSPICIOUS_NULL_LOCAL_GUARD")||type.equals("ISB_TOSTRING_APPENDING")||type.equals("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
                ||type.equals("NP_NULL_INSTANCEOF")||type.equals("NP_GUARANTEED_DEREF_ON_EXCEPTION_PATH")||type.equals("NP_NULL_PARAM_DEREF_NONVIRTUAL")
                ||type.equals("NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS")||type.equals("NP_NONNULL_RETURN_VIOLATION")||type.equals("NP_TOSTRING_COULD_RETURN_NULL")
                ||type.equals("NP_GUARANTEED_DEREF")||type.equals("SNG_SUSPICIOUS_NULL_FIELD_GUARD")||type.equals("FI_NULLIFY_SUPER")||type.equals("WEAK_TRUST_MANAGER")
                ||type.equals("BROKEN_AUTHENTICATION")||type.equals("FI_FINALIZER_NULLS_FIELDS")||type.equals("SPP_NULL_BEFORE_INSTANCEOF")
                ||type.equals("IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION")||type.equals("NP_NULL_PARAM_DEREF")||type.equals("SPP_SUSPECT_STRING_TEST")
                ||type.equals("UTAO_JUNIT_ASSERTION_ODDITIES_USE_ASSERT_NULL")||type.equals("NP_ARGUMENT_MIGHT_BE_NULL")||type.equals("UWF_NULL_FIELD")
                ||type.equals("SPP_NULL_CHECK_ON_OPTIONAL")||type.equals("SPP_NULL_CHECK_ON_MAP_SUBSET_ACCESSOR")||type.equals("NP_NONNULL_PARAM_VIOLATION")
                ||type.equals("S508C_NULL_LAYOUT")||type.equals("FI_FINALIZER_ONLY_NULLS_FIELDS")||type.equals("NP_NULL_ON_SOME_PATH")||type.equals("NULL_CIPHER")
                ||type.equals("UNNC_UNNECESSARY_NEW_NULL_CHECK")||type.equals("UTAO_TESTNG_ASSERTION_ODDITIES_USE_ASSERT_NOT_NULL")||type.equals("NP_STORE_INTO_NONNULL_FIELD")
                ||type.equals("UTAO_JUNIT_ASSERTION_ODDITIES_USE_ASSERT_NOT_NULL")||type.equals("NP_ALWAYS_NULL")||type.equals("NP_ALWAYS_NULL_EXCEPTION")
                ||type.equals("NULL_POINTER_EXEPTION")||type.equals("SPP_INVALID_BOOLEAN_NULL_CHECK")){

            return BUG_TYPE.NULL_POINTER_EXEPTION;

        }else if(type.equals("ODR_OPEN_DATABASE_RESOURCE")||type.equals("HCP_HTTP_REQUEST_RESOURCES_NOT_FREED_FIELD")||type.equals("ODR_OPEN_DATABASE_RESOURCE_EXCEPTION_PATH")
                ||type.equals("OS_OPEN_STREAM_EXCEPTION_PATH")||type.equals("HCP_HTTP_REQUEST_RESOURCES_NOT_FREED_LOCAL")||type.equals("MDM_RUNFINALIZATION")
                ||type.equals("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")||type.equals("OBL_UNSATISFIED_OBLIGATION")||type.equals("TLW_TWO_LOCK_WAIT")||type.equals("OS_OPEN_STREAM")){

            return BUG_TYPE.RESOURCE_LEAK;

        }else if(type.equals("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")||type.equals("SQL_INJECTION_SPRING_JDBC")||type.equals("HTTP_RESPONSE_SPLITTING")
                ||type.equals("SQL_INJECTION_HIBERNATE")||type.equals("SEAM_LOG_INJECTION")||type.equals("CRLF_INJECTION_LOGS")||type.equals("MALICIOUS_XSLT")
                ||type.equals("EL_INJECTION")||type.equals("SPEL_INJECTION")||type.equals("SQL_INJECTION_ANDROID")||type.equals("SCRIPT_ENGINE_INJECTION")
                ||type.equals("OGNL_INJECTION")||type.equals("SQL_INJECTION_JDBC")||type.equals("JSP_SPRING_EVAL")||type.equals("LDAP_ENTRY_POISONING")
                ||type.equals("WEAK_FILENAMEUTILS")||type.equals("LDAP_INJECTION")||type.equals("SQL_INJECTION_TURBINE")||type.equals("AWS_QUERY_INJECTION")
                ||type.equals("SQL_INJECTION_JPA")||type.equals("XPATH_INJECTION")||type.equals("TEMPLATE_INJECTION_VELOCITY")||type.equals("SQL_INJECTION_JDO")
                ||type.equals("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE")||type.equals("TEMPLATE_INJECTION_FREEMARKER")||type.equals("COMMAND_INJECTION")
                ||type.equals("SERVLET_PARAMETER")||type.equals("JSP_XSLT")||type.equals("SQL_INJECTION")){

            return BUG_TYPE.INJECTION;

        }else if(type.equals("XSS_SERVLET")||type.equals("XSS_REQUEST_PARAMETER_TO_JSP_WRITER")||type.equals("XSS_REQUEST_WRAPPER")
                ||type.equals("XSS_REQUEST_PARAMETER_TO_SERVLET_WRITER")||type.equals("XSS_REQUEST_PARAMETER_TO_SEND_ERROR")
                ||type.equals("ANDROID_WEB_VIEW_JAVASCRIPT")||type.equals("JSP_JSTL_OUT")||type.equals("XSS_JSP_PRINT")) {

            return BUG_TYPE.CROSS_SITE_SCRIPTING;

        }else if (
                type.equals("UL_UNRELEASED_LOCK")||type.equals("ML_SYNC_ON_FIELD_TO_GUARD_CHANGING_THAT_FIELD")||type.equals("NO_NOTIFY_NOT_NOTIFYALL")||type.equals("ML_SYNC_ON_UPDATED_FIELD")
                        ||type.equals("NP_SYNC_AND_NULL_CHECK_FIELD")||type.equals("SP_SPIN_ON_FIELD")||type.equals("NN_NAKED_NOTIFY")||type.equals("PS_PUBLIC_SEMAPHORES")
                        ||type.equals("JML_JSR166_CALLING_WAIT_RATHER_THAN_AWAIT")||type.equals("TLW_TWO_LOCK_NOTIFY")||type.equals("DM_USELESS_THREAD")||type.equals("RS_READOBJECT_SYNC")
                        ||type.equals("VO_VOLATILE_INCREMENT")||type.equals("MWN_MISMATCHED_NOTIFY")||type.equals("DC_PARTIALLY_CONSTRUCTED")||type.equals("DL_SYNCHRONIZATION_ON_UNSHARED_BOXED_PRIMITIVE")
                        ||type.equals("UW_UNCOND_WAIT")||type.equals("ESync_EMPTY_SYNC")||type.equals("WA_AWAIT_NOT_IN_LOOP")||type.equals("VO_VOLATILE_REFERENCE_TO_ARRAY")||type.equals("SC_START_IN_CTOR")
                        ||type.equals("LI_LAZY_INIT_STATIC")||type.equals("DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE")||type.equals("MSF_MUTABLE_SERVLET_FIELD")||type.equals("DC_DOUBLECHECK")||type.equals("DM_MONITOR_WAIT_ON_CONDITION")
                        ||type.equals("RV_RETURN_VALUE_OF_PUTIFABSENT_IGNORED")||type.equals("STCAL_STATIC_CALENDAR_INSTANCE")||type.equals("DL_SYNCHRONIZATION_ON_SHARED_CONSTANT")||type.equals("WS_WRITEOBJECT_SYNC")
                        ||type.equals("UL_UNRELEASED_LOCK_EXCEPTION_PATH")||type.equals("IS_INCONSISTENT_SYNC")||type.equals("DL_SYNCHRONIZATION_ON_BOOLEAN")||type.equals("IS_FIELD_NOT_GUARDED")||type.equals("LI_LAZY_INIT_INSTANCE")
                        ||type.equals("TLW_TWO_LOCK_WAIT")||type.equals("JLM_JSR166_LOCK_MONITORENTER")||type.equals("RU_INVOKE_RUN")||type.equals("STCAL_STATIC_SIMPLE_DATE_FORMAT_INSTANCE")||type.equals("STCAL_INVOKE_ON_STATIC_DATE_FORMAT_INSTANCE")
                        ||type.equals("JLM_JSR166_UTILCONCURRENT_MONITORENTER")||type.equals("MWN_MISMATCHED_WAIT")||type.equals("UG_SYNC_SET_UNSYNC_GET")||type.equals("WL_USING_GETCLASS_RATHER_THAN_CLASS_LITERAL")||type.equals("LI_LAZY_INIT_UPDATE_STATIC")
                        ||type.equals("WA_NOT_IN_LOOP")||type.equals("SWL_SLEEP_WITH_LOCK_HELD")||type.equals("AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION")||type.equals("IS2_INCONSISTENT_SYNC")||type.equals("STCAL_INVOKE_ON_STATIC_CALENDAR_INSTANCE")){
            return BUG_TYPE.SYNCHRONIZATION;
        }else if (type.equals("EQ_GETCLASS_AND_CLASS_CONSTANT")||type.equals("UI_INHERITANCE_UNSAFE_GETRESOURCE")||type.equals("HE_INHERITS_EQUALS_USE_HASHCODE")||type.equals("RI_REDUNDANT_INTERFACES")
                ||type.equals("SE_PRIVATE_READ_RESOLVE_NOT_INHERITED")||type.equals("HE_SIGNATURE_DECLARES_HASHING_OF_UNHASHABLE_CLASS")||type.equals("EQ_SELF_USE_OBJECT")||type.equals("EQ_ALWAYS_FALSE")
                ||type.equals("DMI_FUTILE_ATTEMPT_TO_CHANGE_MAXPOOL_SIZE_OF_SCHEDULED_THREAD_POOL_EXECUTOR")||type.equals("EQ_COMPARETO_USE_OBJECT_EQUALS")||type.equals("URV_INHERITED_METHOD_WITH_RELATED_TYPES")
                ||type.equals("ITC_INHERITANCE_TYPE_CHECKING")||type.equals("IPU_IMPROPER_PROPERTIES_USE_SETPROPERTY")||type.equals("CI_CONFUSED_INHERITANCE")||type.equals("HE_EQUALS_USE_HASHCODE")||type.equals("EQ_OTHER_NO_OBJECT")
                ||type.equals("HE_HASHCODE_USE_OBJECT_EQUALS")||type.equals("IA_AMBIGUOUS_INVOCATION_OF_INHERITED_OR_OUTER_METHOD")){
            return BUG_TYPE.INHERITANCE;
        }else {
            return BUG_TYPE.ANOTHER_TYPE;
        }
    }

    @Override
    public String getBugMessage() {
        return message;
    }

    public String getClassName() {
        int index_Classname = sourcePath.lastIndexOf("/") + 1;
        String className = sourcePath.substring(index_Classname, sourcePath.length());
        return className;
    }

    @Override
    public int getBugLineNumber() {
        return Integer.parseInt(startLine);
    }

    @Override
    public String getSourcePath() {
        return sourcePath;
    }

    @Override
    public ToolCollection getToolName() {
        return ToolCollection.FINDBUGS;
    }

}
