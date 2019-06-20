package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.Settings;
import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class MainGUI extends BasePageableGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private Settings settings = plugin.getSettings();
    private Inventory inventory;

    public MainGUI(GuildPlayer guildPlayer) {
        super(guildPlayer);

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
                .item(5, 0, new ItemBuilder().material(Material.ENDER_PORTAL_FRAME).displayName("&f我的宗门").colored().build())
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
