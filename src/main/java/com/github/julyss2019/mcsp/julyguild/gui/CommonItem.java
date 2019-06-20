package com.github.julyss2019.mcsp.julyguild.gui;

import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CommonItem {
    private static ItemStack nextPageItem = new ItemBuilder().material(Material.BOOK).displayName("&c下一页").colored().build();
    private static ItemStack previousPageItem = new ItemBuilder().material(Material.BOOK).displayName("&c上一页").colored().build();

    public static ItemStack getNextPageItem() {
        return nextPageItem;
    }

    public static ItemStack getPreviousPageItem() {
        return previousPageItem;
    }
}
