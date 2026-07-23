package org.vfast.backrooms.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.attachments.BackroomsAttachments;
import org.vfast.backrooms.attachments.PlayerSnapshot;
import org.vfast.backrooms.blocks.BackroomsPortalBlock;
import org.vfast.backrooms.sounds.BackroomsSounds;
import org.vfast.backrooms.world.BackroomsGameRules;
import org.vfast.backrooms.world.BackroomsLevels;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Use this when a block is used to **travel to a Backrooms level** */
public interface LevelPortal extends Portal {
    Map<LivingEntity, Float> transferEntitySpeed = new HashMap<>();

    default void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, EntityBounding entityBounding) {
        boolean suffocating = this.shouldSuffocate(entity, pos);
        if (suffocating && level.isClientSide()) {
            this.simulateSuffocation(this.suffocatingBlock(state));
        } else if (!suffocating && level.isClientSide()) {
            this.simulateSuffocation(null);
        }

        boolean isPlayerInside = (entity instanceof Player && this.fullyInBounding(entity, pos, entityBounding));
        boolean isEntityInside = (!(entity instanceof Player) && this.lazyBounding(entity, pos));

        if (entity.canUsePortal(false) && !entity.isOnPortalCooldown() && (isPlayerInside || isEntityInside)) {
            entity.setAsInsidePortal(this, pos);

            long seed = RandomSource.create().nextLong();
            if (!level.isClientSide()) {
                boolean isSmallEntity = entity instanceof ItemEntity || entity instanceof Projectile;
                if (!isSmallEntity) {
                    boolean isHostile = entity instanceof Monster;
                    SoundSource entitySource = isHostile ? SoundSource.HOSTILE : SoundSource.NEUTRAL;

                    level.playSeededSound(entity, entity.getX(), entity.getY(), entity.getZ(), BackroomsSounds.NOCLIP, isPlayerInside ? SoundSource.PLAYERS : entitySource, 1.0f, 1.0f, seed);
                } else {
                    level.playSeededSound(entity, entity.getX(), entity.getY(), entity.getZ(), BackroomsSounds.NOCLIP_SMALL, SoundSource.AMBIENT, 1.0f, 1.0f, seed);
                }
            }
        }
    }

    default void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        this.entityInside(state, level, pos, entity, EntityBounding.BOTH);
    }

    default void simulateSuffocation(@Nullable BlockState blockState) {
        Minecraft minecraft = Minecraft.getInstance();
        GameRenderer renderer = minecraft.gameRenderer;
        ScreenEffectRenderer screenRenderer = ((GameRendererGetter) renderer).getScreenEffectRenderer();
        ((Suffocator) screenRenderer).setSuffocating(blockState);
    }

    default boolean fullyInBounding(Entity entity, BlockPos blockPos, EntityBounding detectionBounding) {
        double x = blockPos.getX() + 0.5d;
        double y = blockPos.getY() + 0.5d;
        double z = blockPos.getZ() + 0.5d;
        Vec3 pos = new Vec3(x, y, z);
        Vec3 lb = pos.subtract(0.5d, 0.5d, 0.5d); // left bottom
        Vec3 ru = pos.add(0.5d, 0.5d, 0.5d); // right up

        AABB bounding = entity.getBoundingBox();
        Vec3 minFeetBounding = bounding.getMinPosition();
        Vec3 maxFeetBounding = bounding.getMaxPosition().subtract(0.0d, 1.0d, 0.0d);

        boolean minFeetThrough = this.passingThrough(minFeetBounding, lb, ru);
        boolean maxFeetThrough = this.passingThrough(maxFeetBounding, lb, ru);

        Vec3 minHeadBounding = bounding.getMinPosition().add(0.0d, 1.0d, 0.0d);
        Vec3 maxHeadBounding = bounding.getMaxPosition();

        boolean minHeadThrough = this.passingThrough(minHeadBounding, lb, ru);
        boolean maxHeadThrough = this.passingThrough(maxHeadBounding, lb, ru);

        if (detectionBounding == EntityBounding.FULL_BODY) {
            return (minFeetThrough && maxFeetThrough) && (minHeadThrough && maxHeadThrough);
        } else {
            boolean feetThrough = minFeetThrough && maxFeetThrough && detectionBounding.detectsFeet();
            boolean headThrough = minHeadThrough && maxHeadThrough && detectionBounding.detectsHead();

            return feetThrough || headThrough;
        }
    }

    default boolean fullyInBounding(Entity entity, BlockPos blockPos) {
        return this.fullyInBounding(entity, blockPos, EntityBounding.BOTH);
    }

    default boolean lazyBounding(Entity entity, BlockPos blockPos) {
        double x = blockPos.getX() + 0.5d;
        double y = blockPos.getY() + 0.5d;
        double z = blockPos.getZ() + 0.5d;
        Vec3 pos = new Vec3(x, y, z);
        Vec3 lb = pos.subtract(0.5d, 0.5d, 0.5d); // left bottom
        Vec3 ru = pos.add(0.5d, 0.5d, 0.5d); // right up

        AABB bounding = entity.getBoundingBox();
        Vec3 entityPos = bounding.getCenter();

        return this.touchingPosition(entityPos, lb, ru);
    }

    default boolean shouldSuffocate(Entity entity, BlockPos blockPos) {
        if (!(entity instanceof Player)) {
            return false;
        }

        double headDiameter = 0.155d;

        double x = blockPos.getX() + 0.5d;
        double y = blockPos.getY() + 0.5d;
        double z = blockPos.getZ() + 0.5d;
        Vec3 pos = new Vec3(x, y, z);
        Vec3 lb = pos.subtract(0.5d, 0.5d, 0.5d); // left bottom
        Vec3 ru = pos.add(0.5d, 0.5d, 0.5d); // right up

        Vec3 centerHead = entity.getEyePosition();
        return this.passingThrough(centerHead, lb, ru, headDiameter / 2);
    }

    default boolean passingThrough(Vec3 currentPos, Vec3 pos1, Vec3 pos2) {
        return this.passingThrough(currentPos, pos1, pos2, 0.0d);
    }

    default boolean passingThrough(Vec3 currentPos, Vec3 pos1, Vec3 pos2, double tolerance) {
        double minX = pos1.x - tolerance;
        double maxX = pos2.x + tolerance;
        double minY = pos1.y - tolerance;
        double maxY = pos2.y + tolerance;
        double minZ = pos1.z - tolerance;
        double maxZ = pos2.z + tolerance;

        return (currentPos.x >= minX && currentPos.x <= maxX) && (currentPos.y >= minY && currentPos.y <= maxY) && (currentPos.z >= minZ && currentPos.z <= maxZ);
    }

    default boolean touchingPosition(Vec3 currentPos, Vec3 pos1, Vec3 pos2) {
        double minX = pos1.x;
        double maxX = pos2.x;
        double minY = pos1.y;
        double maxY = pos2.y;
        double minZ = pos1.z;
        double maxZ = pos2.z;

        return (currentPos.x >= minX && currentPos.x <= maxX) || (currentPos.y >= minY && currentPos.y <= maxY) || (currentPos.z >= minZ && currentPos.z <= maxZ);
    }

    static void prepareEntity(LivingEntity entity, boolean ongoing) {
        assert entity.isAlive();

        if (ongoing) {
            float currentSpeed = entity.getSpeed();
            transferEntitySpeed.put(entity, currentSpeed);

            entity.setAttached(BackroomsAttachments.LOADING_WORLD, true);
            entity.setSpeed(0);
            entity.setInvulnerable(true);
        } else {
            float transferredSpeed = transferEntitySpeed.get(entity);
            transferEntitySpeed.remove(entity);

            entity.setAttached(BackroomsAttachments.LOADING_WORLD, false);
            entity.setSpeed(transferredSpeed);
            entity.setInvulnerable(false);
        }
    }

    static ServerPlayer.RespawnConfig getSpawnConfig(BlockPos position, float yaw, float pitch, ResourceKey<Level> level) {
        return new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(level, position, yaw, pitch), true);
    }

    static void affectPlayer(Entity entity) {
        ServerLevel level = (ServerLevel) entity.level(); // destination
        boolean fullyImmersed = level.getGameRules().get(BackroomsGameRules.FULL_IMMERSION);

        long seed = RandomSource.create().nextLong();
        level.playSeededSound(null, entity, BackroomsSounds.NOCLIP, SoundSource.PLAYERS, 1.0f, 1.0f, seed);
        if (entity instanceof LivingEntity && fullyImmersed) {
            MobEffectInstance mobEffect = new MobEffectInstance(MobEffects.DARKNESS, 130, 0, false, false, false);
            ((LivingEntity) entity).addEffect(mobEffect);
        }

        if (entity instanceof ServerPlayer && fullyImmersed) {
            entity.setAttached(BackroomsAttachments.LOADING_WORLD, false);
            if (level.dimension() == BackroomsLevels.LEVEL_0) {
                PlayerSnapshot.saveAndClear((ServerPlayer) entity);

                if (entity instanceof ServerPlayer) {
                    ServerPlayer.RespawnConfig respawn = LevelPortal.getSpawnConfig(entity.blockPosition(), entity.getXRot(), entity.getYRot(), BackroomsLevels.LEVEL_0);
                    ((ServerPlayer) entity).setRespawnPosition(respawn, false);
                }
            } else {
                PlayerSnapshot.restore((ServerPlayer) entity);
            }
        }
    }

    default BackroomsPortalBlock.SpawnLocation selectStartPosition(BlockPos origin, ServerLevel level, @Nullable BlockState updateBlock) {
        Optional<BlockPos> startPosition = Optional.empty();
        Optional<Float> rotation = Optional.empty();
        int y = 50; // from the feet position

        for (int x = 0; x < 16 && startPosition.isEmpty(); x++) {
            for (int z = 0; z < 16 && startPosition.isEmpty(); z++) {
                BlockPos position = origin.offset(x, 0, z).atY(y);
                if (level.isInWorldBounds(position)) {
                    BackroomsMod.LOGGER.info("[BackroomsMod+selectStartPosition] Looking for {}", position);
                    BlockState currentState = level.getBlockState(position);
                    BlockState currentAboveState = level.getBlockState(position.above(1));

                    if (currentState.canBeReplaced() && currentAboveState.canBeReplaced()) {
                        Direction spawnDirection = LevelPortal.getSpawnDirection(position, level);

                        if (updateBlock != null) {
                            Optional<Direction> hasFacing = updateBlock.getOptionalValue(HorizontalDirectionalBlock.FACING);
                            if (hasFacing.isPresent()) {
                                updateBlock = updateBlock.setValue(HorizontalDirectionalBlock.FACING, spawnDirection);
                            }
                        }

                        boolean isValid = this.isPositionValid(position, level, spawnDirection, updateBlock);

                        if (isValid) {
                            BackroomsMod.LOGGER.info("[BackroomsMod+selectStartPosition] Found start for {}", position);
                            startPosition = Optional.of(position);
                            rotation = Optional.of(spawnDirection.toYRot());
                        }
                    }
                }
            }
        }

        return new BackroomsPortalBlock.SpawnLocation(startPosition.orElse(BlockPos.ZERO), rotation.orElse(0.0f));
    }

    private boolean isPositionValid(BlockPos expectedStart, ServerLevel level, Direction spawnDirection, @Nullable BlockState updateBlock) {
        Direction backDirection = spawnDirection.getOpposite();
        BlockPos backSpawn = expectedStart.offset(backDirection.getStepX(), backDirection.getStepY(), backDirection.getStepZ());

        Block currentBlock = level.getBlockState(backSpawn).getBlock();
        Block topBlock = level.getBlockState(backSpawn.above(1)).getBlock();

        boolean isValid = (currentBlock == this.invalidBlock() && topBlock == this.invalidBlock()) || (currentBlock == this && topBlock == this);

        if (isValid && updateBlock != null) {
            level.setBlockAndUpdate(backSpawn, updateBlock);
            level.setBlockAndUpdate(backSpawn.above(1), updateBlock);
        }

        return isValid;
    }

    private static Direction getSpawnDirection(BlockPos expectedStart, ServerLevel level) {
        BlockState pxBlock = level.getBlockState(expectedStart.offset(1, 0, 0));
        if (pxBlock.canBeReplaced()) {
            return Direction.EAST;
        }

        BlockState nxBlock = level.getBlockState(expectedStart.offset(-1, 0, 0));
        if (nxBlock.canBeReplaced()) {
            return Direction.WEST;
        }

        BlockState pzBlock = level.getBlockState(expectedStart.offset(0, 0, 1));
        if (pzBlock.canBeReplaced()) {
            return Direction.SOUTH;
        }

        BlockState nzBlock = level.getBlockState(expectedStart.offset(0, 0, -1));
        if (nzBlock.canBeReplaced()) {
            return Direction.NORTH;
        }

        return Direction.EAST;
    }

    @Override
    default int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return 0;
    }

    @Override
    default Transition getLocalTransition() {
        return Transition.NONE;
    }

    Block invalidBlock();
    BlockState suffocatingBlock(BlockState currentState);

    enum EntityBounding {
        HEAD,
        FEET,
        BOTH,
        FULL_BODY;

        public boolean detectsFeet() {
            return this == EntityBounding.FEET || this == EntityBounding.BOTH;
        }

        public boolean detectsHead() {
            return this == EntityBounding.HEAD || this == EntityBounding.BOTH;
        }
    }

    record SpawnLocation(BlockPos position, float xRot, float yRot) {
        public SpawnLocation(BlockPos position, float xRot, float yRot) {
            this.position = position;
            this.xRot = xRot;
            this.yRot = yRot;
        }

        public SpawnLocation(BlockPos position, Float yRot) {
            this(position, 0.0f, yRot);
        }
    }
}
