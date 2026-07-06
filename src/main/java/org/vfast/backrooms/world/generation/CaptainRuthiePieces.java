package org.vfast.backrooms.world.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;

public class CaptainRuthiePieces {

    /**
     * How many blocks below the surface the structure top should sit.
     * 8 means the top of the template is buried 9 blocks underground.
     */
    private static final int BURIAL_DEPTH = 9;

    /**
     * The Y level used as the initial placement anchor before surface-snapping.
     * Matches the value chosen in CaptainRuthieStructure#generatePieces.
     */
    private static final int GENERATION_HEIGHT = 90;

    /** NBT structure template location: assets/backrooms/structures/overworld/captain_ruthie.nbt */
    private static final Identifier STRUCTURE_LOCATION =
            Identifier.fromNamespaceAndPath("backrooms", "overworld/captain_ruthie");

    /**
     * The rotation pivot of your template. Adjust X/Y/Z to match the centre of
     * your .nbt file (same role as IglooPieces.PIVOTS). A common default is the
     * centre of the structure's bounding box; set to (0, 0, 0) if unsure and
     * tweak after testing in-game.
     */
    private static final BlockPos PIVOT = new BlockPos(0, 0, 0);

    // -------------------------------------------------------------------------

    public static void addPieces(final StructureTemplateManager structureTemplateManager, final BlockPos position, final Rotation rotation, final StructurePieceAccessor structurePieceAccessor) {
        structurePieceAccessor.addPiece(
                new CaptainRuthiePieces.CaptainRuthiePiece(structureTemplateManager, position, rotation)
        );
    }

    // -------------------------------------------------------------------------

    public static class CaptainRuthiePiece extends TemplateStructurePiece {

        public CaptainRuthiePiece(final StructureTemplateManager structureTemplateManager, final BlockPos position, final Rotation rotation) {
            super(BackroomsPieceTypes.CAPTAIN_RUTHIE_PIECE, 0, structureTemplateManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(rotation), position);
        }

        /** Deserialization constructor — called when loading a saved chunk. */
        public CaptainRuthiePiece(final StructureTemplateManager structureTemplateManager, final CompoundTag tag) {
            super(BackroomsPieceTypes.CAPTAIN_RUTHIE_PIECE, tag, structureTemplateManager, _ -> makeSettings(tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
        }

        // -- Helpers ----------------------------------------------------------

        private static StructurePlaceSettings makeSettings(final Rotation rotation) {
            return new StructurePlaceSettings()
                    .setRotation(rotation)
                    .setMirror(Mirror.NONE)
                    .setRotationPivot(PIVOT)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
                    .setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);
        }

        // -- Serialization ----------------------------------------------------

        @Override
        protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
        }

        // -- Data markers (add chest / spawner logic here if needed) ----------

        @Override
        protected void handleDataMarker(final String markerId, final BlockPos position, final ServerLevelAccessor level, final RandomSource random, final BoundingBox chunkBB) {
            // Add any data-marker handling here (e.g. loot chests, mob spawners).
            // Leave empty if your template has no data markers.
        }

        // -- Placement --------------------------------------------------------

        @Override
        public void postProcess(final WorldGenLevel level, final StructureManager structureManager, final ChunkGenerator generator, final RandomSource random, final BoundingBox chunkBB, final ChunkPos chunkPos, final BlockPos referencePos) {
            // Snap to the actual world surface at the structure's XZ position,
            // then bury the top of the template BURIAL_DEPTH blocks underground.
            int surfaceY = level.getHeight(Types.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ());

            // Store the original template position so we can restore it after
            // placement (same pattern as IglooPieces).
            BlockPos savedPosition = this.templatePosition;

            // Offset from the placeholder Y (GENERATION_HEIGHT) to
            // (surfaceY - BURIAL_DEPTH), so the top of the template sits
            // BURIAL_DEPTH blocks below the ground surface.
            this.templatePosition = this.templatePosition.offset(0, surfaceY - GENERATION_HEIGHT - BURIAL_DEPTH, 0);

            super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);

            // Restore original position so saved NBT remains consistent.
            this.templatePosition = savedPosition;
        }
    }
}