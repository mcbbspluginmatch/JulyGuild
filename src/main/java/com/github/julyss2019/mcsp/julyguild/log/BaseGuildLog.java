package com.github.julyss2019.mcsp.julyguild.log;

public class BaseGuildLog implements GuildLog {
    private GuildLogType type;
    private Long creationTime;
    private String guildUUID;

    public BaseGuildLog(GuildLogType type, String guildUUID) {
        this.type = type;
        this.creationTime = System.currentTimeMillis();
        this.guildUUID = guildUUID;
    }

    public BaseGuildLog(GuildLogType type, String guildUUID, Long creationTime) {
        this(type, guildUUID);
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

    @Override
    public String getGuildUUID() {
        return guildUUID;
    }
}
