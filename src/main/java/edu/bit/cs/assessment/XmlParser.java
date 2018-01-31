package edu.bit.cs.assessment;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import com.google.common.collect.Sets;
import edu.bit.cs.assessment.findbugs.FindBugs_Rule;
import org.w3c.dom.*;

import java.io.File;
import java.util.Set;

public class XmlParser {


    /*
    *
    * Parse the xml rule files for find bugs
    *
    * */
    public static Set<FindBugs_Rule> parseFindBugsRules(String file){
        Set<FindBugs_Rule> findbugs_rules = Sets.newHashSet();
        try{
            File XmlFile = new File(file);

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(XmlFile);

            doc.getDocumentElement().normalize();
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nodeList = doc.getElementsByTagName("rule");
            //now XML is loaded as Document in memory, lets convert it to Object List

            for (int i = 0; i < nodeList.getLength(); i++) {
                findbugs_rules.add(getRule(nodeList.item(i)));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return findbugs_rules;
    }
    /*
    *
    * get a single Node from the Xml tree
    *
    * */
    private static FindBugs_Rule getRule(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        FindBugs_Rule findBugs_rule = new FindBugs_Rule();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            findBugs_rule = new FindBugs_Rule(getTagValue("name", element),getTagValue("configKey", element),element.getAttribute("priority"),getTagValue("description", element),getTagsValue("tag", element));

        }
        return findBugs_rule;
    }

    /*
    *
    * Print a Single rule
    *
    * */
    public static void print(FindBugs_Rule findBugs_rule){
        System.out.println("name: " + findBugs_rule.getName());
        System.out.println("Key: " + findBugs_rule.getRule_configKey());
        System.out.println("Priority: " + findBugs_rule.getRule_priority());
        System.out.println("description: " + findBugs_rule.getRule_description());
        for (String tag: findBugs_rule.getTags()) {
            System.out.println("tag: " + tag);
        }
        System.out.println();
    }
    /*
    *
    * get a tag that has only one occurence
    *
    * */
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
    /*
     *
     * get tags that have multiple occurences
     *
     * */
    private static ArrayList getTagsValue(String tag, Element element) {
        ArrayList<Node> node_tags = new ArrayList();
        ArrayList<String> string_tags = new ArrayList();

        NodeList nodeList = element.getElementsByTagName(tag);

        for (int h = 0; h < nodeList.getLength() ; h++) {
            NodeList child = nodeList.item(h).getChildNodes();

            for (int i = 0; i < child.getLength(); i++) {
                string_tags.add(child.item(i).getNodeValue());

                for (int k = 0; k < node_tags.size() ; k++) {
                    Node node = (Node) nodeList.item(i);
                    System.out.println(node.getNodeValue());
                }
            }
        }
        string_tags.trimToSize();
        return string_tags;
    }


}


