package com.github.julyss2019.mcsp.julyguild.log;

public class BaseGuildLog implements GuildLog {
    private GuildLogType type;
    private Long creationTime;

    public BaseGuildLog(GuildLogType type) {
        this.type = type;
        this.creationTime = System.currentTimeMillis();
    }

    public BaseGuildLog(GuildLogType type, Long creationTime) {
        this.type = type;
        this.creationTime = creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public GuildLogType getType() {
        return type;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }
}
