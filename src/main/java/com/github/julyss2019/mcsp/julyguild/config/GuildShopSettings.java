package com.github.julyss2019.mcsp.julyguild.config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuildShopSettings {
    private List<ConfigGuildShopItem> configGuildShopItems = new ArrayList<>();

    public void reset() {
        configGuildShopItems.clear();
    }

    public void addItem(@NotNull ConfigGuildShopItem item) {
        configGuildShopItems.add(item);
        configGuildShopItems.sort(Comparator.comparingInt(ConfigGuildShopItem::getIndex));
    }

    public List<ConfigGuildShopItem> getConfigGuildShopItems() {
        return new ArrayList<>(configGuildShopItems);
    }
}
