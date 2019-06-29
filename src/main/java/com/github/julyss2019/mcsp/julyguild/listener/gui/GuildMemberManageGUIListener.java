package com.github.julyss2019.mcsp.julyguild.listener.gui;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.GuildMemberManageGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class GuildMemberManageGUIListener implements Listener {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player bukkitPlayer = (Player) event.getWhoClicked();
        GuildPlayer guildPlayer = guildPlayerManager.getGuildPlayer(bukkitPlayer);
        GUI usingGUI = guildPlayer.getUsingGUI();
        int slot = event.getSlot();

        if ((slot >= 0 && slot < 51) && (usingGUI instanceof GuildMemberManageGUI)) {
            Guild guild = guildPlayer.getOfflineGuildPlayer().getGuild();
            List<GuildMember> members = ((GuildMemberManageGUI) usingGUI).getGuildMembers();

            if (slot < members.size()) {
                GuildMember guildMember = members.get(slot);
                InventoryAction action = event.getAction();

                if (action == InventoryAction.PICKUP_ALL) {
                    usingGUI.close();
                    JulyMessage.sendColoredMessages(bukkitPlayer, "&e管理员拥有的权限: 审批玩家.");
                    JulyMessage.sendColoredMessages(bukkitPlayer, "&c如果要将会员 &e" + guildMember.getName() + " &c设置为宗门管理员, 请在 &e10秒内 &c在聊天栏输入并发送: &econfirm");
                    JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                        @Override
                        public void onChat(AsyncPlayerChatEvent event) {
                            event.setCancelled(true);
                            JulyChatFilter.unregisterChatFilter(bukkitPlayer);

                            if (event.getMessage().equalsIgnoreCase("confirm")) {
                                guild.setMemberPermission(guildMember, Permission.ADMIN);
                                JulyMessage.sendColoredMessages(bukkitPlayer, "&c已设置成员 &e" +guildMember.getName() + " &c为宗门管理员.");
                            }
                        }

                        @Override
                        public void onTimeout() {
                            JulyMessage.sendColoredMessages(bukkitPlayer, "&c确认已超时.");
                        }
                    }, 10);
                } else if (action == InventoryAction.PICKUP_HALF) {
                    usingGUI.close();
                    JulyMessage.sendColoredMessages(bukkitPlayer, "&c如果要从宗门移出会员 &e" + guildMember.getName() + " &c, 请在 &e10秒内 &c在聊天栏输入并发送: &econfirm");
                    JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                        @Override
                        public void onChat(AsyncPlayerChatEvent event) {
                            event.setCancelled(true);
                            JulyChatFilter.unregisterChatFilter(bukkitPlayer);

                            if (event.getMessage().equalsIgnoreCase("confirm")) {
                                guild.removeMember(guildMember);
                                JulyMessage.sendColoredMessages(bukkitPlayer, "&c已移出会员 &e" +guildMember.getName() + "&c.");
                            }
                        }

                        @Override
                        public void onTimeout() {
                            JulyMessage.sendColoredMessages(bukkitPlayer, "&c确认已超时.");
                        }
                    }, 10);
                }
            }
        }
    }
}
