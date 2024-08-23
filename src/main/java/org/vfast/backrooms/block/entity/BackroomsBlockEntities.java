package org.vfast.backrooms.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.block.BackroomsBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BackroomsBlockEntities {
    public static BlockEntityType<TapePlayerEntity> TAPE_PLAYER_BLOCK_ENTITY;
//    public static BlockEntityType<RadioBlockEntity> RADIO_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        BackroomsBlockEntities.TAPE_PLAYER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(BackroomsMod.ID, "tape_player"),
                BlockEntityType.Builder.create(TapePlayerEntity::new,
                        BackroomsBlocks.TAPE_PLAYER).build());
//        BackroomsBlockEntities.RADIO_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
//                Identifier.of(BackroomsMod.ID, "radio"),
//                FabricBlockEntityTypeBuilder.create(RadioBlockEntity::new,
//                        BackroomsBlocks.RADIO).build());
    }
}
