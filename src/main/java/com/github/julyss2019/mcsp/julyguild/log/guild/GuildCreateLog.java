package com.github.julyss2019.mcsp.julyguild.log.guild;

import com.github.julyss2019.mcsp.julyguild.log.BaseGuildLog;
import com.github.julyss2019.mcsp.julyguild.log.GuildLogType;

public class GuildCreateLog extends BaseGuildLog {
    private String owner;
    private String guildName;

    public GuildCreateLog(String guildUUID, String guildName, String owner) {
        super(GuildLogType.CREATE, guildUUID);

        this.owner = owner;
        this.guildName = guildName;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
