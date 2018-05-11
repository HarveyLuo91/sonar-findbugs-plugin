package edu.bit.cs.findbugs;

import java.util.List;

import edu.bit.cs.ReportedBugInfo;
import edu.bit.cs.ReportedInfoProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;

public class findBugReportParser implements ReportedInfoProcessor {
    @Override
    public Collection<? extends ReportedBugInfo> getReportedBugs(BufferedReader br){
        ArrayList<findbugsReportedBug>bugs = new ArrayList<findbugsReportedBug>();
        SAXReader reader = new SAXReader();
        try{
            Document document = reader.read(br);
            Element bugRoot = document.getRootElement();
            Iterator it = bugRoot.elementIterator();
            String p = "";
            while(it.hasNext()) {
                String type="";
                String message="";
                String className="";
                int bugLineNumber=0;
                String sourcePath="";
                Element bug = (Element) it.next();
                if(bug.getName().equals("Project")){
                    Iterator proIt = bug.elementIterator();
                    while(proIt.hasNext()){
                        Element path = (Element)proIt.next();
                        if(path.getName().equals("Jar")){
                            p = path.getStringValue();
                        }
                    }
                }
                if (!bug.getName().equals("BugInstance"))
                    continue;
                List<Attribute> instanceAttr = bug.attributes();
                for (Attribute attr : instanceAttr) {
                    if (attr.getName().equals("type"))
                        type = attr.getValue();
                }
                Iterator itt = bug.elementIterator();
                while (itt.hasNext()) {
                    Element el = (Element) itt.next();
                    if (el.getName().equals("ShortMessage"))
                        message = el.getStringValue();
                    if (el.getName().equals("Class")) {
                        List<Attribute> ClassAttr = el.attributes();
                        for (Attribute attr : ClassAttr) {
                            if (attr.getName().equals("classname"))
                                className = attr.getValue();
                        }
                    }
                    if (el.getName().equals("SourceLine")) {
                        List<Attribute> at = el.attributes();
                        for (Attribute a : at) {
                            if (a.getName().equals("start")) {
                                bugLineNumber = Integer.parseInt(a.getValue());
                            }
                        }
                    }
                }
                p = p.replaceAll("\\\\", "/");
                String[] pa = p.split("/");
                int le= pa.length;
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<le-1;i++)
                    sb.append(pa[i]+"/");
                String[] classes = className.split("[.]");
                le = classes.length;
                for(int i=0;i<le-1;i++)
                    sb.append(classes[i]+"/");
                sourcePath = sb.toString();
                findbugsReportedBug BugInfo = new findbugsReportedBug(type, message, className,bugLineNumber, sourcePath);
                bugs.add(BugInfo);
            }
            return bugs;
        }catch (DocumentException e){
            e.printStackTrace();
        }
        return null;
    }
}
