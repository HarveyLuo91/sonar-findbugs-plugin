package edu.bit.cs.util;

public enum ToolCollection {
    NONE(0, "none"),
    FINDBUGS(1, "[FindBugs] "),
    JLINT(2, "[Jlint] "),
    INFER(4, "[Infer] "),
    BIT(8,"[BIT]");

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
            "",
            FINDBUGS.getName(),//0001 //1
            JLINT.getName(),//0010  //2
            FINDBUGS.getName() + JLINT.getName(),//0011 //3
            INFER.getName(),//0100 //4
            FINDBUGS.getName() + INFER.getName(),//0101 //5
            JLINT.getName() + INFER.getName(),//0110    //6
            FINDBUGS.getName() + JLINT.getName() + INFER.getName(),//0111  //7
            BIT.getName(),//1000 //8
            FINDBUGS.getName() + BIT.getName(), // 1001 //9
            INFER.getName() + BIT.getName(), //1100  //12
            FINDBUGS.getName() + INFER.getName() + BIT.getName(), //1101 //13
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
            case BIT: {
                return index | BIT.getId();
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
