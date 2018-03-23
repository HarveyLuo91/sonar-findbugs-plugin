package edu.bit.cs.assessment;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.bit.cs.ReportedBugInfo;

import java.util.List;
import java.util.Map;

public class Result {
    private String name;

    private Map<String, List<ReportedBugInfo>> tp;
    private Map<String, List<ReportedBugInfo>> fp;

//    private Map<String, List<String>> hitBugTags;

    private Map<String, Integer> tags;

    public Result(String name) {
        this.name = name;
        tp = Maps.newHashMap();
        fp = Maps.newHashMap();
//        hitBugTags = Maps.newHashMap();
        tags = Maps.newHashMap();
    }

    public void judge(ReportedBugInfo bugInfo) {
        Preconditions.checkNotNull(bugInfo);
//        if (CsvParser.FAKEBUGS.containsKey(bugInfo.getUID())) {
//            if (!tp.containsKey(bugInfo.getUID())) {
//                tp.put(bugInfo.getUID(), Lists.newArrayList());
//            }
//            List<ReportedBugInfo> reportedBugInfos = tp.get(bugInfo.getUID());
//            reportedBugInfos.add(bugInfo);
////            hitBugTags.put(bugInfo.getUID(), CsvParser.FAKEBUGS.get(bugInfo.getUID()));
//            //sorting out and counting the times of tag appearance
//
//            for (String tag : CsvParser.FAKEBUGS.get(bugInfo.getUID())) {
//                if (!tags.containsKey(tag)) {
//                    tags.put(tag, 1);
//                } else {
//                    tags.put(tag, tags.get(tag) + 1);
//                }
//            }
        if (CsvParser.F_BUGS.containsKey(bugInfo.getUID())) {
            if (!tp.containsKey(bugInfo.getUID())) {
                tp.put(bugInfo.getUID(), Lists.newArrayList());
            }
            List<ReportedBugInfo> reportedBugInfos = tp.get(bugInfo.getUID());
            reportedBugInfos.add(bugInfo);
//            hitBugTags.put(bugInfo.getUID(), CsvParser.FAKEBUGS.get(bugInfo.getUID()));
            //sorting out and counting the times of tag appearance

//            for (String tag : CsvParser.FAKEBUGS.get(bugInfo.getUID())) {
//                if (!tags.containsKey(tag)) {
//                    tags.put(tag, 1);
//                } else {
//                    tags.put(tag, tags.get(tag) + 1);
//                }
//            }


        } else {
            if (!fp.containsKey(bugInfo.getUID())) {
                fp.put(bugInfo.getUID(), Lists.newArrayList());
            }
            List<ReportedBugInfo> reportedBugInfos = fp.get(bugInfo.getUID());
            reportedBugInfos.add(bugInfo);
        }
    }

    public void judge(List<ReportedBugInfo> bugInfos) {
        for (ReportedBugInfo bug : bugInfos) {
            judge(bug);
        }
    }

    @Override
    public String toString() {
        return "\n" +
                "Name: " + this.getName() + "\n" +
                "Bug Number: " + (this.getFp().size() + this.getTp().size()) + "\n" +
                "TP: " + this.getTp().size() + "\n" +
                "FP: " + this.getFp().size() + "\n" +
                "precision: " + this.getPrecision() + "\n" +
                "recall: " + this.getRecall() + "\n";
    }

    public void printTags() {
        for (String key : tags.keySet()) {
            System.out.println(key + ":" + tags.get(key));
        }
    }


    public String getName() {
        return name;
    }

    public double getPrecision() {
        return ((double) tp.size()) / (tp.size() + fp.size());
    }

    public double getRecall() {
        return ((double) tp.size()) / CsvParser.FAKEBUGS.size();
    }

    public Map<String, List<ReportedBugInfo>> getTp() {
        return tp;
    }

    public Map<String, List<ReportedBugInfo>> getFp() {
        return fp;
    }

    public Map<String, Integer> getTags() {
        return tags;
    }

    public void countTags() {
        for (String uid : tp.keySet())
            for (String tag : CsvParser.FAKEBUGS.get(uid)) {
                if (!tags.containsKey(tag)) {
                    tags.put(tag, 1);
                } else {
                    tags.put(tag, tags.get(tag) + 1);
                }
            }
    }
}
