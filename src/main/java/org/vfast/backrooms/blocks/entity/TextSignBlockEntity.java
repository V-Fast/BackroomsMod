package org.vfast.backrooms.blocks.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.vfast.backrooms.blocks.interfaces.CeilingSupportSign;
import org.vfast.backrooms.blocks.interfaces.TextBlockEntity;

import java.util.Objects;

public class TextSignBlockEntity extends BlockEntity implements TextBlockEntity {
    private String frontText;
    private String backText;
    private Direction blockRotation;

    public static final EnumProperty<Direction> ROTATION = BlockStateProperties.HORIZONTAL_FACING;

    public TextSignBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BackroomsBlockEntities.TEXT_SIGN_ENTITY, worldPosition, blockState);
        this.frontText = "F";
        this.backText = "B";
        this.blockRotation = blockState.getValue(ROTATION);
    }

    public String getFrontText() {
        return this.frontText;
    }

    public String getBackText() {
        return this.backText;
    }

    @Override
    public int maxTextWidth() {
        return 75;
    }

    public Direction getRotation() {
        return this.blockRotation;
    }

    @Override
    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);
        output.store("front_text", Codec.STRING, this.frontText);
        output.store("back_text", Codec.STRING, this.backText);
    }

    @Override
    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);
        this.frontText = input.read("front_text", Codec.STRING).orElse("");
        this.backText = input.read("back_text", Codec.STRING).orElse("");
    }

    public boolean setBackText(final String text) {
        if (!Objects.equals(text, this.backText)) {
            this.backText = text;
            this.markUpdated();
            return true;
        } else {
            return false;
        }
    }

    public boolean setFrontText(final String text) {
        if (!Objects.equals(text, this.frontText)) {
            this.frontText = text;
            this.markUpdated();
            return true;
        } else {
            return false;
        }
    }

    public void updateText(String text, boolean isFrontText) {
        if (isFrontText) {
            this.setFrontText(text);
        } else {
            this.setBackText(text);
        }
    }

    private void markUpdated() {
        this.setChanged();
        assert this.level != null;
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
