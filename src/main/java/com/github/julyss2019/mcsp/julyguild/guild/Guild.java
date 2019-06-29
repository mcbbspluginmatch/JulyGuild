package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.exception.GuildLoadException;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildAdmin;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildOwner;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.guild.request.*;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class Guild {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildSettings guildSettings = plugin.getGuildSettings();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private static GuildManager guildManager = plugin.getGuildManager();
    private static GuildIcon defGuildIcon = GuildIcon.createNew(guildSettings.getDefIconMaterial(), guildSettings.getDefIconDurability());
    private File file;
    private YamlConfiguration yml;

    private boolean valid;
    private boolean deleted;
    private UUID uuid;
    private GuildOwner owner;
    private String name;
    private Map<String, GuildMember> memberMap = new HashMap<>();
    private Material icon = Material.SIGN;
    private int maxMemberCount;
    private long creationTime;
    private List<String> announcements;
    private Map<String, Request> uuidRequestMap = new HashMap<>();
    private Map<String, List<Request>> playerRequestMap = new HashMap<>();
    private double balance;
    private Map<String, GuildIcon> iconMap = new HashMap<>();
    private GuildIcon currentIcon;

    protected Guild(File file) {
        this.file = file;
    }

    public void init() {
        if (!file.exists()) {
            throw new GuildLoadException("宗门不存在!");
        }

        this.yml = YamlConfiguration.loadConfiguration(file);
        this.deleted = yml.getBoolean("deleted");
        this.valid = !deleted;
        this.uuid = UUID.fromString(yml.getString("uuid"));
    }

    /**
     * 载入（或初始化）
     * @return
     */
    public Guild load() {
        memberMap.clear();

        this.owner = new GuildOwner(this, guildPlayerManager.getOfflineGuildPlayer(yml.getString("owner.name")));
        this.name = yml.getString("name");
        this.balance = yml.getDouble("balance");
        this.maxMemberCount = yml.getInt("max_member_count", guildSettings.getGuildDefMaxMemberCount());
        this.icon = Material.valueOf(yml.getString("icon", Material.SIGN.name()));
        this.announcements = yml.getStringList("announcements");
        this.creationTime = yml.getLong("creation_time");

        if (announcements.size() == 0) {
            announcements.add("&d欢迎加入!");
        }

        if (yml.contains("members")) {
            for (String memberName : yml.getConfigurationSection("members").getKeys(false)) {
                loadMember(memberName);
            }
        }

        memberMap.put(owner.getName(), owner);

        if (yml.contains("requests")) {
            // 载入玩家请求
            for (String uuid : yml.getConfigurationSection("requests").getKeys(false)) {
                ConfigurationSection requestSection = yml.getConfigurationSection("requests").getConfigurationSection(uuid);
                RequestType requestType = RequestType.valueOf(requestSection.getString("type"));
                BaseRequest request = null;
                String playerName = requestSection.getString("player");

                switch (requestType) {
                    case JOIN:
                        request = new JoinRequest();
                        break;
                }

                request.setOfflineGuildPlayer(guildPlayerManager.getOfflineGuildPlayer(playerName));
                request.setTime(requestSection.getLong("time"));
                request.setUUID(UUID.fromString(uuid));

                uuidRequestMap.put(uuid, request);

                if (!playerRequestMap.containsKey(playerName)) {
                    playerRequestMap.put(playerName, new ArrayList<>());
                }

                playerRequestMap.get(playerName).add(request);
            }
        }

        if (yml.contains("iconMap")) {
            for (String uuid : yml.getConfigurationSection("iconMap").getKeys(false)) {
                ConfigurationSection iconSection = yml.getConfigurationSection("iconMap").getConfigurationSection(uuid);
                GuildIcon icon = new GuildIcon(Material.valueOf(iconSection.getString("material")), (short) iconSection.getInt("durability"), UUID.fromString(uuid));

                iconMap.put(uuid, icon);
            }
        }

        this.currentIcon = iconMap.get(yml.getString("current_icon"));

        if (currentIcon == null) {
            this.currentIcon = defGuildIcon;
        }

        return this;
    }

    public GuildIcon getCurrentIcon() {
        return currentIcon;
    }

    public void setCurrentIcon(GuildIcon guildIcon) {
        String uuid = guildIcon.getUuid().toString();

        if (!iconMap.containsKey(uuid)) {
            throw new IllegalArgumentException("图标不存在");
        }

        yml.set("current_icon", uuid);
        save();
        this.currentIcon = guildIcon;
    }

    public void giveGuildIcon(GuildIcon guildIcon) {
        String uuid = guildIcon.getUuid().toString();

        yml.set("iconMap." + uuid + ".material", guildIcon.getMaterial().name());
        yml.set("iconMap." + uuid + ".durability", guildIcon.getDurability());
        save();
        iconMap.put(uuid, guildIcon);
    }

    public Collection<GuildIcon> getIcons() {
        return iconMap.values();
    }

    public void depositBalance(double amount) {
        setBalance(balance + amount);
    }

    public void withdrawBalance(double amount) {
        setBalance(balance - amount);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        yml.set("balance", balance);
        save();
        this.balance = balance;
    }

    public void loadMember(String memberName) {
        Permission permission = Permission.valueOf(yml.getString("members." + memberName + ".permission"));
        OfflineGuildPlayer offlineGuildPlayer = guildPlayerManager.getOfflineGuildPlayer(memberName);

        switch (permission) {
            case MEMBER:
                memberMap.put(memberName, new GuildMember(this, offlineGuildPlayer));
                break;
            case ADMIN:
                memberMap.put(memberName, new GuildAdmin(this, offlineGuildPlayer));
                break;
        }
    }

    public void setMemberPermission(GuildMember guildMember, Permission permission) {
        String memberName = guildMember.getName();

        if (!memberMap.containsKey(memberName)) {
            throw new IllegalArgumentException("成员不存在");
        }

        yml.set("members." + memberName + ".permission", permission.name());
        save();
        memberMap.remove(memberName);
        loadMember(memberName);
    }

    /**
     * 是否为宗主
     * @param name
     * @return
     */
    public boolean isOwner(String name) {
        return getOwner().getName().equalsIgnoreCase(name);
    }

    /**
     * 得到成员
     * @param name
     * @return
     */
    public GuildMember getMember(String name) {
        return memberMap.get(name);
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
     * 是否已被解散
     * @return
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * 是否有效
     * @return
     */
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

    public boolean hasMember(String playerName) {
        return memberMap.containsKey(playerName);
    }

    /**
     * 得到成员：包含主人
     * @return
     */
    public List<GuildMember> getMembers() {
        return new ArrayList<>(memberMap.values());
    }

    public static void sortMembers(List<GuildMember> guildMembers) {
        guildMembers.sort((o1, o2) -> o1.getPermission().getLevel() > o2.getPermission().getLevel() ? -1 : 0);
    }

    /**
     * 添加成员
     * @param offlineGuildPlayer
     */
    public void addMember(OfflineGuildPlayer offlineGuildPlayer) {
        String playerName = offlineGuildPlayer.getName();

        if (memberMap.containsKey(playerName) || offlineGuildPlayer.equals(owner.getOfflineGuildPlayer())) {
            throw new IllegalArgumentException("成员已存在");
        }

        yml.set("members." + playerName + ".permission", Permission.MEMBER.name());
        yml.set("members." + playerName + ".join_time", System.currentTimeMillis());
        YamlUtil.saveYaml(yml, file);
        offlineGuildPlayer.setGuild(this);
        memberMap.put(playerName, new GuildMember(this, offlineGuildPlayer));
        updateMembersGUI(GUIType.MEMBER, GUIType.MAIN);
    }

    /**
     * 删除成员
     * @param guildMember
     */
    public void removeMember(GuildMember guildMember) {
        String memberName = guildMember.getName();

        if (guildMember instanceof GuildOwner) {
            throw new IllegalArgumentException("不能删除宗主成员");
        }

        if (!memberMap.containsKey(memberName)) {
            throw new IllegalArgumentException("成员不存在");
        }

        yml.set("members." + memberName, null);
        save();
        guildMember.getOfflineGuildPlayer().setGuild(null);
        memberMap.remove(memberName);
        updateMembersGUI(GUIType.MEMBER);
    }

    /**
     * 删除工会
     * @return
     */
    public void delete() {
        yml.set("deleted", true);
        YamlUtil.saveYaml(yml, file);

        for (GuildMember guildMember : getMembers()) {
            guildMember.getOfflineGuildPlayer().setGuild(null);

            if (guildMember.getOfflineGuildPlayer().isOnline()) {
                guildMember.getOfflineGuildPlayer().getGuildPlayer().updateGUI(GUIType.values());
            }
        }

        guildManager.unloadGuild(this);
        this.valid = false;
    }

    /**
     * 工会唯一区别符
     * @return
     */
    public UUID getUUID() {
        return uuid;
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

    /**
     * 得到创建时间
     * @return
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * 图标
     * @param icon
     */
    public void setIcon(Material icon) {
        yml.set("icon", icon.name());
        YamlUtil.saveYaml(yml, file);
        this.icon = icon;
    }

    /**
     * 设置公告
     * @param announcements
     */
    public void setAnnouncements(List<String> announcements) {
        yml.set("announcements", announcements);
        YamlUtil.saveYaml(yml, file);
        this.announcements = announcements;
    }

    /**
     * 是否有请求
     * @param offlineGuildPlayer
     * @param requestType
     * @return
     */
    public boolean hasRequest(OfflineGuildPlayer offlineGuildPlayer, RequestType requestType) {
        for (Request request : getRequests(false)) {
            if (request.getOfflineGuildPlayer().equals(offlineGuildPlayer) && requestType == request.getType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 删除玩家的请求
     * @param request
     */
    public void removeRequest(Request request) {
        String uuid = request.getUUID().toString();

        yml.set("requests." + uuid, null);
        save();
        uuidRequestMap.remove(uuid);
        updateMembersGUI(GUIType.PLAYER_REQUEST);
    }

    /**
     * 更新所有成员的GUI
     */
    public void updateMembersGUI(GUIType... guiTypes) {
        for (GuildMember guildMember : getMembers()) {
            OfflineGuildPlayer offlineGuildPlayer = guildMember.getOfflineGuildPlayer();

            if (offlineGuildPlayer.isOnline()) {
                offlineGuildPlayer.getGuildPlayer().updateGUI(guiTypes);
            }
        }
    }

    /**
     * 添加玩家的请求
     * @param request
     * @return
     */
    public String addRequest(Request request) {
        if (request.isOnlyOne() && hasRequest(request.getOfflineGuildPlayer(), request.getType())) {
            throw new IllegalArgumentException("&e请求类型只允许存在一个!");
        }

        String uuid = request.getUUID().toString();

        yml.set("requests." + uuid + ".player", request.getOfflineGuildPlayer().getName());
        yml.set("requests." + uuid + ".type", request.getType().name());
        yml.set("requests." + uuid + ".time", request.getCreationTime());
        save();
        uuidRequestMap.put(uuid, request);
        updateMembersGUI(GUIType.PLAYER_REQUEST);
        return uuid;
    }

    /**
     * 得到请求（不排序）
     * @return
     */
    public List<Request> getRequests() {
        return getRequests(false);
    }

    public List<Request> getPlayerRequests(String playerName) {
        return playerRequestMap.get(playerName);
    }

    /**
     * 得到请求
     * @param sorted 排序
     * @return
     */
    public List<Request> getRequests(boolean sorted) {
        List<Request> result = new ArrayList<>(uuidRequestMap.values());

        if (sorted) {
            result.sort((o1, o2) -> o1.getCreationTime() < o2.getCreationTime() ? -1 : 0);
        }

        return result;
    }

    /**
     * 得到请求数量
     * @return
     */
    public int getRequestCount() {
        return uuidRequestMap.size();
    }

    /**
     * 得到公告
     * @return
     */
    public List<String> getAnnouncements() {
        return announcements;
    }

    public YamlConfiguration getYml() {
        return yml;
    }

    public void save() {
        YamlUtil.saveYaml(yml, file);
    }

    public int getRank() {
        return 0;
    }

    public void broadcastMessage(String message) {
        for (GuildMember member : getMembers()) {
            if (member.isOnline()) {
                JulyMessage.sendColoredMessage(member.getBukkitPlayer(), message);
            }
        }
    }
}
