package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GuildManager {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private Map<String, Guild> guildMap = new HashMap<>();

    public GuildManager() {}

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

        guild.init();
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
