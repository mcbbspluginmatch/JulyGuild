package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildOwner;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guild {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private String uuid;
    private GuildOwner owner;
    private Map<String, GuildMember> memberMap = new HashMap<>();
    private File file;
    private YamlConfiguration yml;
    private int level;
    private String name;

    protected Guild(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public Guild load() {
        memberMap.clear();

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

        if (yml.getBoolean("deleted")) {
            throw new IllegalArgumentException("该工会已被删除.");
        }

        this.uuid = yml.getString("uuid");
        this.owner = new GuildOwner(yml.getString("owner"));
        this.name = yml.getString("name");

        for (String memberName : yml.getStringList("members")) {
            Permission permission = Permission.valueOf(yml.getString("members." + memberName + ".permission"));

            switch (permission) {
                case MEMBER:
                    memberMap.put(memberName, new GuildMember(memberName));
                    break;
            }
        }

        return this;
    }

    public GuildOwner getOwner() {
        return owner;
    }

    public List<GuildMember> getMembers() {
        return new ArrayList<>(memberMap.values());
    }

    public boolean delete() {
        yml.set("deleted", "true");
        return YamlUtil.saveYaml(yml, file);
    }

    public String getUUID() {
        return uuid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
