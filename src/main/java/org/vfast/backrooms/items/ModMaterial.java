package org.vfast.backrooms.items;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ModMaterial implements ToolMaterial {
    private int durability = 0;
    private float attackDamage = 0;
    private int miningLevel = 0;
    private int enchantability = 0;
    private Ingredient repairIngredient = null;

    // any
    public ModMaterial(int durability, float attackDamage, int miningLevel, int enchantability, Ingredient repairIngredient) {
        this.durability = durability;
        this.attackDamage = attackDamage;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    // sword type
    public ModMaterial(int durability, float attackDamage) {
        this.durability = durability;
        this.attackDamage = attackDamage;
        this.enchantability = 15;
    }

    // tool type
    public ModMaterial(int durability, int miningLevel, int enchantability) {
        this.durability = durability;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.attackDamage = 2.0F;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 0;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }
}