package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

public class GuildAdmin extends GuildMember {
    public GuildAdmin(Guild guild, OfflineGuildPlayer player) {
        super(guild, player);
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }
}
