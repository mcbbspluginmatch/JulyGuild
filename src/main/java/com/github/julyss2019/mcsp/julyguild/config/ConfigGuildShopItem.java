package com.github.julyss2019.mcsp.julyguild.config;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.Material;

import java.util.List;

public class ConfigGuildShopItem {
    private static JulyGuild plugin = JulyGuild.getInstance();
    public enum Target {ONLINE_MEMBERS, ALL_MEMBERS}

    private String name;
    private int index;
    private org.bukkit.Material material;
    private short durability;
    private List<String> lores;
    private Target target;
    private boolean moneyEnabled;
    private String moneyFormula;
    private boolean pointsEnabled;
    private String pointsFormula;
    private String message;
    private List<String> rewardCommands;
    private String displayName;


    public ItemBuilder getItemBuilder() {
        return new ItemBuilder().material(material).durability(durability).displayName(displayName).lores(lores).colored();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public void setDurability(short durability) {
        this.durability = durability;
    }

    public void setLores(List<String> lores) {
        this.lores = lores;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void setMoneyEnabled(boolean moneyEnabled) {
        this.moneyEnabled = moneyEnabled;
    }

    public void setMoneyFormula(String moneyFormula) {
        this.moneyFormula = moneyFormula;
    }

    public void setPointsEnabled(boolean pointsEnabled) {
        this.pointsEnabled = pointsEnabled;
    }

    public void setPointsFormula(String pointsFormula) {
        this.pointsFormula = pointsFormula;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRewardCommands(List<String> rewardCommands) {
        this.rewardCommands = rewardCommands;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public short getDurability() {
        return durability;
    }

    public List<String> getLores() {
        return lores;
    }

    public Target getTarget() {
        return target;
    }

    public boolean isMoneyEnabled() {
        return moneyEnabled;
    }

    public String getMoneyFormula() {
        return moneyFormula;
    }

    public boolean isPointsEnabled() {
        return pointsEnabled && plugin.isPlayerPointsHooked();
    }

    public String getPointsFormula() {
        return pointsFormula;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getRewardCommands() {
        return rewardCommands;
    }
}
