package org.vfast.backrooms.item.interactable;

import org.vfast.backrooms.block.BackroomsBlocks;
import org.vfast.backrooms.block.interactable.TapePlayerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MusicTape extends MusicDiscItem {
    public MusicTape(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds) {
        super(comparatorOutput, sound, settings.rarity(Rarity.RARE).maxCount(1), lengthInSeconds);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        if (!blockState.isOf(BackroomsBlocks.TAPE_PLAYER) || blockState.get(TapePlayerBlock.HAS_RECORD).booleanValue()) {
            return ActionResult.PASS;
        }
        ItemStack itemStack = context.getStack();
        if (!world.isClient) {
            ((TapePlayerBlock) BackroomsBlocks.TAPE_PLAYER).setRecord(context.getPlayer(), world, blockPos, blockState, itemStack);
            world.playSound(null, context.getBlockPos(), this.getSound(), SoundCategory.RECORDS, 0.75f, 1f);
            world.getServer().getPlayerManager().broadcast(Text.translatable("record.nowPlaying", Text.translatable(this.getTranslationKey() + ".desc").getString()).formatted(Formatting.YELLOW), true);
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
