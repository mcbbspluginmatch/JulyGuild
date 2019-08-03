package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;

// 无意义的类继承关系，可以在同一个类中通过不同的构造方法参数完成不同用户区分 —— 754503921
public class GuildOwner extends GuildAdmin {
    public GuildOwner(Guild guild, GuildPlayer player) {
        super(guild, player, guild.getYml().getConfigurationSection("owner"));
    }

    @Override
    public Permission getPermission() {
        return Permission.OWNER;
    }
}
