package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class MyGuildGUI extends BaseGUI {
    private int lastPage;
    private Inventory inventory;

    public MyGuildGUI(GuildPlayer guildPlayer) {
        super(guildPlayer);

        build();
    }

    @Override
    public void build() {
        InventoryBuilder inventoryBuilder = new InventoryBuilder().title("&e&l我的宗门").colored().row(6);

        if (guildPlayer.getGuild().isOwner(guildPlayer)) {

        } else {
            inventoryBuilder
                    .item(1, 4, new SkullItemBuilder().owner(guildPlayer.getName()).displayName("&f个人信息").colored().build())
                    .item(2, 3, new ItemBuilder().material(Material.PAINTING).displayName("&f宗门公告").colored().build())
                    .item(2, 4, new ItemBuilder().material(Material.SKULL_ITEM).durability((short) 3).displayName("&f宗门成员").colored().build())
                    .item(2, 5, new ItemBuilder().material(Material.GOLD_NUGGET).displayName("&e贡献金币").colored().build())
                    .item(3, 4, new ItemBuilder().material(Material.CHEST).displayName("&f宗门仓库").colored().build())
                    .item(53, new ItemBuilder().material(Material.DARK_OAK_DOOR_ITEM).displayName("&c退出宗门").colored().build());

        }

        this.inventory = inventoryBuilder.build();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
