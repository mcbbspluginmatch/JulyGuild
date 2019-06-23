package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildOwner;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guild {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
    private File file;
    private YamlConfiguration yml;

    private String uuid;
    private GuildOwner owner;
    private String name;
    private Map<String, GuildMember> memberMap = new HashMap<>();
    private int level;
    private Material icon = Material.SIGN;
    private int maxMemberCount;
    private long creationTime;
    private String announcement;

    protected Guild(File file) {
        this.file = file;
    }

    public boolean isOwner(OfflineGuildPlayer offlineGuildPlayer) {
        return offlineGuildPlayer.equals(owner);
    }

    /**
     * 工会文件
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     * 公会名
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 载入（或初始化）
     * @return
     */
    public Guild load() {
        memberMap.clear();

        if (!file.exists()) {
            throw new GuildException("宗会不存在!");
        }

        this.yml = YamlConfiguration.loadConfiguration(file);

        if (yml.getBoolean("deleted")) {
            throw new IllegalArgumentException("该宗会已被删除.");
        }

        this.uuid = yml.getString("uuid");
        this.owner = new GuildOwner(yml.getString("owner"));
        this.name = yml.getString("name");
        this.level = yml.getInt("level");
        this.maxMemberCount = yml.getInt("max_member_count");

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

    /**
     * 成员数量
     * @return
     */
    public int getMemberCount() {
        return memberMap.size();
    }

    /**
     * 得到工会主任
     * @return
     */
    public GuildOwner getOwner() {
        return owner;
    }

    /**
     * 得到成员
     * @return
     */
    public List<GuildMember> getMembers() {
        return new ArrayList<>(memberMap.values());
    }

    /**
     * 删除工会
     * @return
     */
    public boolean delete() {
        yml.set("deleted", "true");
        return YamlUtil.saveYaml(yml, file);
    }

    /**
     * 工会唯一区别符
     * @return
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * 得到工会等级
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     * 设置工会等级
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 得到工会图标
     * @return
     */
    public Material getIcon() {
        return icon;
    }

    /**
     * 得到最大成员数
     * @return
     */
    public int getMaxMemberCount() {
        return maxMemberCount;
    }

    /**
     * 设置最大成员数
     * @param maxMemberCount
     * @return
     */
    public boolean setMaxMemberCount(int maxMemberCount) {
        yml.set("max_member_count", maxMemberCount);

        if (YamlUtil.saveYaml(yml, file)) {
            this.maxMemberCount = maxMemberCount;
            return true;
        }

        return false;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public List<String> replaceVariables(List<String> list) {
        List<String> result = new ArrayList<>();

        for (String s : list) {
            result.add(replaceVariables(s));
        }

        return result;
    }

    public String replaceVariables(String s) {
        String result = s;
        Map<String, String> variableMap = new HashMap<>();

        variableMap.put("%GUILD_LEVEL%", String.valueOf(getLevel()));
        variableMap.put("%GUILD_OWNER%", getOwner().getName());
        variableMap.put("%GUILD_MEMBER_COUNT%", String.valueOf(getMemberCount()));
        variableMap.put("%GUILD_MAX_MEMBER_COUNT%", String.valueOf(getMaxMemberCount()));
        variableMap.put("%GUILD_CREATION_TIME%", SDF.format(getCreationTime()));

        for (Map.Entry<String, String> entry : variableMap.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
