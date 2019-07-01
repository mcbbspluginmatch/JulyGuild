package com.github.julyss2019.mcsp.julyguild.log.guild;

import com.github.julyss2019.mcsp.julyguild.log.BaseGuildLog;
import com.github.julyss2019.mcsp.julyguild.log.GuildLogType;

public class GuildCreateGuildLog extends BaseGuildLog {
    private String guildUUID;
    private String owner;
    private String guildName;

    public GuildCreateGuildLog(String guildUUID, String guildName, String owner) {
        super(GuildLogType.CREATE);
        this.guildUUID = guildUUID;
        this.owner = owner;
        this.guildName = guildName;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getGuildUUID() {
        return guildUUID;
    }

    public void setGuildUUID(String guildUUID) {
        this.guildUUID = guildUUID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
