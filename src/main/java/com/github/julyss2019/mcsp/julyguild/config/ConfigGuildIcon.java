package com.github.julyss2019.mcsp.julyguild.config;


import org.bukkit.Material;

import java.util.List;

public class ConfigGuildIcon {
    public enum CostType {
        POINTS("点券"), MONEY("金币");

        String chineseName;

        CostType(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getChineseName() {
            return chineseName;
        }
    }

    private org.bukkit.Material material;
    private short durability;
    private String displayName;
    private List<String> lores;
    private CostType costType;
    private int fee;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public short getDurability() {
        return durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getLores() {
        return lores;
    }

    public void setLores(List<String> lores) {
        this.lores = lores;
    }

    public CostType getCostType() {
        return costType;
    }

    public void setCostType(CostType costType) {
        this.costType = costType;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
