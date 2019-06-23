package com.github.julyss2019.mcsp.julyguild.guild.log;

import com.github.julyss2019.mcsp.julyguild.log.GuildLog;
import com.github.julyss2019.mcsp.julyguild.log.GuildLogType;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class GuildCreateLog implements GuildLog {
    private long creationTime;
    private String uuid;
    private String owner;
    private String guildName;
    private Boolean success;

    public GuildCreateLog(long creationTime, String uuid, String owner, String guildName, Boolean success) {
        this.creationTime = creationTime;
        this.uuid = uuid;
        this.owner = owner;
        this.guildName = guildName;
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public GuildLogType getType() {
        return GuildLogType.CREATE;
    }
}
