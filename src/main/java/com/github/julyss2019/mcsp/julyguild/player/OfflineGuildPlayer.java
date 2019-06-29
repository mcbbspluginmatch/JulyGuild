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
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OfflineGuildPlayer {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildManager guildManager = plugin.getGuildManager();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private String name;
    private File file;
    private YamlConfiguration yml;
    private Guild guild;

    protected OfflineGuildPlayer(String name) {
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

    public void setGuild(Guild guild) {
        yml.set("guild", guild == null ? null : guild.getUUID());
        save();
        this.guild = guild;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(name);
    }

    public boolean isOnline() {
        Player tmp = getBukkitPlayer();

        return tmp != null && tmp.isOnline();
    }

    public GuildPlayer getGuildPlayer() {
        return !isOnline() ? null : guildPlayerManager.getGuildPlayer(getBukkitPlayer());
    }

    public boolean isInGuild() {
        return guild != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfflineGuildPlayer)) return false;
        OfflineGuildPlayer that = (OfflineGuildPlayer) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void save() {
        YamlUtil.saveYaml(yml, file);
    }
}
