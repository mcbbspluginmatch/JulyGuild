package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;

public class GuildManageGUI extends BaseGUI {
    private Inventory inventory;
    private Guild guild;
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildSettings guildSettings = plugin.getGuildSettings();

    public GuildManageGUI(GuildPlayer guildPlayer) {
        super(GUIType.MANAGE, guildPlayer);

        this.guild = offlineGuildPlayer.getGuild();
        build();
    }

    @Override
    public void build() {
        InventoryBuilder inventoryBuilder = new InventoryBuilder().row(3).title("&e&l宗门管理").colored()
                .item(26, CommonItem.BACK, new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();

                        if (guild.isValid()) {
                            new GuildMineGUI(guildPlayer).open();
                        }
                    }
                });

        if (guildPlayer.getOfflineGuildPlayer().getGuild().getMember(guildPlayer.getName()).getPermission() == Permission.OWNER) {
            inventoryBuilder
                    .item(1, 2, new ItemBuilder()
                            .material(Material.PAINTING)
                            .displayName("&a设置公告")
                            .addLore("")
                            .addLore("&7- &f点击设置公告")
                            .addLore("&7- &F玩家可以在宗门介绍页和我的宗门中看见公告")
                            .addLore("")
                            .addLores(guild.getAnnouncements())
                            .colored()
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&e请在聊天栏输入并发送要设置的公告, 使用符号 &c" + guildSettings.getGuildAnnouncementSplitChar() + " &e来换行, 最多支持 &c" + guildSettings.getGuildAnnouncementMaxCount() + "行&e: ");

                            JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                                @Override
                                public void onChat(AsyncPlayerChatEvent event) {
                                    event.setCancelled(true);

                                    String[] messages = event.getMessage().split(guildSettings.getGuildAnnouncementSplitChar());

                                    if (messages.length > guildSettings.getGuildAnnouncementMaxCount()) {
                                        JulyMessage.sendColoredMessage(bukkitPlayer, "&c公告最多能设置 &e" + guildSettings.getGuildAnnouncementMaxCount() + "条&c, 使用分隔符 &e" + guildSettings.getGuildAnnouncementSplitChar() + " &c换行.");
                                        return;
                                    }

                                    guild.setAnnouncements(Arrays.asList(messages));
                                    JulyMessage.sendColoredMessage(bukkitPlayer, "&d设置公告成功!");
                                    JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                }
                            });
                        }
                    })

                    .item(1, 1, new ItemBuilder()
                                    .material(Material.GOLD_BARDING)
                                    .displayName("&a审批玩家")
                                    .addLore("")
                                    .addLore("&7- &f处理玩家加入宗门的请求")
                                    .addLore("")
                                    .enchant(Enchantment.DURABILITY, 1)
                                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                                    .colored()
                                    .build()
                            , new ItemListener() {
                                @Override
                                public void onClicked(InventoryClickEvent event) {
                                    close();
                                    new GuildPlayerRequestGUI(guildPlayer).open();
                                }
                            })

                    .item(1, 3, new ItemBuilder()
                                    .material(Material.TOTEM)
                                    .displayName("&a成员管理").colored()
                                    .addLore("")
                                    .addLore("&7- &f任职或移出成员")
                                    .addLore("")
                                    .enchant(Enchantment.DURABILITY, 1)
                                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                                    .build()
                            , new ItemListener() {
                                @Override
                                public void onClicked(InventoryClickEvent event) {
                                    close();
                                    new GuildMemberManageGUI(guildPlayer).open();
                                }
                            })

                    .item(1, 4, new ItemBuilder()
                            .material(Material.BLAZE_POWDER)
                            .displayName("&f宗门升级")
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .colored()
                            .build())

                    .item(1, 5, new ItemBuilder()
                            .material(Material.DOUBLE_PLANT)
                            .displayName("&a设置宗门图标")
                            .addLore("")
                            .addLore("&7- &f更改排行榜显示的图标")
                            .addLore("")
                            .colored()
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .build())

                    .item(1, 6, new ItemBuilder()
                            .material(Material.NAME_TAG)
                            .displayName("&a转让宗门")
                            .colored()
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .build())

                    .item(1, 7, new ItemBuilder()
                            .material(Material.BARRIER)
                            .displayName("&c解散宗门")
                            .colored()
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&c（该操作不可恢复，请谨慎操作）如果要解散宗门, 请在 &e10秒内 &c聊天栏输入并发送: &econfirm");
                            JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                                @Override
                                public void onTimeout() {
                                    JulyMessage.sendColoredMessages(bukkitPlayer, "&c确认请求已超时.");
                                }

                                @Override
                                public void onChat(AsyncPlayerChatEvent event) {
                                    event.setCancelled(true);

                                    if (event.getMessage().equals("confirm")) {
                                        guild.delete();
                                        JulyMessage.sendColoredMessage(bukkitPlayer, "&d解散宗门成功.");
                                    } else {
                                        JulyMessage.sendColoredMessage(bukkitPlayer, "&e解散宗门失败.");
                                    }

                                    JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                }
                            }, 10);
                        }
                    });
        } else {
            inventoryBuilder
                    .item(1, 4, new ItemBuilder()
                                    .material(Material.GOLD_BARDING)
                                    .displayName("&a审批玩家")
                                    .addLore("")
                                    .addLore("&7- &f处理玩家加入宗门的请求")
                                    .addLore("")
                                    .enchant(Enchantment.DURABILITY, 1)
                                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                                    .colored()
                                    .build()
                            , new ItemListener() {
                                @Override
                                public void onClicked(InventoryClickEvent event) {
                                    close();
                                    new GuildPlayerRequestGUI(guildPlayer).open();
                                }
                            });

        }



        this.inventory = inventoryBuilder.build();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
