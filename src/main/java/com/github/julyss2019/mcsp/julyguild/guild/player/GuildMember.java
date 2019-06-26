package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

public class GuildMember {
    private OfflineGuildPlayer offlineGuildPlayer;

    public GuildMember(OfflineGuildPlayer player) {
        this.offlineGuildPlayer = player;
    }

    public OfflineGuildPlayer getOfflineGuildPlayer() {
        return offlineGuildPlayer;
    }

    public String getName() {
        return getOfflineGuildPlayer().getName();
    }

    public Permission getPermission() {
        return Permission.MEMBER;
    }
}
