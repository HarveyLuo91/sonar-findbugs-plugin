package edu.bit.cs.util;

import edu.bit.cs.BUG_TYPE;
import edu.bit.cs.ReportedBugInfo;

import java.util.List;
import java.util.Map;

public class Bug_Type_Pair_Analysis {
    //F-------Findbugs
    //I-------Infer
    //J-------Jlint
    //FT------Fortify

    ////////////////////////NPE///////////////////////////////
    public int F_npe = 0;
    public int I_npe = 0;
    public int J_npe = 0;
    public int FT_npe = 0;
    public int F_J_npe = 0;
    public int F_I_npe = 0;
    public int F_FT_npe = 0;
    public int J_I_npe = 0;
    public int J_FT_npe = 0;
    public int I_FT_npe = 0;
    public int F_J_I_FT_npe = 0;

    ////////////////////////RL///////////////////////////////
    public int F_rl = 0;
    public int I_rl = 0;
    public int B_rl = 0;
    public int FT_rl = 0;
    public int F_I_rl = 0;//F_+
    public int F_B_rl = 0;
    public int F_FT_rl = 0;
    public int I_B_rl = 0;//I_+
    public int I_FT_rl = 0;
    public int B_FT_rl = 0;//B_+

    public int F_I_B_rl = 0;
    public int F_I_FT_rl = 0;
    public int F_B_FT_rl = 0;
    public int I_B_FT_rl = 0;

    public int F_I_B_FT_rl = 0;

    ////////////////////////SYNC///////////////////////////////
    public int F_sync = 0;
    public int J_sync = 0;
    public int I_sync = 0;
    public int FT_sync = 0;
    public int F_J_sync = 0;
    public int F_I_sync = 0;
    public int F_FT_sync = 0;
    public int J_I_sync = 0;
    public int J_FT_sync = 0;
    public int I_FT_sync = 0;

    public int F_J_I_FT_sync = 0;
    public int F_J_I_sync = 0;
    public int F_J_FT_sync = 0;
    public int F_I_FT_sync = 0;
    public int J_I_FT_sync = 0;



    ////////////////////////INJC///////////////////////////////
    public int F_injc = 0;
    public int B_injc = 0;
    public int FT_injc = 0;
    public int F_B_injc = 0;
    public int F_FT_injc = 0;

    ////////////////////////XSS///////////////////////////////
    public int F_xss = 0;
    public int B_xss = 0;
    public int F_B_xss = 0;

    ////////////////////////INHERIT///////////////////////////////
    public int F_inhrit = 0;
    public int J_inhrit = 0;
    public int F_J_inhrit = 0;

    ////////////////////////OTHERS///////////////////////////////
    public int F_other = 0;
    public int J_other = 0;
    public int I_other = 0;
    public int B_other = 0;
    public int FT_other = 0;
    public int J_I_other = 0;
    public int F_J_other = 0;
    public int F_I_other = 0;
    public int F_B_other = 0;
    public int F_J_I_other = 0;

