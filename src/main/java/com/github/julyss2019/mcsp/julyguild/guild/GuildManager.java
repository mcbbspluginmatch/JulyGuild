package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuildManager {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private Map<String, Guild> guildMap = new HashMap<>();

    public GuildManager() {}

    public List<Guild> getGuilds() {
        return getGuilds(false);
    }

    public boolean createGuild(@NotNull GuildPlayer guildOwner) {
        if (guildOwner.isInGuild()) {
            throw new IllegalArgumentException("主人已经有工会了!");
        }

        String uuid = UUID.randomUUID().toString();
        File file = new File(plugin.getDataFolder(), "guilds" + File.separator + uuid);

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

        yml.set("uuid", uuid);
        yml.set("owner", guildOwner.getName());
        return YamlUtil.saveYaml(yml, file);
    }

    public List<Guild> getGuilds(boolean sorted) {
        List<Guild> guilds = new ArrayList<>(guildMap.values());

        return guilds;
    }

    /**
     * 卸载公会
     * @param guild
     */
    private void unloadGuild(Guild guild) {
        guildMap.remove(guild.getUUID());
    }

    /**
     * 载入公会
     * @param file
     */
    private void loadGuild(File file) {
        Guild guild = new Guild(file);

        guild.load();
        guildMap.put(guild.getUUID(), guild);
    }

    /**
     * 载入所有公会
     */
    public void loadGuilds() {
        guildMap.clear();

        File guildFolder = new File(plugin.getDataFolder(), "guilds");

        if (!guildFolder.exists()) {
            return;
        }

        File[] guildFiles = guildFolder.listFiles();

        if (guildFiles != null) {
            for (File guildFile : guildFiles) {
                loadGuild(guildFile);
            }
        }
    }

    /**
     * 得到公会
     * @param uuid
     * @return
     */
    public Guild getGuild(String uuid) {
        return guildMap.get(uuid);
    }
}
