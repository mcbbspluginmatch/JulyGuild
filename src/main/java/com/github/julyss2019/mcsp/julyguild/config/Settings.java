package com.github.julyss2019.mcsp.julyguild.config;

import com.github.julyss2019.mcsp.julylibrary.config.Config;

import java.util.List;

public class Settings {
    @Config(path = "player_info_item_lores")
    private List<String> playerInfoItemLores;
    @Config(path = "create_guild.name_regex")
    private String createGuildNameRegex;
    @Config(path = "create_guild.name_not_valid_msg")
    private String createGuildNameNotValidMsg;
    @Config(path = "create_guild.cost.money.amount")
    private int createGuildCostMoneyAmount;
    @Config(path = "create_guild.cost.money.enabled")
    private int createGuildCostMoneyEnabled;
    @Config(path = "create_guild.cost.points.amount")
    private int createGuildCostPointsAmount;
    @Config(path = "create_guild.cost.points.enabled")
    private int createGuildCostPointsEnabled;
    @Config(path = "create_guild.cost.item.key_lore")
    private int createGuildCostItemKeyLore;
    @Config(path = "create_guild.cost.item.enabled")
    private int createGuildCostItemEnabled;

    public List<String> getPlayerInfoItemLores() {
        return playerInfoItemLores;
    }

    public String getCreateGuildNameRegex() {
        return createGuildNameRegex;
    }

    public String getCreateGuildNameNotValidMsg() {
        return createGuildNameNotValidMsg;
    }

    public int getCreateGuildCostMoneyAmount() {
        return createGuildCostMoneyAmount;
    }

    public int getCreateGuildCostMoneyEnabled() {
        return createGuildCostMoneyEnabled;
    }

    public int getCreateGuildCostPointsAmount() {
        return createGuildCostPointsAmount;
    }

    public int getCreateGuildCostPointsEnabled() {
        return createGuildCostPointsEnabled;
    }

    public int getCreateGuildCostItemKeyLore() {
        return createGuildCostItemKeyLore;
    }

    public int getCreateGuildCostItemEnabled() {
        return createGuildCostItemEnabled;
    }
}
