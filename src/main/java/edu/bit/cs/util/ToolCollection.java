package edu.bit.cs.util;

public enum ToolCollection {
    NONE(0, "none"),
    FINDBUGS(1, "[FindBugs] "),
    JLINT(2, "[Jlint] "),
    INFER(4, "[Infer] "),
    COVERITY(8, "[Coverity] "),
    FORTIFY(16, "[FORTIFY] ");

    private int id;
    private String name;

    ToolCollection(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private static String[] tools = new String[]{
            "",//0
            FINDBUGS.getName(),//1
            JLINT.getName(),//2
            FINDBUGS.getName() + JLINT.getName(),//3
            INFER.getName(),//4
            FINDBUGS.getName() + INFER.getName(),//5
            JLINT.getName() + INFER.getName(),//6
            FINDBUGS.getName() + JLINT.getName() + INFER.getName(),//7
            COVERITY.getName(),//8
            FINDBUGS.getName() + COVERITY.getName(),//9
            JLINT.getName() + COVERITY.getName(),//10
            FINDBUGS.getName() + JLINT.getName() + COVERITY.getName(),//11
            INFER.getName() + COVERITY.getName(),//12
            FINDBUGS.getName() + INFER.getName() + COVERITY.getName(),//13
            JLINT.getName() + INFER.getName() + COVERITY.getName(),//14
            FINDBUGS.getName() + JLINT.getName() + INFER.getName() + COVERITY.getName(),//15
            FORTIFY.getName(),//16
            FINDBUGS.getName() + FORTIFY.getName(),//17
            JLINT.getName() + FORTIFY.getName(),//18
            FINDBUGS.getName() + JLINT.getName() + FORTIFY.getName(),//19
            INFER.getName() + FORTIFY.getName(),//20
            FINDBUGS.getName() + INFER.getName() + FORTIFY.getName(),//21
            JLINT.getName() + INFER.getName() + FORTIFY.getName(),//22
            FINDBUGS.getName() + JLINT.getName() + INFER.getName() + FORTIFY.getName(),//23
            COVERITY.getName() + FORTIFY.getName(),//24
            FINDBUGS.getName() + COVERITY.getName() + FORTIFY.getName(),//25
            JLINT.getName() + COVERITY.getName() + FORTIFY.getName(),//26
            FINDBUGS.getName() + JLINT.getName() + COVERITY.getName() + FORTIFY.getName(),//27
            INFER.getName() + COVERITY.getName() + FORTIFY.getName(),//28
            FINDBUGS.getName() + INFER.getName() + COVERITY.getName() + FORTIFY.getName(),//29
            JLINT.getName() + INFER.getName() + COVERITY.getName() + FORTIFY.getName(),//30
            FINDBUGS.getName() + JLINT.getName() + INFER.getName() + COVERITY.getName() + FORTIFY.getName()//31
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
            case FORTIFY: {
                return index | FORTIFY.getId();
            }
            case COVERITY: {
                return index | COVERITY.getId();
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
