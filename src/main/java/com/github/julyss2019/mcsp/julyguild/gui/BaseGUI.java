package com.github.julyss2019.mcsp.julyguild.gui;

import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BaseGUI implements GUI {
    protected GuildPlayer guildPlayer;
    protected Player bukkitPlayer;

    public BaseGUI(GuildPlayer guildPlayer) {
        this.guildPlayer = guildPlayer;
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
}
