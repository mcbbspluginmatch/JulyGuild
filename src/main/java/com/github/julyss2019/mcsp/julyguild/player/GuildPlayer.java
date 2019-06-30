package com.github.julyss2019.mcsp.julyguild.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.player.request.GuildPlayerRequest;
import com.github.julyss2019.mcsp.julyguild.player.request.GuildPlayerRequestType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class GuildPlayer {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private String name;
    private GUI usingGUI;
    private Map<String, GuildPlayerRequest> requestMap = new HashMap<>();

    public GuildPlayer(Player player) {
        this.name = player.getName();
    }

    public GuildPlayer load() {
        return this;
    }

    public GUI getUsingGUI() {
        return usingGUI;
    }

    public void setUsingGUI(GUI usingGUI) {
        this.usingGUI = usingGUI;
    }

    public OfflineGuildPlayer getOfflineGuildPlayer() {
        return guildPlayerManager.getOfflineGuildPlayer(name);
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(name);
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        Player tmp = getBukkitPlayer();

        return tmp != null && tmp.isOnline();
    }

    /**
     * 添加请求，不存储到文件系统
     */
    public void addRequest(GuildPlayerRequest request) {
        requestMap.put(request.getUUID().toString(), request);
    }

    public Collection<GuildPlayerRequest> getRequests() {
        return requestMap.values();
    }

    public void removeRequest(String uuid) {
        requestMap.remove(uuid);
    }

    public boolean hasRequest(String uuid) {
        return requestMap.containsKey(uuid);
    }

    public boolean hasRequest(GuildPlayerRequestType type) {
        for (GuildPlayerRequest request : getRequests()) {
            if (request.getType() == type) {
                return true;
            }
        }

        return false;
    }

    public GuildPlayerRequest getOnlyOneRequest(GuildPlayerRequestType type) {
        for (GuildPlayerRequest request : getRequests()) {
            if (request.getType() == type) {
                return request;
            }
        }

        return null;
    }

    public void updateGUI(GUIType... guiTypes) {
        GUI usingGUI = this.usingGUI;

        if (usingGUI != null) {
            for (GUIType guiType : guiTypes) {
                if (usingGUI.getType() == guiType) {
                    usingGUI.close();
                    usingGUI.build();
                    usingGUI.open();
                }
            }
        }
    }
}
