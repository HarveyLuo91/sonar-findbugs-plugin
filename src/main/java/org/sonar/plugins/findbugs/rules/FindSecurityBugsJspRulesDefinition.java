package org.sonar.plugins.findbugs.rules;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.plugins.findbugs.language.Jsp;

/**
 * This RulesDefinition build a separate repository from the FindSecurityBugsRulesDefinition to allow a separate ruleset
 * for JSP language.
 * @see FindSecurityBugsRulesDefinition
 */
public class FindSecurityBugsJspRulesDefinition implements RulesDefinition {

    public static final String REPOSITORY_KEY = "findsecbugs-jsp";
    public static final String REPOSITORY_JSP_NAME = "Find Security Bugs (JSP)";

    @Override
    public void define(Context context) {
        NewRepository repositoryJsp = context
                .createRepository(REPOSITORY_KEY, Jsp.KEY)
                .setName(REPOSITORY_JSP_NAME);

        RulesDefinitionXmlLoader ruleLoaderJsp = new RulesDefinitionXmlLoader();
        ruleLoaderJsp.load(repositoryJsp, FindSecurityBugsRulesDefinition.class.getResourceAsStream("/org/sonar/plugins/findbugs/rules-jsp.xml"), "UTF-8");
        repositoryJsp.done();
    }
}
