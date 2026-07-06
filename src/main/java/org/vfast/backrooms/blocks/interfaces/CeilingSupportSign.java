package org.vfast.backrooms.blocks.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public interface CeilingSupportSign {
    default boolean canSurvive(final LevelReader level, final BlockPos pos) {
        return level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN, SupportType.CENTER);
    }

    default BlockState updateShape(BlockState state, LevelReader level, BlockPos pos) {
        return state.canSurvive(level, pos) ? state : Blocks.AIR.defaultBlockState();
    }
}
