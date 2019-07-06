package com.github.julyss2019.mcsp.julyguild.log.guild;

import com.github.julyss2019.mcsp.julyguild.guild.GuildBank;
import com.github.julyss2019.mcsp.julyguild.log.BaseGuildLog;
import com.github.julyss2019.mcsp.julyguild.log.GuildLogType;

public class GuildBalanceChangedLog extends BaseGuildLog {
    private GuildBank.BalanceType balanceType;
    private Double oldBalance;
    private Double newBalance;

    public GuildBalanceChangedLog(String guildUUID, GuildBank.BalanceType balanceType, Double oldBalance, Double newBalance) {
        super(GuildLogType.BALANCE_CHANGED, guildUUID);

        this.balanceType = balanceType;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
    }


    public GuildBank.BalanceType getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(GuildBank.BalanceType balanceType) {
        this.balanceType = balanceType;
    }

    public Double getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(Double oldBalance) {
        this.oldBalance = oldBalance;
    }

    public Double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Double newBalance) {
        this.newBalance = newBalance;
    }
}
