package org.vfast.backrooms.block.entity;

import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.block.BackroomsBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class BackroomsBlockEntities {
    public static BlockEntityType<TapePlayerEntity> tapePlayer;
    public static BlockEntityType<RadioEntity> radio;

    public static void registerBlockEntities() {
        BackroomsBlockEntities.tapePlayer = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(BackroomsMod.MOD_ID, "tape_player"),
                QuiltBlockEntityTypeBuilder.create(TapePlayerEntity::new,
                        BackroomsBlocks.TAPE_PLAYER).build(null));
        BackroomsBlockEntities.radio = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(BackroomsMod.MOD_ID, "radio"),
                QuiltBlockEntityTypeBuilder.create(RadioEntity::new,
                        BackroomsBlocks.RADIO).build(null));

        BackroomsMod.LOGGER.debug("Registered BlockEntities");
    }
}
