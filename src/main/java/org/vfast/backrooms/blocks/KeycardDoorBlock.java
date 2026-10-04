package org.vfast.backrooms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.items.BackroomsItems;

public class KeycardDoorBlock extends DoorBlock {
    public KeycardDoorBlock(Properties properties) {
        super(BlockSetType.IRON, properties);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {} // prevent redstone activation

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.isOpen(state)) {
            this.setOpen(null, level, state, pos, false);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean wasOpen = this.isOpen(state);
        if (itemStack.getItem() != BackroomsItems.KEYCARD || wasOpen) return InteractionResult.FAIL;

        this.setOpen(player, level, state, pos, !wasOpen);
        level.scheduleTick(pos, this, 100);
        return InteractionResult.SUCCESS;
    }
}
