package org.vfast.backrooms.block.interactable;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.dimensions.BackroomsDimensions;

import java.util.Set;

import static net.minecraft.block.LeavesBlock.WATERLOGGED;
import static org.vfast.backrooms.dimensions.BackroomsDimensions.*;

public class ExitBlock extends Block implements Portal {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public ExitBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(entity.getServer() != null) {
            World entityWorld = entity.getEntityWorld();
            ServerWorld serverWorld = entity.getServer().getWorld(entityWorld.getRegistryKey());
            assert serverWorld != null;

            PointOfInterestStorage poi = serverWorld.getPointOfInterestStorage();
            poi.preloadChunks(serverWorld, pos, 4);

            TeleportTarget destinationTarget = this.createTeleportTarget(serverWorld, entity, pos);
            entity.teleportTo(destinationTarget);

            if(entityWorld.getRegistryKey() == LEVEL_ZERO_WORLD_KEY || entityWorld.getRegistryKey() == LEVEL_ONE_WORLD_KEY) {
                if (entity instanceof ServerPlayerEntity player) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0, false, false));
                }
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    private RegistryKey<World> getDestinationRegistry(RegistryKey<World> source) {
        if (source == LEVEL_ZERO_WORLD_KEY) {
            return LEVEL_ONE_WORLD_KEY;
        } else if (source == LEVEL_ONE_WORLD_KEY) {
            return LEVEL_TWO_WORLD_KEY;
        } else {
            // source is overworld, nether or the end
            return LEVEL_ZERO_WORLD_KEY;
        }
    }

    @Override
    public @Nullable TeleportTarget createTeleportTarget(ServerWorld world, Entity entity, BlockPos pos) {
        RegistryKey<World> registryKey = this.getDestinationRegistry(entity.getWorld().getRegistryKey());
        ServerWorld destinationWorld = world.getServer().getWorld(registryKey);

        BlockPos destination = entity.getBlockPos();
        destination = destination.withY(21);

        return new TeleportTarget(destinationWorld, destination.toBottomCenterPos(), entity.getVelocity(), entity.getYaw(), entity.getPitch(), TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET));
    }
}