package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

public class GuildOwner extends GuildAdmin {
    public GuildOwner(OfflineGuildPlayer player) {
        super(player);
    }

    @Override
    public Permission getPermission() {
        return Permission.OWNER;
    }
}
