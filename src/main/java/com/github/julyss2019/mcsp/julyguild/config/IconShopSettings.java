package com.github.julyss2019.mcsp.julyguild.config;

import com.github.julyss2019.mcsp.julylibrary.config.Config;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IconShopSettings {
    @Config(path = "default_icon.material")
    private Material defIconMaterial;
    @Config(path = "default_icon.durability")
    private short defIconDurability;

    private List<ConfigGuildIcon> configGuildIcons = new ArrayList<>();

    public void reset() {
        this.configGuildIcons.clear();
    }

    public void addConfigGuildIcon(@NotNull ConfigGuildIcon configGuildIcon) {
        configGuildIcons.add(configGuildIcon);
    }

    public List<ConfigGuildIcon> getConfigGuildIcons() {
        return new ArrayList<>(configGuildIcons);
    }

    public Material getDefIconMaterial() {
        return defIconMaterial;
    }

    public short getDefIconDurability() {
        return defIconDurability;
    }
}
