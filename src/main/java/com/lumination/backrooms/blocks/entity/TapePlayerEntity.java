package com.lumination.backrooms.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TapePlayerEntity extends BlockEntity {
    //protected final PropertyDelegate propertyDelegate;

    public TapePlayerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TAPE_PLAYER, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, TapePlayerEntity entity) {

    }
}
