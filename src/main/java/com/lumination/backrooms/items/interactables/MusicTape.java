package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.blocks.interactable.TapePlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class MusicTape extends MusicDiscItem {

    public MusicTape(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds) {
        super(comparatorOutput, sound, settings.rarity(Rarity.RARE).maxCount(1), lengthInSeconds);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        if (!blockState.isOf(ModBlocks.TAPE_PLAYER) || blockState.get(TapePlayer.HAS_RECORD).booleanValue()) {
            return ActionResult.PASS;
        }
        ItemStack itemStack = context.getStack();
        if (!world.isClient) {
            ((TapePlayer) ModBlocks.TAPE_PLAYER).setRecord(context.getPlayer(), world, blockPos, blockState, itemStack);
            world.syncWorldEvent(null, WorldEvents.MUSIC_DISC_PLAYED, blockPos, Item.getRawId(this));
            PlayerEntity playerEntity = context.getPlayer();
            if (playerEntity != null) {
                if (!playerEntity.isCreative()) {
                    itemStack.decrement(1);
                }
                playerEntity.incrementStat(Stats.PLAY_RECORD);
            }
        }
        return ActionResult.success(world.isClient);
    }
}
