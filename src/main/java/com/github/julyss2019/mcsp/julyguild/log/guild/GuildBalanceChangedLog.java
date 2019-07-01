package com.github.julyss2019.mcsp.julyguild.log.guild;

import com.github.julyss2019.mcsp.julyguild.log.BaseGuildLog;
import com.github.julyss2019.mcsp.julyguild.log.GuildLogType;

public class GuildBalanceChangedLog extends BaseGuildLog {
    private Double oldBalance;
    private Double newBalance;

    public GuildBalanceChangedLog(Double oldBalance, Double newBalance) {
        super(GuildLogType.BALANCE_CHANGED);

        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
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
