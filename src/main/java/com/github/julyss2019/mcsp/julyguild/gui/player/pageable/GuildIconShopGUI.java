package com.github.julyss2019.mcsp.julyguild.gui.player.pageable;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.ConfigGuildIcon;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.gui.player.GuildShopGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildIcon;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class GuildIconShopGUI extends BasePageableGUI {
    private Inventory inventory;
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildSettings guildSettings = plugin.getGuildSettings();
    private static Economy vault = plugin.getVaultAPI();
    private static PlayerPointsAPI playerPointsAPI = plugin.getPlayerPointsAPI();
    private List<ConfigGuildIcon> icons = new ArrayList<>();
    private Guild guild;

    public GuildIconShopGUI(GuildPlayer guildPlayer) {
        super(GUIType.ICON_SHOP, guildPlayer);

        this.guild = offlineGuildPlayer.getGuild();
        build();
    }

    @Override
    public void setCurrentPage(int page) {
        super.setCurrentPage(page);

        icons.clear();
        icons.addAll(guildSettings.getConfigGuildIcons());

        InventoryBuilder inventoryBuilder = new InventoryBuilder().title("&e&l图标商店").row(6).colored().listener(new InventoryListener() {
            @Override
            public void onClicked(InventoryClickEvent event) {
                int index = event.getSlot() + getCurrentPage() * 51;

                if (index < icons.size()) {
                    ConfigGuildIcon configGuildIcon = icons.get(index);

                    if (guild.isOwnedIcon(configGuildIcon.getMaterial(), configGuildIcon.getDurability())) {
                        JulyMessage.sendColoredMessage(bukkitPlayer, "&c宗门已经拥有这个图标了.");
                        return;
                    }

                    int fee = configGuildIcon.getFee();
                    ConfigGuildIcon.CostType costType = configGuildIcon.getCostType();

                    if (costType == ConfigGuildIcon.CostType.MONEY) {
                        if (!vault.has(bukkitPlayer, fee)) {
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&c金币不足.");
                            return;
                        }

                        vault.withdrawPlayer(bukkitPlayer, fee);
                        JulyMessage.sendColoredMessage(bukkitPlayer, "&d购买成功!");

                        GuildIcon guildIcon = GuildIcon.createNew(configGuildIcon.getMaterial(), configGuildIcon.getDurability());

                        guild.giveGuildIcon(guildIcon);
                        guild.setCurrentIcon(guildIcon);
                        close();
                        build();
                        open();
                        return;
                    }

                    if (costType == ConfigGuildIcon.CostType.POINTS) {
                        if (playerPointsAPI.look(bukkitPlayer.getUniqueId()) < fee) {
                            JulyMessage.sendColoredMessage(bukkitPlayer, "&c金币不足.");
                            return;
                        }

                        playerPointsAPI.take(bukkitPlayer.getUniqueId(), fee);
                        JulyMessage.sendColoredMessage(bukkitPlayer, "&d购买成功!");

                        GuildIcon guildIcon = GuildIcon.createNew(configGuildIcon.getMaterial(), configGuildIcon.getDurability());

                        guild.giveGuildIcon(guildIcon);
                        guild.setCurrentIcon(guildIcon);
                        close();
                        build();
                        open();
                    }
                }
            }
        });

        inventoryBuilder
                .item(53, CommonItem.BACK, new ItemListener() {
            @Override
            public void onClicked(InventoryClickEvent event) {
                close();
                new GuildShopGUI(guildPlayer).open();
            }
        });

        if (getTotalPage() > 1) {
            inventoryBuilder.item(51, CommonItem.PREVIOUS_PAGE, new ItemListener() {
                @Override
                public void onClicked(InventoryClickEvent event) {
                    if (hasPrecious()) {
                        close();
                        previousPage();
                    }
                }
            });
            inventoryBuilder.item(52, CommonItem.NEXT_PAGE, new ItemListener() {
                @Override
                public void onClicked(InventoryClickEvent event) {
                    if (hasNext()) {
                        close();
                        nextPage();
                    }
                }
            });
        }

        int iconSize = icons.size();
        int itemCounter = page * 51;
        int loopCount = iconSize - itemCounter < 51 ? iconSize - itemCounter : 51;

        for (int i = 0; i < loopCount; i++) {
            ConfigGuildIcon icon = icons.get(itemCounter++);
            boolean isOwned = guild.isOwnedIcon(icon.getMaterial(), icon.getDurability());
            List<String> newLores = new ArrayList<>();

            for (String lore : icon.getLores()) {
                newLores.add(lore.replace("{type}", icon.getCostType().getChineseName()).replace("{amount}", String.valueOf(icon.getFee())));
            }

            ItemBuilder itemBuilder = new ItemBuilder()
                    .material(icon.getMaterial())
                    .durability(icon.getDurability())
                    .displayName(icon.getDisplayName())
                    .addLore("")
                    .addLore(isOwned ? "&b• &a已拥有 &b•" : "&b• &c点击购买 &b•")
                    .addLore("")
                    .addLores(newLores)
                    .colored();

            if (isOwned) {
                itemBuilder.enchant(Enchantment.DURABILITY, 1);
                itemBuilder.addItemFlag(ItemFlag.HIDE_ENCHANTS);
            }

            inventoryBuilder.item(i, itemBuilder.build());
        }

        this.inventory = inventoryBuilder.build();
    }

    @Override
    public int getTotalPage() {
        int iconSize = icons.size();

        return iconSize == 0 ? 1 : iconSize % 51 == 0 ? iconSize / 51 : iconSize / 51 + 1;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
