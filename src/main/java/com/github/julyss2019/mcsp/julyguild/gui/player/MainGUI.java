package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.Settings;
import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class MainGUI extends BasePageableGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private Settings settings = plugin.getSettings();
    private Inventory inventory;
    private Player bukkitPlayer;

    public MainGUI(GuildPlayer guildPlayer) {
        super(guildPlayer);

        this.bukkitPlayer = guildPlayer.getBukkitPlayer();
        setCurrentPage(0);
    }

    @Override
    public void setCurrentPage(int page) {
        super.setCurrentPage(page);

        this.inventory = new InventoryBuilder()
                .row(6)
                .colored()
                .title("&a&l宗门")
                .item(4, 7, CommonItem.getPreviousPageItem())
                .item(4, 8, CommonItem.getNextPageItem())
                .item(5, 0,
                        guildPlayer.isInGuild()
                                ? new ItemBuilder().material(Material.ENDER_PORTAL_FRAME).displayName("&f我的宗门").colored().build()
                                : new ItemBuilder().material(Material.EMERALD).displayName("&f创建宗门").colored().build()
                        , new ItemListener() {
                            @Override
                            public void onClicked(InventoryClickEvent event) {
                                if (guildPlayer.isInGuild()) {

                                } else {
                                    JulyMessage.sendColoredMessage(bukkitPlayer, "&e请在聊天栏输入并发送宗门名: ");
                                    close();
                                    JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                                        @Override
                                        public void onChat(AsyncPlayerChatEvent event) {
                                            String guildName = event.getMessage();

                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    JulyMessage.sendColoredMessage(bukkitPlayer, "&e要创建 &c" + guildName + " &e宗门, 请支付: ");

                                                    if (settings.isCreateGuildCostMoneyEnabled()) {
                                                        JulyMessage.sendBlankLine(bukkitPlayer);
                                                        JulyMessage.sendRawMessage(bukkitPlayer, "[\"\",{\"text\":\"   点我使用 §d金币x" + settings.getCreateGuildCostMoneyAmount() + " §b支付\",\"color\":\"aqua\",\"italic\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/guild create MONEY " + guildName + "\"}}]");
                                                        JulyMessage.sendBlankLine(bukkitPlayer);
                                                    }

                                                    if (settings.isCreateGuildCostPointsEnabled()) {
                                                        JulyMessage.sendBlankLine(bukkitPlayer);
                                                        JulyMessage.sendRawMessage(bukkitPlayer, "[\"\",{\"text\":\"   点我使用 §d点券x" + settings.getCreateGuildCostPointsAmount() + " §b支付\",\"color\":\"aqua\",\"italic\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/guild create POINTS " + guildName + "\"}}]");
                                                        JulyMessage.sendBlankLine(bukkitPlayer);
                                                    }

                                                    if (settings.isCreateGuildCostMoneyEnabled()) {
                                                        JulyMessage.sendBlankLine(bukkitPlayer);
                                                        JulyMessage.sendRawMessage(bukkitPlayer, "[\"\",{\"text\":\"   点我使用 §d建帮令x1 §b支付\",\"color\":\"aqua\",\"italic\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/guild create ITEM " + guildName + "\"}}]");
                                                        JulyMessage.sendBlankLine(bukkitPlayer);
                                                    }

                                                    JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                                }
                                            }.runTaskLater(plugin, 1L);

                                            event.setCancelled(true);
                                        }
                                    });
                                }
                            }
                        })
                .item(5, 4, new ItemBuilder().material(Material.SKULL_ITEM).displayName("&f个人信息").lores(settings.getPlayerInfoItemLores()).colored().build()).build();

    }

    @Override
    public int getTotalPage() {
        return super.getTotalPage();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
