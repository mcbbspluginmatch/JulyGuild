package com.github.julyss2019.mcsp.julyguild.gui;

import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BaseGUI implements GUI {
    private GUIType type;
    protected GuildPlayer guildPlayer;
    protected OfflineGuildPlayer offlineGuildPlayer;
    protected Player bukkitPlayer;
    protected String playerName;

    public BaseGUI(GUIType guiType, GuildPlayer guildPlayer) {
        this.type = guiType;
        this.playerName = guildPlayer.getName();
        this.guildPlayer = guildPlayer;
        this.offlineGuildPlayer = guildPlayer.getOfflineGuildPlayer();
        this.bukkitPlayer = guildPlayer.getBukkitPlayer();
    }

    @Override
    public GuildPlayer getGuildPlayer() {
        return guildPlayer;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public void build() {}

    @Override
    public GUIType getType() {
        return type;
    }
}
