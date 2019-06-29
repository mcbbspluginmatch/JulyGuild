package com.github.julyss2019.mcsp.julyguild.guild.player;

public enum Permission {
    MEMBER("成员", "&a", 0), ADMIN("管理员", "&c",1), OWNER("宗主", "&d",2);

    String chineseName;
    String color;
    int level;

    Permission(String chineseName, String color, int level) {
        this.chineseName = chineseName;
        this.color = color;
        this.level = level;
    }

    public String getChineseName() {
        return chineseName;
    }

    public int getLevel() {
        return level;
    }

    public String getColor() {
        return color;
    }
}
