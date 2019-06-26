package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.Settings;
import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MainGUI extends BasePageableGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static Settings settings = plugin.getSettings();
    private static GuildManager guildManager = plugin.getGuildManager();
    private Inventory inventory;

    public MainGUI(GuildPlayer guildPlayer) {
        super(guildPlayer);

        setCurrentPage(0);
    }

    @Override
    public void setCurrentPage(int page) {
        super.setCurrentPage(page);

        InventoryBuilder inventoryBuilder = new InventoryBuilder()
                .row(6)
                .colored()
                .title("&a&l宗门(第" + (getCurrentPage() + 1) + "页)");

        // 如果大于1页则提供翻页按钮
        if (getTotalPage() > 1) {
            inventoryBuilder
                    .item(4, 7, CommonItem.getPreviousPageItem(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (hasPrecious()) {
                                close();
                                previousPage();
                                open();
                            }
                        }
                    })
                    .item(4, 8, CommonItem.getNextPageItem(), new ItemListener() {
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

        if (guildPlayer.isInGuild()) {
            inventoryBuilder.item(5, 4, new SkullItemBuilder().owner(guildPlayer.getName()).displayName("&f我的宗门").colored().build(), new ItemListener() {
                @Override
                public void onClicked(InventoryClickEvent event) {
                    close();
                    new GuildMineGUI(guildPlayer).open();
                }
            });
        } else {
            inventoryBuilder.item(5, 2, new ItemBuilder().material(Material.EMERALD).displayName("&f创建宗门").colored().build()
                    , new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&e请在聊天栏输入并发送宗门名: ");
                            close();
                            JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                                @Override
                                public void onChat(AsyncPlayerChatEvent event) {
                                    event.setCancelled(true);

                                    String guildName = event.getMessage();

                                    if (!guildName.matches(settings.getGuildCreateNameRegex())) {
                                        JulyMessage.sendColoredMessage(bukkitPlayer, settings.getGuildCreateNameNotValidMsg());
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
            inventoryBuilder.item(5, 6, new ItemBuilder().colored().material(Material.GOLDEN_APPLE).displayName("&f加入宗门").addLore("&7- &f单击上方的公会图标来加入工会").build());
        }

        List<Guild> guilds = guildManager.getGuilds(true);
        int guildSize = guilds.size();
        int itemCounter = page * 43;
        int loopCount = guildSize - itemCounter < 43 ? guildSize - itemCounter : 43;

        for (int i = 0; i < loopCount; i++) {
            Guild guild = guilds.get(itemCounter++);

            inventoryBuilder.item(i, new ItemBuilder()
                    .lores(guild.replaceVariables(settings.getGuiMainGuiGuildLores()))
                    .material(guild.getIcon())
                    .displayName(guild.getName())
                    .colored()
                    .build());
        }

        this.inventory = inventoryBuilder.build();
    }

    @Override
    public int getTotalPage() {
        int guildSize = guildManager.getGuilds().size();

        return guildSize % 43 == 0 ? guildSize / 43 : guildSize / 43 + 1;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
