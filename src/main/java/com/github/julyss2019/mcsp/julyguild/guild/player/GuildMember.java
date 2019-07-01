package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class GuildMember {
    private Guild guild;
    private GuildPlayer guildPlayer;
    private long joinTime;
    private double donatedMoney; // 赞助的金币
    private double donatedPoints;
    private String name;
    private ConfigurationSection memberSection;

    public GuildMember(Guild guild, GuildPlayer player) {
        this.guild = guild;
        this.guildPlayer = player;
        this.name = player.getName();
        this.memberSection = guild.getYml().getConfigurationSection("members." + player.getName());

        load();
    }

    public GuildMember(Guild guild, GuildPlayer player, ConfigurationSection memberSection) {
        this.guild = guild;
        this.guildPlayer = player;
        this.name = player.getName();
        this.memberSection = memberSection;

        load();
    }

    public void load() {
        if (memberSection != null) {
            this.joinTime = memberSection.getLong("join_time");
            this.donatedMoney = memberSection.getDouble("donated_money");
            this.donatedPoints = memberSection.getDouble("donated_points");
        }
    }

    /**
     * 添加已赞助的金币
     * @param amount
     */
    public void addDonatedMoney(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }

        setDonatedMoney(getDonatedMoney() + amount);
    }

    /**
     * 添加已赞助的点券
     * @param amount
     */
    public void addDonatedPoints(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }

        setDonatedPoints(getDonatedPoints() + amount);
    }

    public double getDonatedMoney() {
        return donatedMoney;
    }

    public double getDonatedPoints() {
        return donatedPoints;
    }

    public void setDonatedMoney(double donatedMoney) {
        memberSection.set("donated_money", donatedMoney);
        save();
        this.donatedMoney = donatedMoney;
    }

    public void setDonatedPoints(double donatedPoints) {
        memberSection.set("donated_points", donatedPoints);
        save();
        this.donatedPoints = donatedPoints;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(getName());
    }

    public long getJoinTime() {
        return joinTime;
    }

    public Guild getGuild() {
        return guild;
    }

    public GuildPlayer getGuildPlayer() {
        return guildPlayer;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return getGuildPlayer().isOnline();
    }

    public Permission getPermission() {
        return Permission.MEMBER;
    }

    public void save() {
        guild.save();
    }
}
