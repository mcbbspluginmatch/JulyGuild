package com.github.julyss2019.mcsp.julyguild.gui;

import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import org.bukkit.inventory.Inventory;

public class BaseGUI implements GUI {
    protected GuildPlayer guildPlayer;

    public BaseGUI(GuildPlayer guildPlayer) {
        this.guildPlayer = guildPlayer;
    }

    @Override
    public GuildPlayer getGuildPlayer() {
        return guildPlayer;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
