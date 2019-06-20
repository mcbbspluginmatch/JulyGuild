package com.github.julyss2019.mcsp.julyguild.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class OfflineGuildPlayer {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private String name;
    private Guild guild;
    private File file;
    private YamlConfiguration yml;

    public OfflineGuildPlayer(String name) {
        this.name = name;
    }

    /**
     * 初始化
     * @return
     */
    public OfflineGuildPlayer init() {
        this.file = new File(plugin.getDataFolder(), "players" + File.separator + name + ".yml");

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.yml = YamlConfiguration.loadConfiguration(file);
        return this;
    }

    public String getName() {
        return name;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(name);
    }

    public boolean isInGuild() {
        return getGuild() != null;
    }
}
