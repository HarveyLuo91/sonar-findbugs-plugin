package org.sonar.plugins.findbugs.rules;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.plugins.java.Java;

public class FbJlintRulesDefinition implements RulesDefinition {
    public static final String REPOSITORY_KEY = "fb-jlint";
    public static final String REPOSITORY_NAME = "FindBugs Jlint";
    public static final int RULE_COUNT = 283;
    public static final int DEACTIVED_RULE_COUNT = 0;

    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(REPOSITORY_KEY, Java.KEY)
                .setName(REPOSITORY_NAME);

        RulesDefinitionXmlLoader ruleLoader = new RulesDefinitionXmlLoader();
        ruleLoader.load(repository, FbContribRulesDefinition.class.getResourceAsStream("/org/sonar/plugins/findbugs/rules-fbcontrib.xml"), "UTF-8");
        repository.done();
    }
}
