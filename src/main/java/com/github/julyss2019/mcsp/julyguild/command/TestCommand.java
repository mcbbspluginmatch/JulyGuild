package com.github.julyss2019.mcsp.julyguild.command;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements Command {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildManager guildManager = plugin.getGuildManager();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();

    @Override
    public boolean onCommand(CommandSender cs, String[] strings) {
        GuildPlayer guildPlayer = guildPlayerManager.getGuildPlayer((Player) cs);

        for (int i = 0; i < 53; i++) {
            guildManager.createGuild(guildPlayer, "w");
        }

        int i = 0;

        for (Guild guild : guildManager.getGuilds(false)) {
            guild.setLevel(i++);
        }

        return true;
    }

    @Override
    public String getFirstArg() {
        return "test";
    }

    @Override
    public String getPermission() {
        return "JulyGuild.admin";
    }
}
