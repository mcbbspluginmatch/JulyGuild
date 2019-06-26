package com.github.julyss2019.mcsp.julyguild.config;

import com.github.julyss2019.mcsp.julylibrary.config.Config;

import java.util.List;

public class Settings {
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
    @Config(path = "gui.main_gui.guild.display_name")
    private String guiMainGuiGuildDisplayName;
    @Config(path = "gui.main_gui.guild.lores")
    private List<String> guiMainGuiGuildLores;
    @Config(path = "gui.main_gui.player_info_lores")
    private List<String> guiMainGuiGuildPlayerInfoLores;
    @Config(path = "guild.create.cost.item.amount")
    private int guildCreateCostItemAmount;
    @Config(path = "guild.announcement.split_char")
    private String guildAnnouncementSplitChar;
    @Config(path = "guild.announcement.max_count")
    private int guildAnnountcementMaxCount;
    @Config(path = "guild.def_max_member_count")
    private int guildDefMaxMemberCount;

    public int getGuildDefMaxMemberCount() {
        return guildDefMaxMemberCount;
    }

    public String getGuildAnnouncementSplitChar() {
        return guildAnnouncementSplitChar;
    }

    public int getGuildAnnountcementMaxCount() {
        return guildAnnountcementMaxCount;
    }

    public String getGuiMainGuiGuildDisplayName() {
        return guiMainGuiGuildDisplayName;
    }

    public List<String> getGuiMainGuiGuildLores() {
        return guiMainGuiGuildLores;
    }

    public List<String> getGuiMainGuiGuildPlayerInfoLores() {
        return guiMainGuiGuildPlayerInfoLores;
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
}
