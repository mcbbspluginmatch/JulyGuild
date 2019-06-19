package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildOwner;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Guild {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private String uuid;
    private GuildOwner owner;
    private File file;
    private YamlConfiguration yml;

    protected Guild(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public Guild init() {
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
        this.uuid = yml.getString("uuid");
        this.owner = new GuildOwner(yml.getString("owner"));
        return this;
    }

    public void delete() {

    }

    public String getUUID() {
        return uuid;
    }
}
