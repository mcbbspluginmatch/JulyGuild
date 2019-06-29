package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class GuildMember {
    private Guild guild;
    private OfflineGuildPlayer offlineGuildPlayer;
    private long joinTime;
    private int donatedBalance; // 赞助的金币
    private String name;
    private ConfigurationSection memberSection;

    public GuildMember(Guild guild, OfflineGuildPlayer player) {
        this.guild = guild;
        this.offlineGuildPlayer = player;
        this.name = player.getName();
        this.memberSection = guild.getYml().getConfigurationSection("members." + player.getName());

        load();
    }

    public GuildMember(Guild guild, OfflineGuildPlayer player, ConfigurationSection memberSection) {
        this.guild = guild;
        this.offlineGuildPlayer = player;
        this.name = player.getName();
        this.memberSection = memberSection;

        load();
    }

    public void load() {
        if (memberSection != null) {
            this.joinTime = memberSection.getLong("join_time");
            this.donatedBalance = memberSection.getInt("donated_balance");
        }
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

    public OfflineGuildPlayer getOfflineGuildPlayer() {
        return offlineGuildPlayer;
    }

    public String getName() {
        return name;
    }

    public void donateBalance(int amount) {
        setDonatedBalance(getDonatedBalance() + amount);
    }

    public void setDonatedBalance(int amount) {
        memberSection.set("donated_balance", amount);
        guild.save();
        this.donatedBalance = amount;
    }

    public int getDonatedBalance() {
        return donatedBalance;
    }

    public boolean isOnline() {
        return getOfflineGuildPlayer().isOnline();
    }

    public Permission getPermission() {
        return Permission.MEMBER;
    }
}
