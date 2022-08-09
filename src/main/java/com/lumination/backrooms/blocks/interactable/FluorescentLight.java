package com.lumination.backrooms.blocks.interactable;

import com.lumination.backrooms.sounds.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FluorescentLight extends Block {
    public FluorescentLight(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(125) == 0) {
            world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, ModSounds.LIGHT_BUZZING, SoundCategory.BLOCKS, 0.25f, 1, true);
        }

        super.randomDisplayTick(state, world, pos, random);
    }
}
