package com.github.julyss2019.mcsp.julyguild.listener;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.GuildInfoGUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.MainGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainGUIListener implements Listener {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildManager guildManager = plugin.getGuildManager();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        GuildPlayer guildPlayer = guildPlayerManager.getGuildPlayer((Player) event.getWhoClicked());
        GUI gui = guildPlayer.getUsingGUI();
        int slot = event.getSlot();

        if (gui instanceof MainGUI && (slot >= 0 && slot <= 42)) {
            MainGUI mainGUI = (MainGUI) gui;
            Guild guild = guildManager.getGuilds(true).get(slot);
            GUI newGUI = new GuildInfoGUI(guildPlayer, guild, mainGUI.getCurrentPage());

            gui.close();
            guildPlayer.setUsingGUI(newGUI);
            newGUI.open();
        }
    }
}
