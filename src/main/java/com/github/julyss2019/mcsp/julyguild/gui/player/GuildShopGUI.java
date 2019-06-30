package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.gui.player.pageable.GuildIconShopGUI;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildShopGUI extends BaseGUI {
    private Inventory inventory;

    public GuildShopGUI(GuildPlayer guildPlayer) {
        super(GUIType.SHOP, guildPlayer);

        build();
    }

    @Override
    public void build() {
        this.inventory = new InventoryBuilder()
                .title("&e&l宗门商店")
                .colored()
                .row(3)
                .item(1, 4, new ItemBuilder()
                                .material(Material.BEACON)
                                .displayName("&a宗门图标商店")
                                .addLore("&7- &f可购买图标来更改主界面显示的图标")
                                .colored()
                                .build(), new ItemListener() {
                            @Override
                            public void onClicked(InventoryClickEvent event) {
                                close();
                                new GuildIconShopGUI(guildPlayer).open();
                            }
                        }
                )
                .item(26, CommonItem.BACK, new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        new GuildManageGUI(guildPlayer).open();
                    }
                })
                .build();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
