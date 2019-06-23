package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildInfoGUI extends BaseGUI {
    private Guild guild;
    private Inventory inventory;
    private int lastPage;

    public GuildInfoGUI(GuildPlayer guildPlayer, Guild guild) {
        this(guildPlayer, guild, 0);
    }

    public GuildInfoGUI(GuildPlayer guildPlayer, Guild guild, int lastPage) {
        super(guildPlayer);

        this.guild = guild;
        this.lastPage = lastPage;
        build();
    }

    @Override
    public void build() {
        this.inventory = new InventoryBuilder().title(guild.getName()).row(6)
                .item(2, 4, new ItemBuilder().material(Material.MAGMA_CREAM).displayName("&a申请加入宗门").colored().build())
                .item(3, 3, new ItemBuilder().material(Material.SKULL_ITEM).durability((short) 3).displayName("&f玩家列表").colored().build())
                .item(3, 4, new ItemBuilder().material(Material.ITEM_FRAME).displayName("&f宗门公告").colored().build())
                .item(3, 5, new ItemBuilder().material(Material.SKULL_ITEM).durability((short) 5).displayName("&f宗主").colored().build())
                .item(53, new ItemBuilder().material(Material.BOOK_AND_QUILL).displayName("&c返回至主界面").colored().build(), new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        MainGUI gui = new MainGUI(guildPlayer);

                        gui.setCurrentPage(gui.isValidPage(lastPage) ? lastPage : 0);
                        gui.open();
                    }
                })
                .build();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
