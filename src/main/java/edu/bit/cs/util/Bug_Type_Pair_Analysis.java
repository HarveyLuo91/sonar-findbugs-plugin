package edu.bit.cs.util;

public class Bug_Type_Pair_Analysis {
    //F-------Findbugs
    //I-------Infer
    //J-------Jlint
    //FT------Fortify

    public int F_npe = 0;
    public int I_npe = 0;
    public int J_npe = 0;
    public int FT_npe = 0;
    public int J_I_npe = 0;
    public int F_J_npe = 0;
    public int F_I_npe = 0;
    public int F_J_I_npe = 0;


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


    public int F_sync = 0;
    public int J_sync = 0;
    public int I_sync = 0;
    public int J_I_sync = 0;
    public int F_J_sync = 0;
    public int F_I_sync = 0;
    public int F_J_I_sync = 0;

    public int F_injc = 0;
    public int B_injc = 0;
    public int F_B_injc = 0;

    public int F_xss = 0;
    public int B_xss = 0; //BIT
    public int F_B_xss = 0;

    public int F_inhrit = 0;
    public int J_inhrit = 0;
    public int F_J_inhrit = 0;

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
}
