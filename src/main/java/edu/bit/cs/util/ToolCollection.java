package edu.bit.cs.util;

import org.sonar.plugins.findbugs.FindbugsExecutor;

public enum ToolCollection {
    FINDBUGS(1, "[FindBugs] "),
    JLINT(2, "[Jlint] "),
    INFER(4, "[Infer] ");

    private int id;
    private String name;

    ToolCollection(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //    private static final String FINDBUGS = "[FindBugs] ";
//    private static final String JLINT = "[Jlint] ";
//    private static final String Infer = "[Infer] ";

    private static String[] tools = new String[]{
            "",
            FINDBUGS.getName(),
            JLINT.getName(),
            FINDBUGS.getName() + JLINT.getName(),
            INFER.getName(),
            FINDBUGS.getName() + INFER.getName(),
            JLINT.getName() + INFER.getName(),
            FINDBUGS.getName() + JLINT.getName() + INFER.getName(),
    };

    public static int addTool(int index, ToolCollection tool) {
        switch (tool) {
            case FINDBUGS: {
                return index | FINDBUGS.getId();
            }
            case JLINT: {
                return index | JLINT.getId();
            }
            case INFER: {
                return index | INFER.getId();
            }
            default: {
                return index;
            }
        }
    }

    public static String getTools(int index) {
        return tools[index];
    }

    public static int getSize() {
        return tools.length;
    }

}
