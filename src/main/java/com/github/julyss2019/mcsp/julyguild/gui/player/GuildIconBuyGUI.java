package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.ConfigGuildIcon;
import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.gui.player.pageable.GuildIconShopGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildBank;
import com.github.julyss2019.mcsp.julyguild.guild.GuildIcon;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildIconBuyGUI extends BaseGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();

    private ConfigGuildIcon configGuildIcon;
    private Inventory inventory;
    private Guild guild;
    private GuildBank guildBank;

    public GuildIconBuyGUI(GuildPlayer guildPlayer, ConfigGuildIcon configGuildIcon) {
        super(GUIType.ICON_BUY, guildPlayer);

        this.guild = guildPlayer.getGuild();
        this.configGuildIcon = configGuildIcon;
        this.guildBank = guild.getGuildBank();
        build();
    }

    @Override
    public void build() {
        super.build();

        this.inventory = new InventoryBuilder()
                .title("&e&l请选择支付方式")
                .row(3)
                .colored()
                .item(26, CommonItem.BACK, new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        new GuildIconShopGUI(guildPlayer).open();
                    }
                })

                .item(1, 2, new ItemBuilder()
                        .material(configGuildIcon.isMoneyPayEnabled() ? Material.GOLD_INGOT : Material.BARRIER)
                        .displayName("&f使用 &a金币x" + configGuildIcon.getMoneyCost() + " &f支付")
                        .addLore("")
                        .addLore(configGuildIcon.isMoneyPayEnabled() ? "&a• &d点击支付&a •" : "&a• 未启用 &a•")
                        .addLore("")
                        .colored()
                        .build(), new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        int fee = configGuildIcon.getMoneyCost();

                        if (!guildBank.has(GuildBank.Type.MONEY, fee)) {
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&c宗门银行金币不足.");
                            return;
                        }

                        guildBank.withdraw(GuildBank.Type.MONEY, fee);
                        JulyMessage.sendColoredMessage(bukkitPlayer, "&d购买成功!");

                        GuildIcon guildIcon = GuildIcon.createNew(configGuildIcon.getMaterial(), configGuildIcon.getDurability());

                        guild.giveGuildIcon(guildIcon);
                        guild.setCurrentIcon(guildIcon);
                        close();
                    }
                })
                .item(1, 6, new ItemBuilder()
                        .material(configGuildIcon.isPointsPayEnabled() ? Material.DIAMOND : Material.BARRIER)
                        .displayName("&f使用 &a点券x" + configGuildIcon.getPointsCost() + " &f支付")
                        .addLore("")
                        .addLore(configGuildIcon.isPointsPayEnabled() ? "&a• &d点击支付&a •" : "&a• 未启用 &a•")
                        .addLore("")
                        .colored()
                        .build(), new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        int fee = configGuildIcon.getPointsCost();

                        if (!guildBank.has(GuildBank.Type.POINTS, fee)) {
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&c宗门银行点券不足.");
                            return;
                        }

                        guildBank.withdraw(GuildBank.Type.POINTS, fee);
                        JulyMessage.sendColoredMessage(bukkitPlayer, "&d购买成功!");

                        GuildIcon guildIcon = GuildIcon.createNew(configGuildIcon.getMaterial(), configGuildIcon.getDurability());

                        guild.giveGuildIcon(guildIcon);
                        guild.setCurrentIcon(guildIcon);
                        close();
                    }
                }).build();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
