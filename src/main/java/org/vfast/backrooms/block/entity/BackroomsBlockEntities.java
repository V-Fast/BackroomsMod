package org.vfast.backrooms.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.block.BackroomsBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BackroomsBlockEntities {
    public static BlockEntityType<TapePlayerEntity> tapePlayer;
    public static BlockEntityType<RadioEntity> radio;

    public static void registerBlockEntities() {
        BackroomsBlockEntities.tapePlayer = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(BackroomsMod.ID, "tape_player"),
                FabricBlockEntityTypeBuilder.create(TapePlayerEntity::new,
                        BackroomsBlocks.TAPE_PLAYER).build(null));
        BackroomsBlockEntities.radio = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(BackroomsMod.ID, "radio"),
                FabricBlockEntityTypeBuilder.create(RadioEntity::new,
                        BackroomsBlocks.RADIO).build(null));
    }
}
