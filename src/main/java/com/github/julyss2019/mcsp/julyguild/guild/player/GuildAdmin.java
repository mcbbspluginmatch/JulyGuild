package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import org.bukkit.configuration.ConfigurationSection;

public class GuildAdmin extends GuildMember {
    public GuildAdmin(Guild guild, GuildPlayer player) {
        super(guild, player);
    }

    public GuildAdmin(Guild guild, GuildPlayer player, ConfigurationSection memberSection) {
        super(guild, player, memberSection);
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }
}
