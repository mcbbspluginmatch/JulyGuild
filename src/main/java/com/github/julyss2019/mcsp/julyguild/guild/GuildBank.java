package com.github.julyss2019.mcsp.julyguild.guild;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.log.guild.GuildBalanceChangedLog;
import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class GuildBank {
    private static JulyGuild plugin = JulyGuild.getInstance();
    public enum BalanceType {POINTS, MONEY}

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
    }

    public GuildBank load() {
        this.money = section.getInt("money");
        this.points = section.getInt("points");
        return this;
    }

    public boolean has(@NotNull GuildBank.BalanceType balanceType, double amount) {
        if (balanceType == BalanceType.MONEY) {
            return money >= amount;
        }

        if (balanceType == BalanceType.POINTS) {
            return points >= amount;
        }

        throw new IllegalArgumentException("非法的类型");
    }

    public void deposit(@NotNull GuildBank.BalanceType balanceType, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }

        setBalance(balanceType, getBalance(balanceType) + amount);
    }

    public void withdraw(@NotNull GuildBank.BalanceType balanceType, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }

        if (!has(balanceType, amount)) {
            throw new IllegalArgumentException("余额不足");
        }


        setBalance(balanceType, getBalance(balanceType) - amount);
    }

    public void setBalance(@NotNull GuildBank.BalanceType balanceType, double amount) {
        if (balanceType == BalanceType.MONEY) {
            double oldMoney = this.money;

            section.set("money", amount);
            guild.save();
            this.money = amount;
            plugin.writeGuildLog(FileLogger.LoggerLevel.INFO, new GuildBalanceChangedLog(guild.getUUID().toString(), BalanceType.MONEY, oldMoney, this.money));
        } else if (balanceType == BalanceType.POINTS) {
            double oldPoints = this.points;

            section.set("points", amount);
            guild.save();
            this.points = amount;
            plugin.writeGuildLog(FileLogger.LoggerLevel.INFO, new GuildBalanceChangedLog(guild.getUUID().toString(), BalanceType.POINTS, oldPoints, this.points));
        }
    }

    public double getBalance(@NotNull GuildBank.BalanceType balanceType) {
        if (balanceType == BalanceType.MONEY) {
            return money;
        }

        if (balanceType == BalanceType.POINTS) {
            return points;
        }

        throw new IllegalArgumentException("非法的类型");
    }
}
