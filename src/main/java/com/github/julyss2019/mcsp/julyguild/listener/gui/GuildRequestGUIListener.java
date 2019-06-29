package com.github.julyss2019.mcsp.julyguild.listener.gui;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.GuildPlayerRequestGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.request.JoinRequest;
import com.github.julyss2019.mcsp.julyguild.guild.request.Request;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuildRequestGUIListener implements Listener {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuildPlayer guildPlayer = guildPlayerManager.getGuildPlayer(player);
        GUI usingGUI = guildPlayer.getUsingGUI();
        int slot = event.getSlot();
        InventoryAction inventoryAction = event.getAction();

        if (usingGUI instanceof GuildPlayerRequestGUI && (slot >= 0 && slot < 51)) {
            GuildPlayerRequestGUI gui = (GuildPlayerRequestGUI) usingGUI;
            Guild guild = guildPlayer.getOfflineGuildPlayer().getGuild();
            List<Request> requests = gui.getRequests();

            if (slot < requests.size()) {
                JoinRequest joinRequest = (JoinRequest) requests.get(slot);
                OfflineGuildPlayer requestOfflineGuildPlayer = joinRequest.getOfflineGuildPlayer();

                String guildName = guild.getName();
                String requesterName = requestOfflineGuildPlayer.getName();

                if (requestOfflineGuildPlayer.isInGuild()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            guild.removeRequest(joinRequest);
                        }
                    }.runTaskLater(plugin, 1L);

                    JulyMessage.sendColoredMessages(player, "&c请求已失效: &e" + requesterName + "&c.");
                }

                // 左键
                if (inventoryAction == InventoryAction.PICKUP_ALL) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            guild.removeRequest(joinRequest);
                            guild.addMember(joinRequest.getOfflineGuildPlayer());
                        }
                    }.runTaskLater(plugin, 1L);

                    JulyMessage.sendColoredMessages(player, "&b审批通过: &e" + requestOfflineGuildPlayer.getName() + "&a.");

                    if (requestOfflineGuildPlayer.isOnline()) {
                        JulyMessage.sendColoredMessage(requestOfflineGuildPlayer.getBukkitPlayer(), "&d恭喜你通过审核, 正式成为 &e" + guildName + " &d宗门会员!");
                    }
                } else if (inventoryAction == InventoryAction.PICKUP_HALF) { // 右键
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            guild.removeRequest(joinRequest);
                        }
                    }.runTaskLater(plugin, 1L);

                    JulyMessage.sendColoredMessages(player, "&c审批拒绝: &e" + requestOfflineGuildPlayer.getName() + "&c.");

                    if (requestOfflineGuildPlayer.isOnline()) {
                        JulyMessage.sendColoredMessages(requestOfflineGuildPlayer.getBukkitPlayer(), "&c你在 &e" + guildName  + " &c宗门的申请请求被拒绝.");
                    }
                }
            }
        }
    }
}
