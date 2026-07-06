package org.vfast.backrooms.world.generation;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsPieceTypes {
    public static final StructureType<CaptainRuthieStructure> CAPTAIN_RUTHIE = Registry.register(
            BuiltInRegistries.STRUCTURE_TYPE,
            Identifier.fromNamespaceAndPath("backrooms", "captain_ruthie"),
            () -> CaptainRuthieStructure.CODEC
    );

    public static final StructurePieceType.StructureTemplateType CAPTAIN_RUTHIE_PIECE = Registry.register(
            BuiltInRegistries.STRUCTURE_PIECE,
            Identifier.fromNamespaceAndPath("backrooms", "captain_ruthie"),
            CaptainRuthiePieces.CaptainRuthiePiece::new
    );

    public static void registerStructures() {
        BackroomsMod.LOGGER.info("[BackroomsMod] Structures initialized");
    }
}
