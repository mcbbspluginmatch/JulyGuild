package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.log.guild.GuildBalanceChangedLog;
import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class GuildBank {
    private static JulyGuild plugin = JulyGuild.getInstance();
    public enum Type {POINTS, MONEY}

    private Guild guild;
    private double money;
    private double points;
    private ConfigurationSection section;

    public GuildBank(Guild guild) {
        this.guild = guild;

        if (!guild.getYml().contains("bank")) {
            guild.getYml().createSection("bank");
        }

        this.section = guild.getYml().getConfigurationSection("bank");
        load();
    }

    public void load() {
        this.money = section.getInt("money");
        this.points = section.getInt("points");
    }

    public boolean has(@NotNull GuildBank.Type type, double amount) {
        return type == Type.MONEY ? money >= amount : points >= amount;
    }

    public void deposit(@NotNull GuildBank.Type type, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }

        if (type == Type.POINTS) {
            setPoints(getPoints() + amount);
        } else {
            setMoney(getMoney() + amount);
        }
    }

    public void withdraw(@NotNull GuildBank.Type type, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }

        if (type == Type.POINTS) {
            if (getPoints() < amount) {
                throw new IllegalArgumentException("点券不足");
            }

            setPoints(getPoints() - amount);
        } else {
            if (getMoney() < amount) {
                throw new IllegalArgumentException("金币不足");
            }

            setMoney(getMoney() - amount);
        }
    }

    public void setMoney(double money) {
        double oldMoney = this.money;

        section.set("money", money);
        guild.save();
        this.money = money;
        plugin.writeGuildLog(FileLogger.LoggerLevel.INFO, new GuildBalanceChangedLog(guild.getUUID().toString(), oldMoney, money));
    }

    public void setPoints(double points) {
        section.set("points", money);
        guild.save();
        this.points = points;
    }

    public double getMoney() {
        return money;
    }

    public double getPoints() {
        return points;
    }
}
