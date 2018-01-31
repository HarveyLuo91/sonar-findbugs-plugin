package edu.bit.cs.assessment.findbugs;

import edu.bit.cs.BUG_TYPE;

import java.util.ArrayList;

public class FindBugs_Rule{

    private String name;
    private String rule_configKey;
    private String rule_description;
    private String rule_priority;
    private ArrayList<String> tags;

    private BUG_TYPE BugType;

    public FindBugs_Rule(String name, String rule_configKey, String rule_priority, String rule_description, ArrayList<String> tags){
        this.name = name;
        this.rule_configKey = rule_configKey;
        this.rule_priority = rule_priority;
        this.rule_description = rule_description;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRule_priority() {
        return rule_priority;
    }
    public String getRule_configKey() {
        return rule_configKey;
    }
    public String getRule_description() {
        return rule_description;
    }
    public ArrayList<String> getTags() {
        return tags;
    }
    public FindBugs_Rule(){
        this.tags = new ArrayList<String>();
    }

    public BUG_TYPE judgetype(){

        if(this.rule_configKey.contains("SYNCHRONIZATION") || this.name.contains("synchronization")   || this.rule_description.contains("synchronization") ){

            return BUG_TYPE.SYNCHRONIZATION;
        }

        return BUG_TYPE.ANOTHER_TYPE;
    }


}