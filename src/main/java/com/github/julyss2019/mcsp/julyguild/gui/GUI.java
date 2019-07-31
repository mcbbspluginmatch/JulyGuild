package com.github.julyss2019.mcsp.julyguild.gui;

import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import org.bukkit.inventory.Inventory;

public interface GUI {
    GuildPlayer getGuildPlayer();

    Inventory getInventory();

    void build();

    default void close() {
        getGuildPlayer().getBukkitPlayer().closeInventory();
        getGuildPlayer().setUsingGUI(null);
    }

    default void open() {
        if (!getInventory().equals(getGuildPlayer().getBukkitPlayer().getOpenInventory().getTopInventory())) {
            getGuildPlayer().getBukkitPlayer().openInventory(getInventory());
            getGuildPlayer().setUsingGUI(this);
        }
    }

    GUIType getType();

    default void update() {
        close();
        build();
        open();
    }
}
