package com.jetug.power_armor_mod.common.entity.capability.data;

public class PowerArmorPartData implements IPowerArmorPartData{
    public static final String DURABILITY = "durability";
    public static final String DEFENSE = "defense";

    private double durability;
    private double defense;

    @Override
    public double getDurability() {
        return durability;
    }

    @Override
    public void setDurability(double value) {
        durability = value;
    }

    @Override
    public double getDefense() {
        return defense;
    }

    @Override
    public void setDefense(double value) {
        defense = value;
    }

    @Override
    public void copyFrom(IPowerArmorPartData source) {
        durability = source.getDurability();
        defense = source.getDefense();
    }
}
