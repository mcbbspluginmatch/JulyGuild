package com.github.julyss2019.mcsp.julyguild.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class OfflineGuildPlayer {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildManager guildManager = plugin.getGuildManager();
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
    public OfflineGuildPlayer load() {
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
        this.guild = guildManager.getGuild(yml.getString("guild"));
        return this;
    }

    public String getName() {
        return name;
    }

    public Guild getGuild() {
        return guild;
    }

    public boolean setGuild(Guild guild) {
        yml.set("guild", guild.getUUID());

        if (YamlUtil.saveYaml(yml, file)) {
            this.guild = guild;
            return true;
        }

        return false;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(name);
    }

    public boolean isOnline() {
        Player tmp = getBukkitPlayer();

        return tmp != null && tmp.isOnline();
    }

    public boolean isInGuild() {
        return getGuild() != null;
    }
}
