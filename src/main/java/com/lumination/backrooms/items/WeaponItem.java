package com.lumination.backrooms.items;

import com.lumination.backrooms.utils.CustomMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tag.BlockTags;

class BackroomsTypeItem {
    public static class Weapon extends SwordItem {
        public Weapon(float attackDamage, float attackSpeed, int durability) {
            super(new CustomMaterial(durability, attackDamage - 1.0f), 0, attackSpeed - 4.0f, new FabricItemSettings().group(BackroomsItemsGroup.Weapons).maxCount(1));
        }
    }

    public static class WeaponAxe extends AxeItem {
        public WeaponAxe(float attackDamage, float attackSpeed, int durability) {
            super(new CustomMaterial(durability, attackDamage - 1.0f), 0, attackSpeed - 4.0f, new FabricItemSettings().group(BackroomsItemsGroup.Weapons).maxCount(1));
        }
    }
}
