package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.config.IconShopSettings;
import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.gui.player.pageable.GuildMemberGUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.pageable.MainGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildBank;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildAdmin;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuildMineGUI extends BaseGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildSettings guildSettings = plugin.getGuildSettings();
    private static Economy vault = plugin.getVaultAPI();
    private static PlayerPointsAPI playerPointsAPI = plugin.getPlayerPointsAPI();

    private Inventory inventory;
    private Guild guild;
    private GuildBank guildBank;

    public GuildMineGUI(GuildPlayer guildPlayer) {
        super(GUIType.MINE, guildPlayer);

        this.guild = this.guildPlayer.getGuild();
        this.guildBank = guild.getGuildBank();
        build();
    }

    @Override
    public void build() {
        GuildMember member = guild.getMember(playerName);
        InventoryBuilder inventoryBuilder = new InventoryBuilder().title("&e&l我的宗门").colored().row(6);

        List<String> memberLores = new ArrayList<>();
        List<GuildMember> guildMembers = guild.getMembers();

        Guild.sortMembers(guildMembers);

        for (GuildMember guildMember : guild.getMembers()) {
            Permission permission = guildMember.getPermission();

            if (memberLores.size() < 10) {
                memberLores.add(permission.getColor() + "[" + permission.getChineseName() + "] " + guildMember.getName());
            } else {
                break;
            }
        }

        inventoryBuilder
                // 宗门信息
                .item(2, 5, new ItemBuilder().
                        material(Material.SIGN)
                        .displayName(PlaceholderAPI.setPlaceholders(bukkitPlayer, guildSettings.getGlobalGuildInfoDisplayName()))
                        .lores(PlaceholderAPI.setPlaceholders(bukkitPlayer, guildSettings.getGlobalGuildInfoLores()))
                        .colored()
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .build())
                // 个人信息
                .item(2, 3, new ItemBuilder().
                        material(Material.SIGN)
                        .displayName(PlaceholderAPI.setPlaceholders(bukkitPlayer, guildSettings.getMineGUIPlayerInfoDisplayName()))
                        .lores(PlaceholderAPI.setPlaceholders(bukkitPlayer, guildSettings.getMineGUIPlayerInfoLores()))
                        .colored()
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .build())
                .item(2, 4, new ItemBuilder()
                        .material(Material.PAINTING)
                        .displayName("&f宗门公告")
                        .lores(guild.getAnnouncements())
                        .colored()
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .build())
                .item(3, 4, new SkullItemBuilder()
                        .owner("Notch")
                        .displayName("&f宗门成员")
                        .addLore("&b>> &a点击查看详细信息")
                        .addLore("")
                        .addLores(memberLores)
                        .addLore(guild.getMemberCount() > 10 ? "&7和 &e" + (guild.getMemberCount() - 10) + "个 &7成员..." : null)
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .colored().build(), new ItemListener() {
                            @Override
                            public void onClicked(InventoryClickEvent event) {
                                close();
                                new GuildMemberGUI(guildPlayer, guild, GuildMineGUI.this).open();
                            }
                        }
                )
                .item(3, 3, new ItemBuilder()
                                .material(Material.GOLD_NUGGET)
                                .displayName("&e贡献金币")
                                .enchant(Enchantment.DURABILITY, 1)
                                .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                                .colored()
                                .build(), new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        Util.sendColoredMessage(bukkitPlayer, "&d金币将存入宗门银行.");
                        Util.sendColoredMessage(bukkitPlayer, "&e请在聊天栏输入并发送要赞助的金币数量: ");
                        JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                            @Override
                            public void onChat(AsyncPlayerChatEvent event) {
                                JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                event.setCancelled(true);

                                int amount;

                                try {
                                    amount = Integer.parseInt(event.getMessage());
                                } catch (Exception e) {
                                    Util.sendColoredMessage(bukkitPlayer, "&c数量不正确!");
                                    return;
                                }

                                if (amount < guildSettings.getDonateMinMoney()) {
                                    Util.sendColoredMessage(bukkitPlayer, "&c最小金币赞助额为 &e" + guildSettings.getDonateMinMoney() + "&c.");
                                    return;
                                }

                                if (vault.getBalance(bukkitPlayer) < amount) {
                                    Util.sendColoredMessage(bukkitPlayer, "&c金币不足.");
                                    return;
                                }

                                vault.withdrawPlayer(bukkitPlayer, amount);
                                member.addDonatedMoney(amount);
                                guildBank.deposit(GuildBank.BalanceType.MONEY, amount);
                                guild.broadcastMessage("&d" + member.getPermission().getChineseName() + " &e" + guildPlayer.getName() + " &d为宗门赞助了 &e" + amount + "个 &d金币&d!");
                            }
                        });
                    }
                })
                .item(3, 5, new ItemBuilder()
                        .material(Material.DIAMOND)
                        .displayName("&b贡献点券")
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .colored()
                        .build(), new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        Util.sendColoredMessage(bukkitPlayer, "&d点券将存入宗门银行.");
                        Util.sendColoredMessage(bukkitPlayer, "&e请在聊天栏输入并发送要赞助的点券数量: ");
                        JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                            @Override
                            public void onChat(AsyncPlayerChatEvent event) {
                                JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                event.setCancelled(true);

                                int amount;

                                try {
                                    amount = Integer.parseInt(event.getMessage());
                                } catch (Exception e) {
                                    Util.sendColoredMessage(bukkitPlayer, "&c数量不正确!");
                                    return;
                                }

                                if (amount < guildSettings.getDonateMinPoints()) {
                                    Util.sendColoredMessage(bukkitPlayer, "&c最小点券赞助额为 &e" + guildSettings.getDonateMinPoints() + "&c.");
                                    return;
                                }

                                UUID uuid = bukkitPlayer.getUniqueId();

                                if (playerPointsAPI.look(uuid) < amount) {
                                    Util.sendColoredMessage(bukkitPlayer, "&c点券不足.");
                                    return;
                                }

                                playerPointsAPI.take(uuid, amount);
                                member.addDonatedPoints(amount);
                                guildBank.deposit(GuildBank.BalanceType.POINTS, amount);
                                guild.broadcastMessage("&d" + member.getPermission().getChineseName() + " &e" + guildPlayer.getName() + " &d为宗门赞助了 &e" + amount + "个 &d点券&d!");
                            }
                        });
                    }
                })
                .item(53, CommonItem.BACK_TO_MAIN, new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        new MainGUI(guildPlayer).open();
                    }
                });


        if (member instanceof GuildAdmin) {
            inventoryBuilder
                    .item(45, new ItemBuilder()
                            .material(Material.ENDER_PORTAL_FRAME)
                            .displayName("&f管理宗门")
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .colored().build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            new GuildManageGUI(guildPlayer).open();
                        }
                    });
        }

        Permission permission = member.getPermission();

        if (permission == Permission.MEMBER || permission == Permission.ADMIN) {
            inventoryBuilder
                    .item(5, permission == Permission.MEMBER ? 0 : 1, new ItemBuilder()
                            .material(Material.IRON_DOOR)
                            .displayName("&c退出宗门")
                            .colored()
                            .build()
                    , new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            Util.sendColoredMessage(bukkitPlayer, "&c如果要退出宗门, 请在聊天栏输入并发送: &econfirm");
                            JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                                @Override
                                public void onChat(AsyncPlayerChatEvent event) {
                                    event.setCancelled(true);

                                    if (event.getMessage().equals("confirm")) {
                                        guild.removeMember(guild.getMember(playerName));
                                        Util.sendColoredMessage(bukkitPlayer, "&d退出宗门成功.");
                                    } else {
                                        Util.sendColoredMessage(bukkitPlayer, "&e退出宗门失败.");
                                    }

                                    JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                }
                            });

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