    public static void analyze(Map<String, List<ReportedBugInfo>> bugs){
        Bug_Type_Pair_Analysis analysis = new Bug_Type_Pair_Analysis();
        for (List<ReportedBugInfo> tb_list: bugs.values()) {//here we have a list containing occurences of a unique bug
            //System.out.println(tb_list.size());
            boolean findbugs = false;
            boolean infer    = false;
            boolean jlint    = false;
            boolean bit      = false;
            boolean fortify  = false;

            BUG_TYPE bug_type = null;

            for (ReportedBugInfo bug: tb_list) {
                bug_type = bug.getBugType();
                if(bug.getToolName() == ToolCollection.FINDBUGS){
                    findbugs = true;
                }else if(bug.getToolName() == ToolCollection.JLINT ){
                    jlint = true;
                }else if(bug.getToolName() == ToolCollection.INFER){
                    infer = true;
                }else if(bug.getToolName() == ToolCollection.BIT){
                    infer = true;
                }else if(bug.getToolName() == ToolCollection.FORTIFY){
                    infer = true;
                }
            }
            if(bug_type.equals(BUG_TYPE.NULL_POINTER_EXEPTION)){
                if(findbugs){
                    analysis.F_npe++;
                }
                if(infer){
                    analysis.I_npe++;
                }
                if(jlint){
                    analysis.J_npe++;
                }
                if(fortify){
                    analysis.FT_npe++;
                }
                if(findbugs && infer){
                    analysis.F_I_npe++;
                }
                if(findbugs && jlint){
                    analysis.F_J_npe++;
                }
                if(findbugs && fortify){
                    analysis.F_FT_npe++;
                }
                if(jlint && infer){
                    analysis.J_I_npe++;
                }
                if(jlint && fortify){
                    analysis.J_FT_npe++;
                }
                if(infer && fortify){
                    analysis.I_FT_npe++;
                }
                if(findbugs && jlint && infer){
                    analysis.F_J_I_FT_npe++;
                }
            }
            if(bug_type.equals(BUG_TYPE.INHERITANCE)){
                if(findbugs){
                    analysis.F_inhrit++;
                }
                if(jlint){
                    analysis.J_inhrit++;
                }
                if(findbugs && jlint){
                    analysis.F_J_inhrit++;
                }
            }
            if(bug_type.equals(BUG_TYPE.SYNCHRONIZATION)){
                if(findbugs){
                    analysis.F_sync++;
                }
                if(jlint){
                    analysis.J_sync++;
                }
                if(infer){
                    analysis.I_sync++;
                }
                if(fortify){
                    analysis.FT_sync++;
                }
                if(findbugs && fortify){
                    analysis.F_FT_sync++;
                }
                if(jlint && infer){
                    analysis.J_I_sync++;
                }
                if(jlint && fortify){
                    analysis.J_FT_sync++;
                }if(infer && fortify){
                    analysis.I_FT_sync++;
                }
                if(findbugs && jlint){
                    analysis.F_J_sync++;
                }
                if(findbugs && infer){
                    analysis.F_I_sync++;
                }
                if(findbugs && jlint && infer && fortify){
                    analysis.F_J_I_FT_sync++;
                }
            }
            if(bug_type.equals(BUG_TYPE.RESOURCE_LEAK)){
                if(findbugs){
                    analysis.F_rl++;
                }
                if(infer){
                    analysis.I_rl++;
                }
                if(bit){
                    analysis.B_rl++;
                }
                if(fortify){
                    analysis.FT_rl++;
                }
                if(findbugs && infer){
                    analysis.F_I_rl++;
                }
                if(findbugs && bit){
                    analysis.F_B_rl++;
                }
                if(findbugs && fortify){
                    analysis.F_FT_rl++;
                }
                if(infer && bit){
                    analysis.I_B_rl++;
                }
                if(infer && fortify){
                    analysis.I_FT_rl++;
                }
                if(bit && fortify){
                    analysis.B_FT_rl++;
                }
                if(findbugs && bit && infer && fortify){
                    analysis.F_I_B_FT_rl++;
                }
            }
            if(bug_type.equals(BUG_TYPE.INJECTION)){
                if(findbugs){
                    analysis.F_injc++;
                }
                if(bit){
                    analysis.B_injc++;
                }
                if(findbugs && bit) {
                    analysis.F_B_injc++;
                }
            }
            if(bug_type.equals(BUG_TYPE.CROSS_SITE_SCRIPTING)){
                if(findbugs){
                    analysis.F_xss++;
                }
                if(bit){
                    analysis.B_xss++;
                }
                if(findbugs && bit){
                    analysis.F_B_xss++;
                }
            }
            if(bug_type.equals(BUG_TYPE.ANOTHER_TYPE)){
                if(findbugs){
                    analysis.F_other++;
                }
                if(infer){
                    analysis.I_other++;
                }
                if(bit){
                    analysis.B_other++;
                }
                if(jlint){
                    analysis.J_other++;
                }
                if(findbugs && jlint){
                    analysis.F_J_other++;
                }
                if(findbugs && infer){
                    analysis.F_I_other++;
                }
                if(infer && jlint){
                    analysis.J_I_other++;
                }
                if(findbugs && infer && jlint){
                    analysis.F_J_I_other++;
                }
                if(findbugs && bit){
                    analysis.F_B_other++;
                }
            }
        }
        System.out.println("FINDBUGS-------------------F");
        System.out.println("INFER----------------------I");
        System.out.println("Jlint----------------------J");
        System.out.println("BIT------------------------B");
        System.out.println("\n");
        System.out.println("NPE------------------------F:" + analysis.F_npe);
        System.out.println("NPE------------------------I:"+ analysis.I_npe);
        System.out.println("NPE------------------------J:"+ analysis.J_npe);
        System.out.println("NPE------------------------F_J:"+ analysis.F_J_npe);
        System.out.println("NPE------------------------F_I:"+ analysis.F_I_npe);
        System.out.println("NPE------------------------J_I:"+ analysis.J_I_npe);
        System.out.println("NPE------------------------F_J_I_FT:"+ analysis.F_J_I_FT_npe);

        System.out.println("\n");
        System.out.println("RL-------------------------F:"+ analysis.F_rl);
        System.out.println("RL-------------------------I:"+ analysis.I_rl);
        System.out.println("RL-------------------------B:"+ analysis.B_rl);
        System.out.println("RL-------------------------F_I:"+ analysis.F_I_rl);
        System.out.println("RL-------------------------F_B:"+ analysis.F_B_rl);
        System.out.println("RL-------------------------F_B:"+ analysis.F_I_B_FT_rl);

        System.out.println("\n");
        System.out.println("SYNC-----------------------F:"+analysis.F_sync);
        System.out.println("SYNC-----------------------J:"+analysis.J_sync);
        System.out.println("SYNC-----------------------I:"+analysis.I_sync);
        System.out.println("SYNC-----------------------J_I:"+analysis.J_I_sync);
        System.out.println("SYNC-----------------------F_J:"+analysis.F_J_sync);
        System.out.println("SYNC-----------------------F_I:"+analysis.F_I_sync);
        System.out.println("SYNC-----------------------F_J_I_FT:"+analysis.F_J_I_FT_sync);

        System.out.println("\n");
        System.out.println("INHERIT--------------------F:"+analysis.F_inhrit);
        System.out.println("INHERIT--------------------J:"+analysis.J_inhrit);
        System.out.println("INHERIT--------------------F_J:"+analysis.F_J_inhrit);

        System.out.println("\n");
        System.out.println("INJC-----------------------F:"+analysis.F_injc);
        System.out.println("INJC-----------------------B:"+analysis.B_injc);
        System.out.println("INJC-----------------------F_B:"+analysis.F_B_injc);

        System.out.println("\n");
        System.out.println("XSS------------------------F:"+analysis.F_xss);
        System.out.println("XSS------------------------B:"+analysis.B_xss);
        System.out.println("XSS------------------------F_B:"+analysis.F_B_xss);

        System.out.println("\n");
        System.out.println("OTHERS---------------------F:"+analysis.F_other);
        System.out.println("OTHERS---------------------I:"+analysis.I_other);
        System.out.println("OTHERS---------------------J:"+analysis.J_other);
        System.out.println("OTHERS---------------------F_I:"+analysis.F_I_other);
        System.out.println("OTHERS---------------------F_J:"+analysis.F_J_other);
        System.out.println("OTHERS---------------------F_I_J:"+analysis.F_J_I_other);
        System.out.println("OTHERS---------------------J_I:"+analysis.F_I_other);
        System.out.println("OTHERS---------------------J_B:"+analysis.F_B_other);
        System.out.println("\n");
    }
}
