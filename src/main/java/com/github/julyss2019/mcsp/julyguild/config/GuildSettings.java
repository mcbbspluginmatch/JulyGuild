package com.github.julyss2019.mcsp.julyguild.config;

import com.github.julyss2019.mcsp.julylibrary.config.Config;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuildSettings {
    @Config(path = "guild.create.name_regex")
    private String guildCreateNameRegex;
    @Config(path = "guild.create.name_not_valid_msg")
    private String guildCreateNameNotValidMsg;
    @Config(path = "guild.create.cost.money.amount")
    private int guildCreateCostMoneyAmount;
    @Config(path = "guild.create.cost.money.enabled")
    private boolean guildCreateCostMoneyEnabled;
    @Config(path = "guild.create.cost.points.amount")
    private int guildCreateCostPointsAmount;
    @Config(path = "guild.create.cost.points.enabled")
    private boolean guildCreateCostPointsEnabled;
    @Config(path = "guild.create.cost.item.key_lore")
    private String guildCreateCostItemKeyLore;
    @Config(path = "guild.create.cost.item.enabled")
    private boolean guildCreateCostItemEnabled;
    @Config(path = "guild.create.colored_no_per_msg")
    private String guildCreateColoredNoPerMsg;
    @Config(path = "guild.create.cost.item.amount")
    private int guildCreateCostItemAmount;
    @Config(path = "guild.announcement.split_char")
    private String guildAnnouncementSplitChar;
    @Config(path = "guild.announcement.max_count")
    private int guildAnnouncementMaxCount;
    @Config(path = "guild.def_max_member_count")
    private int guildDefMaxMemberCount;
    @Config(path = "guild.request.join.timeout")
    private int guildRequestJoinTimeout;

    //@Config(path = "guild.request.invite.timeout")
    private int guildRequestInviteTimeout;

    @Config(path = "guild.def_max_admin_count")
    private int guildDefMaxAdminCount;

    @Config(path = "guild.donate_min_money")
    private int donateMinMoney;

    @Config(path = "guild.def_icon.material")
    private Material defIconMaterial;

    @Config(path = "guild.def_icon.durability")
    private short defIconDurability;

    @Config(path = "guild.promote.money_formula")
    private String promoteMoneyFormula;

    @Config(path = "guild.promote.money_max_member_count")
    private int promoteMoneyMaxMemberCount;

    @Config(path = "guild.promote.point_formula")
    private String promotePointFormula;

    @Config(path = "guild.promote.point_max_member_count")
    private int promotePointMaxMemberCount;

    @Config(path = "guild.tp_all.interval")
    private int tpAllInterval;

    @Config(path = "guild.tp_all.shift_count_interval")
    private int tpAllShiftCountInterval;

    @Config(path = "guild.tp_all.shift_count")
    private int tpAllShiftCount;

    @Config(path = "guild.tp_all.timeout")
    private int tpAllShiftTimeout;

    public int getTpAllShiftTimeout() {
        return tpAllShiftTimeout;
    }

    public int getTpAllInterval() {
        return tpAllInterval;
    }

    public int getTpAllShiftCountInterval() {
        return tpAllShiftCountInterval;
    }

    public int getTpAllShiftCount() {
        return tpAllShiftCount;
    }

    private List<ConfigGuildIcon> configGuildIcons = new ArrayList<>();

    public List<ConfigGuildIcon> getConfigGuildIcons() {
        return configGuildIcons;
    }

    public String getPromoteMoneyFormula() {
        return promoteMoneyFormula;
    }

    public int getPromoteMoneyMaxMemberCount() {
        return promoteMoneyMaxMemberCount;
    }

    public String getPromotePointFormula() {
        return promotePointFormula;
    }

    public int getPromotePointMaxMemberCount() {
        return promotePointMaxMemberCount;
    }

    public Material getDefIconMaterial() {
        return defIconMaterial;
    }

    public short getDefIconDurability() {
        return defIconDurability;
    }

    public ItemStack getDefIconItem() {
        return new ItemBuilder().material(defIconMaterial).durability(defIconDurability).build();
    }

    public int getDonateMinMoney() {
        return donateMinMoney;
    }

    public String getGuildCreateNameRegex() {
        return guildCreateNameRegex;
    }

    public String getGuildCreateNameNotValidMsg() {
        return guildCreateNameNotValidMsg;
    }

    public int getGuildCreateCostMoneyAmount() {
        return guildCreateCostMoneyAmount;
    }

    public boolean isGuildCreateCostMoneyEnabled() {
        return guildCreateCostMoneyEnabled;
    }

    public int getGuildCreateCostPointsAmount() {
        return guildCreateCostPointsAmount;
    }

    public boolean isGuildCreateCostPointsEnabled() {
        return guildCreateCostPointsEnabled;
    }

    public String getGuildCreateCostItemKeyLore() {
        return guildCreateCostItemKeyLore;
    }

    public boolean isGuildCreateCostItemEnabled() {
        return guildCreateCostItemEnabled;
    }

    public String getGuildCreateColoredNoPerMsg() {
        return guildCreateColoredNoPerMsg;
    }

    public int getGuildCreateCostItemAmount() {
        return guildCreateCostItemAmount;
    }

    public String getGuildAnnouncementSplitChar() {
        return guildAnnouncementSplitChar;
    }

    public int getGuildAnnouncementMaxCount() {
        return guildAnnouncementMaxCount;
    }

    public int getGuildDefMaxMemberCount() {
        return guildDefMaxMemberCount;
    }

    public int getGuildRequestJoinTimeout() {
        return guildRequestJoinTimeout;
    }

    public int getGuildRequestInviteTimeout() {
        return guildRequestInviteTimeout;
    }

    public int getGuildDefMaxAdminCount() {
        return guildDefMaxAdminCount;
    }
}
