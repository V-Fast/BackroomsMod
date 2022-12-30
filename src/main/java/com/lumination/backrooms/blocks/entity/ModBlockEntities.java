package com.lumination.backrooms.blocks.entity;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<TapePlayerEntity> TAPE_PLAYER;
    public static BlockEntityType<RadioEntity> RADIO;

    public static void registerBlockEntities() {
        TAPE_PLAYER = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(BackroomsMod.MOD_ID, "tape_player"),
                FabricBlockEntityTypeBuilder.create(TapePlayerEntity::new,
                        ModBlocks.TAPE_PLAYER).build(null));
        RADIO = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(BackroomsMod.MOD_ID, "radio"),
                FabricBlockEntityTypeBuilder.create(RadioEntity::new,
                        ModBlocks.RADIO).build(null));

        BackroomsMod.print("Registered ModBlockEntites");
    }
}
