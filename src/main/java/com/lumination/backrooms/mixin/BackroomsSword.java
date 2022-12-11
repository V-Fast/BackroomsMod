package com.lumination.backrooms.mixin;

import com.lumaa.libu.items.LibuMaterial;
import com.lumaa.libu.items.LibuWeapon;
import com.lumination.backrooms.items.BackroomsItemsGroup;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LibuWeapon.LibuSword.class)
public class BackroomsSword extends SwordItem {
    public BackroomsSword(float attackDamage, float attackSpeed, int durability) {
        super(new LibuMaterial(durability, attackDamage - 1.0F), 0, attackSpeed - 4.0F, new Settings().maxCount(1).group(BackroomsItemsGroup.Weapons));
    }
}
