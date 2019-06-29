package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import org.bukkit.configuration.ConfigurationSection;

public class GuildAdmin extends GuildMember {
    public GuildAdmin(Guild guild, OfflineGuildPlayer player) {
        super(guild, player);
    }

    public GuildAdmin(Guild guild, OfflineGuildPlayer player, ConfigurationSection memberSection) {
        super(guild, player, memberSection);
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }
}
