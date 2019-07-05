package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildBank;
import com.github.julyss2019.mcsp.julyguild.guild.exception.GuildPromoteException;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import parsii.eval.Parser;
import parsii.tokenizer.ParseException;

public class GuildPromoteGUI extends BaseGUI {
    private Inventory inventory;
    private Guild guild;
    private GuildBank guildBank;
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildSettings guildSettings = plugin.getGuildSettings();
    private static Economy vault = plugin.getVaultAPI();
    private static PlayerPointsAPI playerPointsAPI = plugin.getPlayerPointsAPI();

    public GuildPromoteGUI(GuildPlayer guildPlayer) {
        super(GUIType.PROMOTE, guildPlayer);

        this.guild = this.guildPlayer.getGuild();
        this.guildBank = guild.getGuildBank();
        build();
    }

    @Override
    public void build() {
        InventoryBuilder inventoryBuilder = new InventoryBuilder()
                .row(3)
                .title("&e&l宗门升级")
                .colored()
                .item(26, CommonItem.BACK, new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        new GuildManageGUI(guildPlayer).open();
                    }
                })
                .listener(new InventoryListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {

                    }
                });


        int currentMaxMemberCount = guild.getMaxMemberCount();

        if (currentMaxMemberCount + 1 > guildSettings.getPromoteMoneyMaxMemberCount()) {
            inventoryBuilder.item(1, 3, new ItemBuilder().material(Material.BARRIER).displayName("&f使用金币升级").addLore("&7- &c已封顶").colored().build());
        } else {
            int needMoney;

            try {
                needMoney = (int) Parser.parse(PlaceholderAPI.setPlaceholders(bukkitPlayer, guildSettings.getPromoteMoneyFormula())).evaluate();
            } catch (ParseException e) {
                e.printStackTrace();
                throw new GuildPromoteException("算式错误");
            }

            inventoryBuilder
                    .item(1, 3, new ItemBuilder()
                            .material(Material.GOLD_INGOT)
                            .displayName("&f使用金币升级")
                            .addLore("&7- &e花费 &b▹ &e" + needMoney)
                            .addLore("&7- &d人数 &b▹ &d" + currentMaxMemberCount + "->" + (currentMaxMemberCount + 1))
                            .colored()
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (!guildBank.has(GuildBank.Type.MONEY, needMoney)) {
                                JulyMessage.sendColoredMessage(bukkitPlayer, "&c金币不足.");
                                return;
                            }

                            guildBank.withdraw(GuildBank.Type.MONEY, needMoney);
                            guild.setMaxMemberCount(guild.getMaxMemberCount() + 1);
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&d升级成功, 宗门目前可最多容纳: &e" + guild.getMaxMemberCount() + "人&d.");
                            close();

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    build();
                                    open();
                                }
                            }.runTaskLater(plugin, 20L);
                        }
                    });
        }

        if (currentMaxMemberCount + 1 > guildSettings.getPromotePointMaxMemberCount()) {
            inventoryBuilder.item(1, 5, new ItemBuilder().material(Material.BARRIER).displayName("&f使用点券升级").addLore("&7- &c已封顶").colored().build());
        } else {
            int needPoints;

            try {
                needPoints = (int) Parser.parse(PlaceholderAPI.setPlaceholders(bukkitPlayer, guildSettings.getPromotePointFormula())).evaluate();
            } catch (ParseException e) {
                e.printStackTrace();
                throw new GuildPromoteException("算式错误");
            }

            inventoryBuilder
                    .item(1, 5, new ItemBuilder()
                            .material(Material.DIAMOND)
                            .displayName("&f使用点券升级")
                            .addLore("&7- &e花费 &b▹ &e" + needPoints)
                            .addLore("&7- &d人数 &b▹ &d" + currentMaxMemberCount + "->" + (currentMaxMemberCount + 1))
                            .colored()
                            .build(), new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (guildBank.has(GuildBank.Type.POINTS, needPoints)) {
                                JulyMessage.sendColoredMessage(bukkitPlayer, "&c点券不足.");
                                return;
                            }

                            guildBank.withdraw(GuildBank.Type.POINTS, needPoints);
                            guild.setMaxMemberCount(guild.getMaxMemberCount() + 1);
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&d升级成功, 宗门目前可最多容纳: &e" + guild.getMaxMemberCount() + "人&d.");
                            close();

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    build();
                                    open();
                                }
                            }.runTaskLater(plugin, 20L);
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
