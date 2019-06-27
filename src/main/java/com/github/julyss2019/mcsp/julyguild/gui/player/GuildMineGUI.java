package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.Settings;
import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildOwner;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class GuildMineGUI extends BaseGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static Settings settings = plugin.getSettings();
    private Inventory inventory;
    private Guild guild;

    public GuildMineGUI(GuildPlayer guildPlayer) {
        super(guildPlayer);

        this.guild = guildPlayer.getGuild();
        build();
    }

    @Override
    public void build() {
        InventoryBuilder inventoryBuilder = new InventoryBuilder().title("&e&l我的宗门").colored().row(6);

        List<String> memberLores = new ArrayList<>();

        for (GuildMember guildMember : guild.getMembers(true, true)) {
            if (guildMember instanceof GuildOwner) {
                memberLores.add("&7- &c" + guildMember.getName());
            } else {
                memberLores.add("&7- &f" + guildMember.getName());
            }

        }

        inventoryBuilder
                .item(1, 4, new ItemBuilder().
                        material(Material.SIGN)
                        .displayName("&f个人信息").colored()
                        .lores(PlaceholderAPI.setPlaceholders(bukkitPlayer, settings.getGuiMainGuiGuildPlayerInfoLores()))
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .build())
                .item(2, 3, new ItemBuilder()
                        .material(Material.PAINTING)
                        .displayName("&f宗门公告")
                        .lores(guild.getAnnouncements())
                        .colored()
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .build())
                .item(2, 4, new ItemBuilder()
                        .material(Material.TOTEM)
                        .displayName("&f宗门成员")
                        .lores(memberLores)
                        .colored()
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .build())
                .item(2, 5, new ItemBuilder()
                        .material(Material.GOLD_NUGGET)
                        .displayName("&e贡献金币")
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .colored()
                        .build())
                .item(3, 4, new ItemBuilder()
                        .material(Material.YELLOW_SHULKER_BOX)
                        .displayName("&f宗门仓库")
                        .colored()
                        .enchant(Enchantment.DURABILITY, 1)
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .build())
                .item(53, CommonItem.BACK_TO_MAIN, new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        new MainGUI(guildPlayer).open();
                    }
                });

        if (guild.isOwner(guildPlayer)) {
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
        } else {
            inventoryBuilder
                    .item(53, new ItemBuilder().material(Material.DARK_OAK_DOOR_ITEM).displayName("&c退出宗门").colored().build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            close();
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&c如果要退出宗门, 请在聊天栏输入并发送: &econfirm");
                            JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                                @Override
                                public void onChat(AsyncPlayerChatEvent event) {
                                    event.setCancelled(true);

                                    if (event.getMessage().equals("confirm")) {
                                        guild.removeMember(guild.getMember(guildPlayer));
                                        JulyMessage.sendColoredMessage(bukkitPlayer, "&d退出宗门成功.");
                                    } else {
                                        JulyMessage.sendColoredMessage(bukkitPlayer, "&e退出宗门失败.");
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
