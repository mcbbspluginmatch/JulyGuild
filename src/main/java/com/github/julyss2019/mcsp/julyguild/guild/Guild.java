package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.guild.exception.GuildLoadException;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildOwner;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.guild.request.RequestType;
import com.github.julyss2019.mcsp.julyguild.guild.request.player.BasePlayerRequest;
import com.github.julyss2019.mcsp.julyguild.guild.request.player.JoinRequest;
import com.github.julyss2019.mcsp.julyguild.guild.request.player.PlayerRequest;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class Guild {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private static GuildManager guildManager = plugin.getGuildManager();
    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
    private File file;
    private YamlConfiguration yml;

    private boolean valid;
    private boolean deleted;
    private String uuid;
    private GuildOwner owner;
    private String name;
    private Map<String, GuildMember> memberMap = new HashMap<>();
    private int level;
    private Material icon = Material.SIGN;
    private int maxMemberCount;
    private long creationTime;
    private List<String> announcements;
    private Map<String, List<PlayerRequest>> playerRequestMap = new HashMap<>();

    protected Guild(File file) {
        this.file = file;

        load();
    }

    public boolean isOwner(OfflineGuildPlayer offlineGuildPlayer) {
        return offlineGuildPlayer.equals(owner.getOfflineGuildPlayer());
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
            throw new GuildLoadException("宗会不存在!");
        }

        this.yml = YamlConfiguration.loadConfiguration(file);

        this.deleted = yml.getBoolean("deleted");
        this.uuid = yml.getString("uuid");
        this.owner = new GuildOwner(guildPlayerManager.getOfflineGuildPlayer(yml.getString("owner")));
        this.name = yml.getString("name");
        this.level = yml.getInt("level");
        this.maxMemberCount = yml.getInt("max_member_count");
        this.icon = Material.valueOf(yml.getString("icon", Material.SIGN.name()));
        this.announcements = yml.getStringList("announcements");
        this.creationTime = yml.getLong("creation_time");

        for (String memberName : yml.getStringList("members")) {
            Permission permission = Permission.valueOf(yml.getString("members." + memberName + ".permission"));

            switch (permission) {
                case MEMBER:
                    memberMap.put(memberName, new GuildMember(guildPlayerManager.getOfflineGuildPlayer(memberName)));
                    break;
            }
        }

        if (yml.isConfigurationSection("player_requests")) {
            // 载入玩家请求
            for (String uuid : yml.getConfigurationSection("player_requests").getKeys(false)) {
                ConfigurationSection requestSection = yml.getConfigurationSection("player_requests").getConfigurationSection(uuid);
                RequestType requestType = RequestType.valueOf(requestSection.getString("type"));
                BasePlayerRequest request = null;
                String playerName = requestSection.getString("player");

                switch (requestType) {
                    case JOIN:
                        request = new JoinRequest();
                        break;
                }

                request.setOfflineGuildPlayer(guildPlayerManager.getOfflineGuildPlayer(playerName));
                request.setTime(requestSection.getLong("time"));
                request.setUUID(UUID.fromString(uuid));

                if (!playerRequestMap.containsKey(playerName)) {
                    playerRequestMap.put(playerName, new ArrayList<>());
                }

                playerRequestMap.get(playerName).add(request);
            }
        }

        return this;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isValid() {
        return valid;
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
     * 得到成员：包含主人，不排序
     * @return
     */
    public List<GuildMember> getMembers() {
        return getMembers(true, false);
    }

    /**
     * 得到成员
     * @return
     */
    public List<GuildMember> getMembers(boolean withOwner, boolean sorted) {
        List<GuildMember> guildMembers = new ArrayList<>(memberMap.values());

        if (withOwner) {
            guildMembers.add(owner);
        }

        if (sorted) {
            guildMembers.sort((o1, o2) -> isOwner(o1.getOfflineGuildPlayer()) ? -1 : isOwner(o2.getOfflineGuildPlayer()) ? 1 : 0);
        }

        return guildMembers;
    }

    /**
     * 删除工会
     * @return
     */
    public void delete() {
        yml.set("deleted", "true");
        YamlUtil.saveYaml(yml, file);

        for (GuildMember guildMember : getMembers(true, false)) {
            guildMember.getOfflineGuildPlayer().setGuild(null);
        }

        guildManager.unloadGuild(this);
        this.valid = false;
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
        yml.set("level", level);
        YamlUtil.saveYaml(yml, file);
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
    public void setMaxMemberCount(int maxMemberCount) {
        yml.set("max_member_count", maxMemberCount);
        YamlUtil.saveYaml(yml, file);
        this.maxMemberCount = maxMemberCount;
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

    public void setIcon(Material icon) {
        yml.set("icon", icon.name());
        YamlUtil.saveYaml(yml, file);
        this.icon = icon;
    }

    public void setAnnouncements(List<String> announcements) {
        yml.set("announcements", announcements);
        YamlUtil.saveYaml(yml, file);
        this.announcements = announcements;
    }

    public boolean hasRequest(GuildPlayer guildPlayer, RequestType requestType) {
        String playerName = guildPlayer.getName();

        if (playerRequestMap.containsKey(playerName)) {
            for (PlayerRequest request : playerRequestMap.get(playerName)) {
                if (request.getType() == requestType) {
                    return true;
                }
            }
        }

        return false;
    }

    public String addRequest(PlayerRequest playerRequest) {
        String uuid = UUID.randomUUID().toString();
        String playerName = playerRequest.getOfflineGuildPlayer().getName();

        yml.set("player_requests." + uuid + ".player", playerRequest.getOfflineGuildPlayer().getName());
        yml.set("player_requests." + uuid + ".type", playerRequest.getType().name());
        yml.set("player_requests." + uuid + ".time", playerRequest.getTime());
        YamlUtil.saveYaml(yml, file);

        if (!playerRequestMap.containsKey(playerName)) {
            playerRequestMap.put(playerName, new ArrayList<>());
        }

        List<PlayerRequest> playerRequests = playerRequestMap.get(playerRequest.getOfflineGuildPlayer().getName());

        playerRequests.add(playerRequest);
        return uuid;
    }

    public List<String> getAnnouncements() {
        return announcements;
    }
}
