package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import org.bukkit.configuration.ConfigurationSection;

public class GuildMember {
    private Guild guild;
    private OfflineGuildPlayer offlineGuildPlayer;
    private long joinTime;
    private int donateMoney; // 赞助的金币
    private ConfigurationSection memberSection;

    public GuildMember(Guild guild, OfflineGuildPlayer player) {
        this.guild = guild;
        this.offlineGuildPlayer = player;
        this.memberSection = guild.getYml().getConfigurationSection("members." + player.getName());

        load();
    }

    public void load() {
        if (memberSection != null) {
            this.joinTime = memberSection.getLong("join_time");
            this.donateMoney = memberSection.getInt("donate_money");
        }
    }

    public long getJoinTime() {
        return joinTime;
    }

    public Guild getGuild() {
        return guild;
    }

    public OfflineGuildPlayer getOfflineGuildPlayer() {
        return offlineGuildPlayer;
    }

    public String getName() {
        return getOfflineGuildPlayer().getName();
    }

    public void addDonateMoney(int amount) {
        setDonateMoney(getDonateMoney() + amount);
    }

    public void setDonateMoney(int amount) {
        memberSection.set("donate_money", amount);
        guild.save();
        this.donateMoney = amount;
    }

    public int getDonateMoney() {
        return donateMoney;
    }

    public Permission getPermission() {
        return Permission.MEMBER;
    }
}
