package com.github.julyss2019.mcsp.julyguild.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuildPlayerManager {
    private Map<String, GuildPlayer> guildPlayerMap = new HashMap<>();

    public GuildPlayer getGuildPlayer(String playerName) {
        if (!guildPlayerMap.containsKey(playerName)) {
            guildPlayerMap.put(playerName, new GuildPlayer(playerName).load());
        }

        return guildPlayerMap.get(playerName);
    }

    public GuildPlayer getGuildPlayer(Player player) {
        return getGuildPlayer(player.getName());
    }

    public List<GuildPlayer> getOnlineGuildPlayers() {
        return guildPlayerMap.values().stream().filter(GuildPlayer::isOnline).collect(Collectors.toList());
    }
}
