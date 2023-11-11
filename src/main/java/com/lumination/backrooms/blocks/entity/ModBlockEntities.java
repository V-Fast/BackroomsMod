package com.lumination.backrooms.blocks.entity;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class ModBlockEntities {
    public static BlockEntityType<TapePlayerEntity> tapePlayer;
    public static BlockEntityType<RadioEntity> radio;

    public static void registerBlockEntities() {
        ModBlockEntities.tapePlayer = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(BackroomsMod.MOD_ID, "tape_player"),
                QuiltBlockEntityTypeBuilder.create(TapePlayerEntity::new,
                        ModBlocks.TAPE_PLAYER).build(null));
        ModBlockEntities.radio = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(BackroomsMod.MOD_ID, "radio"),
                QuiltBlockEntityTypeBuilder.create(RadioEntity::new,
                        ModBlocks.RADIO).build(null));

        BackroomsMod.print("Registered ModBlockEntities");
    }
}
