package edu.bit.cs.Findbugs;

import com.google.common.collect.Sets;
import edu.bit.cs.ReportedBugInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FindbugsReportParser {

    /*
    *
    * Parse the xml reported bugs for find bugs template
    *
    * */
    public static Collection<ReportedBugInfo> getReportedBugs() {
        Collection<ReportedBugInfo> bugs = Sets.newHashSet();
        try {
            File XmlFile = new File("src/main/resources/file/findbugs-result.xml");

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(XmlFile);

            doc.getDocumentElement().normalize();
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nodeList = doc.getElementsByTagName("BugInstance");
            //now XML is loaded as Document in memory, lets convert it to Object List

            for (int i = 0; i < nodeList.getLength(); i++) {
                bugs.add(getBugInstance(nodeList.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bugs;
    }

    /*
    *
    * get a single Node from the Xml tree
    *
    * */
    private static ReportedBugInfo getBugInstance(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        ReportedBugInfo bugInstance = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String bugType = ((Element) node).getAttribute("type");
            String message = getTagValue("ShortMessage", element);

            NodeList sourceLine =  element.getElementsByTagName("SourceLine");
            String sourceFile = sourceLine.item(0).getAttributes().getNamedItem("sourcefile").getNodeValue();
            String sourcePath = sourceLine.item(0).getAttributes().getNamedItem("sourcepath").getNodeValue();
            String startLine = sourceLine.item(0).getAttributes().getNamedItem("start").getNodeValue();
            bugInstance = new FindbugsReportedBugFromXML(message, sourcePath, startLine, bugType);
            System.out.println(bugInstance.getUID());
        }
        return bugInstance;
    }

    /*
    *
    * Print a Single rule
    *
    * */
    public static void print(ReportedBugInfo bugInstance) {
        System.out.println("Bug Messgae: " + bugInstance.getBugMessage());
        System.out.println("Class Name: " + bugInstance.getClassName());
        System.out.println("Bug Type: " + bugInstance.getBugType());
        System.out.println("Tool Name: " + bugInstance.getToolName());
        System.out.println("Source Path: " + bugInstance.getSourcePath());

        System.out.println();
    }

    /*
    *
    * get a tag that has only one occurence
    *
    * */
    private static String getTagValue(String tag, Element element) {
        HashMap<String,String > tagAttributes =  new HashMap<String,String>();

        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        int numberNodes = nodeList.getLength();
        for (int i = 0; i < numberNodes; i++) {
            Node node = (Node) nodeList.item(i);
            //System.out.println(node);
        }
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}
