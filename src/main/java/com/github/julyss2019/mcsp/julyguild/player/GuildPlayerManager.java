package com.github.julyss2019.mcsp.julyguild.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuildPlayerManager {
    private Map<String, OfflineGuildPlayer> offlineGuildPlayerMap = new HashMap<>();
    private Map<String, GuildPlayer> guildPlayerMap = new HashMap<>();

    public OfflineGuildPlayer getOfflineGuildPlayer(String name) {
        if (!offlineGuildPlayerMap.containsKey(name)) {
            offlineGuildPlayerMap.put(name, new OfflineGuildPlayer(name).load());
        }

        return offlineGuildPlayerMap.get(name);
    }

    public GuildPlayer getGuildPlayer(String playerName) {
        return getGuildPlayer(Bukkit.getPlayer(playerName));
    }

    public GuildPlayer getGuildPlayer(Player player) {
        String playerName = player.getName();

        if (!guildPlayerMap.containsKey(playerName)) {
            guildPlayerMap.put(playerName, new GuildPlayer(player).load());
        }

        return guildPlayerMap.get(playerName);
    }

    public List<GuildPlayer> getOnlineGuildPlayers() {
        return guildPlayerMap.values().stream().filter(OfflineGuildPlayer::isOnline).collect(Collectors.toList());
    }
}
