package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;

public class GuildOwner extends GuildAdmin {
    public GuildOwner(Guild guild, GuildPlayer player) {
        super(guild, player, guild.getYml().getConfigurationSection("owner"));
    }

    @Override
    public Permission getPermission() {
        return Permission.OWNER;
    }
}
