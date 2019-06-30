package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.gui.player.pageable.GuildIconRepositoryGUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.pageable.GuildMemberManageGUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.pageable.GuildPlayerRequestGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.request.TpRequest;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.message.TitleBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuildManageGUI extends BaseGUI {
    private Inventory inventory;
    private Guild guild;
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildSettings guildSettings = plugin.getGuildSettings();
    private static Map<String, Long> tpAllIntervalMap = new HashMap<>();

    public GuildManageGUI(GuildPlayer guildPlayer) {
        super(GUIType.MANAGE, guildPlayer);

        this.guild = offlineGuildPlayer.getGuild();
        build();
    }

    @Override
    public void build() {
        InventoryBuilder inventoryBuilder = new InventoryBuilder().row(4).title("&e&l宗门管理").colored()
                .item(35, CommonItem.BACK, new ItemListener() {
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

                    .item(1, 4, new ItemBuilder()
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

                    .item(1, 5, new ItemBuilder()
                            .material(Material.BLAZE_POWDER)
                            .displayName("&a宗门升级")
                            .addLore("")
                            .addLore("&7- &f升级宗门最大人数")
                            .addLore("")
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .colored()
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            new GuildPromoteGUI(guildPlayer).open();
                        }
                    })

                    .item(1, 6, new ItemBuilder()
                            .material(Material.LAVA_BUCKET)
                            .displayName("&a宗门商店")
                            .addLore("")
                            .addLore("&7- &f图标商店")
                            .addLore("")
                            .colored()
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            new GuildShopGUI(guildPlayer).open();
                        }
                    })

                    .item(2, 2, new ItemBuilder()
                            .material(Material.END_CRYSTAL)
                            .displayName("&a图标仓库")
                            .colored()
                            .addLore("&7- &f设置你购买的的宗门图标")
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            new GuildIconRepositoryGUI(guildPlayer).open();
                        }
                    })

                    .item(2, 3, new ItemBuilder()
                            .material(Material.NAME_TAG)
                            .displayName("&a转让宗门")
                            .colored()
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&c如果要转让宗门, 请在 &e60秒内 &c输入欲转让给的成员ID: ");
                            JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                                @Override
                                public void onChat(AsyncPlayerChatEvent event) {
                                    event.setCancelled(true);
                                    JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                    String name = event.getMessage();

                                    if (name.equalsIgnoreCase(playerName)) {
                                        JulyMessage.sendColoredMessage(bukkitPlayer, "&c你不能转让给你自己.");
                                        return;
                                    }

                                    if (!guild.isMember(name)) {
                                        JulyMessage.sendColoredMessage(bukkitPlayer, "&c玩家 &e" + name + " &c不是你宗门的成员.");
                                        return;
                                    }

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            guild.setOwner(guild.getMember(name));
                                        }
                                    }.runTaskLater(plugin, 1L);

                                    JulyMessage.sendColoredMessage(bukkitPlayer, "&d成功将宗门转让给 &e" + name + "&d.");
                                }
                            }, 60);
                        }
                    })

                    .item(2, 4, new ItemBuilder()
                            .material(Material.EYE_OF_ENDER)
                            .displayName("&a全员集结令")
                            .addLore("")
                            .addLore("&7- &f请求全体成员传送到你所在的位置")
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .colored()
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();

                            long interval = (System.currentTimeMillis() - tpAllIntervalMap.getOrDefault(bukkitPlayer.getName(), 0L)) / 1000L;

                            if (interval < guildSettings.getTpAllInterval()) {
                                JulyMessage.sendColoredMessage(bukkitPlayer, "&c冷却中: &e" + Util.getTimeLeftStr(guildSettings.getTpAllInterval() - interval) + "&c.");
                                return;
                            }

                            tpAllIntervalMap.put(bukkitPlayer.getName(), System.currentTimeMillis());

                            int validCounter = 0;

                            for (GuildMember member : guild.getMembers()) {
                                if (member.isOnline() && !member.getName().equals(guildPlayer.getName())) {
                                    validCounter++;
                                    member.getOfflineGuildPlayer().getGuildPlayer().addRequest(TpRequest.createNew(offlineGuildPlayer, bukkitPlayer.getLocation()));
                                    plugin.getTpAllListener().resetPlayer(member.getName()); // 重置
                                    JulyMessage.sendTitle(member.getBukkitPlayer(), new TitleBuilder().text("&b全员集结令").colored().build());
                                    JulyMessage.sendColoredMessage(member.getBukkitPlayer(), "&e宗主 &c" + bukkitPlayer.getName() + " &e请求你传送到TA那, 如果要传送请在 &c" + guildSettings.getTpAllShiftTimeout() + "秒内 &e快速按 &c" + guildSettings.getTpAllShiftCount() + "次 &eShift键!");
                                }
                            }

                            JulyMessage.sendColoredMessage(bukkitPlayer, "&e成功向 &c" + validCounter + "个 &e成员发送了全员集结令, 请等待确认!");
                        }
                    })

                    .item(2, 5, new ItemBuilder()
                            .material(Material.POTION)
                            .displayName("&aBUFF仓库")
                            .addLore("")
                            .addLore("&c未开放")
                            .addLore("")
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
                            .colored()
                            .build())

                    .item(2, 6, new ItemBuilder()
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
