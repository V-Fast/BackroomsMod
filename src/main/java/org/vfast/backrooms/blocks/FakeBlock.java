package org.vfast.backrooms.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.interfaces.LevelPortal;
import org.vfast.backrooms.world.BackroomsLevels;

import java.util.Set;

public class FakeBlock extends Block implements LevelPortal {
    public static final MapCodec<FakeBlock> CODEC = simpleCodec(FakeBlock::new);

    public static final EnumProperty<FakeBlock.Mimic> MIMIC = EnumProperty.create("mimic_block", FakeBlock.Mimic.class);

    public FakeBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(MIMIC, Mimic.GRASS));
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        LevelPortal.super.entityInside(state, level, pos, entity, EntityBounding.HEAD);

        RandomSource random = level.getRandom();
        entity.makeStuckInBlock(state, new Vec3(0.9F, 1.5, 0.9F));
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
        level.addAlwaysVisibleParticle(
                particle,
                entity.getX() + Mth.randomBetween(random, -1.0F, 1.0F) * 0.3f,
                pos.getY() + 1,
                entity.getZ()  + Mth.randomBetween(random, -1.0F, 1.0F) * 0.3f,
                0.0f,
                0.0f,
                0.0f
        );
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        assert entity.isAlive();
        this.prepareEntity(entity, true);

        ResourceKey<Level> dimension = BackroomsLevels.LEVEL_0;
        ServerLevel newLevel = currentLevel.getServer().getLevel(dimension);

        if (newLevel != null && newLevel != currentLevel) {
            LevelPortal.SpawnLocation spawnLoc = this.selectStartPosition(portalEntryPos, newLevel, null);

            if (entity instanceof ServerPlayer) {
                ServerPlayer.RespawnConfig respawn = LevelPortal.getSpawnConfig(spawnLoc.position(), spawnLoc.yRot(), spawnLoc.xRot(), dimension);
                ((ServerPlayer) entity).setRespawnPosition(respawn, false);
            }

            this.prepareEntity(entity, false);
            return new TeleportTransition(newLevel, spawnLoc.position().getBottomCenter(), Vec3.ZERO, spawnLoc.yRot(), spawnLoc.xRot(), Set.of(), LevelPortal::affectPlayer);
        } else {
            this.prepareEntity(entity, false);
            return null;
        }
    }

    private void prepareEntity(Entity entity, boolean ongoing) {
        if (entity instanceof LivingEntity) {
            LevelPortal.prepareEntity((LivingEntity) entity, ongoing);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(MIMIC, Mimic.GRASS);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MIMIC);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected SoundType getSoundType(BlockState state) {
        return state.getValue(MIMIC).getSoundType();
    }

    @Override
    public Block invalidBlock() {
        return BackroomsBlocks.MOIST_SILK;
    }

    @Override
    public BlockState suffocatingBlock(BlockState currentState) {
        return currentState.getValue(MIMIC).getBlock().defaultBlockState();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }

    enum Mimic implements StringRepresentable {
        GRASS,
        SAND;

        @Override
        public String getSerializedName() {
            return switch (this) {
                case GRASS -> "grass_block";
                case SAND -> "sand";
            };
        }

        public SoundType getSoundType() {
            return switch (this) {
                case GRASS -> SoundType.GRASS;
                case SAND -> SoundType.SAND;
            };
        }

        public Block getBlock() {
            return switch (this) {
                case GRASS -> Blocks.GRASS_BLOCK;
                case SAND -> Blocks.SAND;
            };
        }
    }
}
