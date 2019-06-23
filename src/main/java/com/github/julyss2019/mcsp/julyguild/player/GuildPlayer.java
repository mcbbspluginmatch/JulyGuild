package com.github.julyss2019.mcsp.julyguild.player;

import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import org.bukkit.entity.Player;

public class GuildPlayer extends OfflineGuildPlayer {
    private GUI usingGUI;

    public GuildPlayer(Player player) {
        super(player.getName());
    }

    public GuildPlayer load() {
        super.load();

        return this;
    }

    public GUI getUsingGUI() {
        return usingGUI;
    }

    public void setUsingGUI(GUI usingGUI) {
        this.usingGUI = usingGUI;
    }
}
