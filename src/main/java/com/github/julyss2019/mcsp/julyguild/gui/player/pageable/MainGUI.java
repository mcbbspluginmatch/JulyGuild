package com.github.julyss2019.mcsp.julyguild.gui.player.pageable;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.GUISettings;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.gui.player.GuildCreateGUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.GuildInfoGUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.GuildMineGUI;
import com.github.julyss2019.mcsp.julyguild.guild.CacheGuildManager;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import me.clip.placeholderapi.PlaceholderAPI;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MainGUI extends BasePageableGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildSettings guildSettings = plugin.getGuildSettings();
    private static GUISettings guiSettings = plugin.getGuiSettings();
    private static CacheGuildManager cacheGuildManager = plugin.getCacheGuildManager();
    private Inventory inventory;
    private List<Guild> guilds = new ArrayList<>();

    public MainGUI(GuildPlayer guildPlayer) {
        super(GUIType.MAIN, guildPlayer);

        setCurrentPage(0);
    }

    @Override
    public void setCurrentPage(int page) {
        super.setCurrentPage(page);

        InventoryBuilder inventoryBuilder = new InventoryBuilder()
                .row(6)
                .colored()
                .title("&a&l宗门(第" + (getCurrentPage() + 1) + "页)")
                .listener(new InventoryListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        int index = event.getSlot() + getCurrentPage() * 43;

                        if (index < guilds.size()) {
                            Guild guild = guilds.get(index);
                            GUI newGUI = new GuildInfoGUI(guildPlayer, guild, getCurrentPage());

                            close();
                            guildPlayer.setUsingGUI(newGUI);
                            newGUI.open();
                        }
                    }
                });

        // 如果大于1页则提供翻页按钮
        if (getTotalPage() > 1) {
            inventoryBuilder
                    .item(4, 7, CommonItem.PREVIOUS_PAGE, new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (hasPrecious()) {
                                close();
                                previousPage();
                                open();
                            }
                        }
                    })
                    .item(4, 8, CommonItem.NEXT_PAGE, new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (hasNext()) {
                                close();
                                nextPage();
                                open();
                            }
                        }
                    });
        }

        if (offlineGuildPlayer.isInGuild()) {
            inventoryBuilder.item(5, 4, new SkullItemBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjIxYWIzMWE0MjczOWEzNTI3ZDMwNWNjOTU2YWVlNGQ2YmEzNDU1NTQzODFhNmE0YzRmZjA2YTFjMTlmZGQ0In19fQ==")
                    .displayName("&f我的宗门")
                    .enchant(Enchantment.DURABILITY, 1)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .colored()
                    .build(), new ItemListener() {
                @Override
                public void onClicked(InventoryClickEvent event) {
                    close();
                    new GuildMineGUI(guildPlayer).open();
                }
            });
        } else {
            inventoryBuilder.item(5, 2, new ItemBuilder()
                            .material(Material.NETHER_STAR)
                            .displayName("&d创建宗门").colored()
                            .enchant(Enchantment.DURABILITY, 1)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .build()
                    , new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&e请在聊天栏输入并发送宗门名: ");
                            close();
                            JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                                @Override
                                public void onChat(AsyncPlayerChatEvent event) {
                                    event.setCancelled(true);
                                    JulyChatFilter.unregisterChatFilter(bukkitPlayer);

                                    String guildName = event.getMessage();

                                    if (!guildName.matches(guildSettings.getGuildCreateNameRegex())) {
                                        JulyMessage.sendColoredMessage(bukkitPlayer, guildSettings.getGuildCreateNameNotValidMsg());
                                        return;
                                    }

                                    if (guildName.contains("§") && !bukkitPlayer.hasPermission("JulyGuild.create.colored")) {
                                        JulyMessage.sendColoredMessage(bukkitPlayer, "&c你没有权限使用彩色的宗门名!");
                                        return;
                                    }

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            close();
                                            new GuildCreateGUI(guildPlayer, guildName).open();
                                            JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                        }
                                    }.runTaskLater(plugin, 1L);

                                    event.setCancelled(true);
                                }
                            });
                        }
                    });
            inventoryBuilder.item(5, 6, new ItemBuilder()
                    .colored()
                    .material(Material.EMERALD)
                    .enchant(Enchantment.DURABILITY, 1)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .displayName("&a加入宗门")
                    .addLore("&7- &f单击上方的图标以查看信息或加入宗门").build());
        }

        guilds.clear();
        guilds.addAll(cacheGuildManager.getSortedGuilds());
        int guildSize = guilds.size();
        int itemCounter = page * 43;
        int loopCount = guildSize - itemCounter < 43 ? guildSize - itemCounter : 43;

        for (int i = 0; i < loopCount; i++) {
            Guild guild = guilds.get(itemCounter++);

            inventoryBuilder.item(i, new ItemBuilder(guild.getCurrentIcon().getItemStack())
                    .lores(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(guild.getOwner().getName()), guiSettings.getMainGUIRankingListLores()))
                    .displayName(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(guild.getOwner().getName()), guiSettings.getMainGUIRankingListDisplayName()))
                    .colored()
                    .enchant(Enchantment.DURABILITY, 1)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .build());
        }

        this.inventory = inventoryBuilder.build();
    }

    @Override
    public int getTotalPage() {
        int guildSize = guilds.size();

        return guildSize == 0 ? 1 : guildSize % 43 == 0 ? guildSize / 43 : guildSize / 43 + 1;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public List<Guild> getGuilds() {
        return guilds;
    }
}
