package org.vfast.backrooms.world.generation;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class CaptainRuthieStructure extends Structure {

    public static final MapCodec<CaptainRuthieStructure> CODEC = simpleCodec(CaptainRuthieStructure::new);

    public CaptainRuthieStructure(final StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(final GenerationContext context) {
        return onTopOfChunkCenter(context, Types.WORLD_SURFACE_WG, builder -> this.generatePieces(builder, context));
    }

    private void generatePieces(final StructurePiecesBuilder builder, final GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        WorldgenRandom random = context.random();
        // Y = 90 is a placeholder; postProcess in CaptainRuthiePieces will snap
        // the piece to the actual surface and then apply the 8-block burial offset.
        BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(random);
        CaptainRuthiePieces.addPieces(context.structureTemplateManager(), startPos, rotation, builder);
    }

    @Override
    public StructureType<CaptainRuthieStructure> type() {
        return BackroomsPieceTypes.CAPTAIN_RUTHIE;
    }
}