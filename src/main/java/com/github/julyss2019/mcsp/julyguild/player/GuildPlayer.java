package com.github.julyss2019.mcsp.julyguild.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GuildPlayer {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private String name;
    private GUI usingGUI;

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
