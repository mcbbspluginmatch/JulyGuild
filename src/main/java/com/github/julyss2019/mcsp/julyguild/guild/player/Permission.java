package com.github.julyss2019.mcsp.julyguild.guild.player;

public enum Permission {
    MEMBER("成员"), ADMIN("管理员"), OWNER("宗主");

    String chineseName;

    Permission(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }
}
