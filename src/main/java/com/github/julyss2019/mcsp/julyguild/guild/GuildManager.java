package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.player.MainGUI;
import com.github.julyss2019.mcsp.julyguild.guild.log.GuildCreateLog;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GuildManager {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private static FileLogger fileLogger = plugin.getFileLogger();
    private Map<String, Guild> guildMap = new HashMap<>();

    public GuildManager() {}

    /**
     * 得到宗会列表（不排序）
     * @return
     */
    public List<Guild> getGuilds() {
        return getGuilds(false);
    }

    /**
     * 创建宗会
     * @param guildOwner 宗会主人
     * @return
     */
    public boolean createGuild(@NotNull GuildPlayer guildOwner, @NotNull String guildName) {
        if (guildOwner.isInGuild()) {
            throw new IllegalArgumentException("主人已经有宗会了!");
        }

        String uuid = UUID.randomUUID().toString();
        File file = new File(plugin.getDataFolder(), "guilds" + File.separator + uuid + ".yml");

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
        long creationTime = System.currentTimeMillis();

        yml.set("name", guildName);
        yml.set("uuid", uuid);
        yml.set("owner", guildOwner.getName());
        yml.set("creation_time", creationTime);

        if (YamlUtil.saveYaml(yml, file)) {
            loadGuild(file);
            guildMap.put(uuid, getGuild(uuid));
            plugin.writeGuildLog(FileLogger.LoggerLevel.INFO, new GuildCreateLog(creationTime, uuid, guildOwner.getName(), guildName, true));
            guildOwner.setGuild(getGuild(uuid));

            // 更新所有玩家的GUI
            for (GuildPlayer guildPlayer : guildPlayerManager.getOnlineGuildPlayers()) {
                GUI usingGUI = guildPlayer.getUsingGUI();

                if (usingGUI instanceof MainGUI) {
                    usingGUI.close();
                    ((MainGUI) usingGUI).setCurrentPage(0); // 刷新
                    usingGUI.open();
                }
            }

            return true;
        }

        plugin.writeGuildLog(FileLogger.LoggerLevel.INFO, new GuildCreateLog(creationTime, uuid, guildOwner.getName(), guildName, false));
        return false;
    }

    /**
     * 得到宗会列表
     * @param sorted 排序
     * @return
     */
    public List<Guild> getGuilds(boolean sorted) {
        List<Guild> guilds = new ArrayList<>(guildMap.values());

        return sorted ? guilds.stream().sorted(new Comparator<Guild>() {
            @Override
            public int compare(Guild o1, Guild o2) {
                return o1.getLevel() > o2.getLevel() ? -1 : 0;
            }
        }).collect(Collectors.toList()) : guilds;
    }

    /**
     * 卸载公会
     * @param guild
     */
    private void unloadGuild(Guild guild) {
        guildMap.remove(guild.getUUID());
    }

    private void loadGuild(String uuid) {
        Guild guild = new Guild(new File(plugin.getDataFolder(), "guilds" + File.separator + uuid + ".yml"));

        guild.load();
        guildMap.put(guild.getUUID(), guild);
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
