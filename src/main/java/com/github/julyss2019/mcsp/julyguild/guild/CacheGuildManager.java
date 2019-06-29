package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;

import java.util.List;

public class CacheGuildManager {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildManager guildManager = plugin.getGuildManager();
    private List<Guild> sortedGuilds;

    public CacheGuildManager() {

    }

    public void updateSortedGuilds() {
        this.sortedGuilds = guildManager.getSortedGuilds();
    }

    public List<Guild> getSortedGuilds() {
        return sortedGuilds;
    }

    public int getRanking(Guild guild) {
        return sortedGuilds.indexOf(guild) + 1;
    }
}
