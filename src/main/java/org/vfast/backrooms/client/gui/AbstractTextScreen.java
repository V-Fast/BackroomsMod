package org.vfast.backrooms.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.vfast.backrooms.blocks.interfaces.TextBlockEntity;
import org.vfast.backrooms.network.UpdateTextSignPacket;

@Environment(EnvType.CLIENT)
public abstract class AbstractTextScreen<S extends BlockEntity & TextBlockEntity> extends Screen {
    private final S block;
    public Identifier blockTexture;
    public BlockPos blockPos;
    public String text;
    private final boolean isFrontText;

    private @Nullable TextFieldHelper signField;

    public AbstractTextScreen(S blockEntity, BlockPos pos, boolean isFrontText, Identifier texturePath, final Component title) {
        super(title);
        this.block = blockEntity;
        this.blockTexture = texturePath;
        this.blockPos = pos;
        this.isFrontText = isFrontText;
        this.text = isFrontText ? blockEntity.getFrontText() : blockEntity.getBackText();
    }

    @Override
    protected void init() {
        this.minecraft.textInputManager().startTextInput();
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, _ -> this.closeScreen()).bounds(this.width / 2 - 100, this.height / 4 + 144, 200, 20).build());
        this.signField = new TextFieldHelper(() -> this.text, this::setText, TextFieldHelper.createClipboardGetter(this.minecraft), TextFieldHelper.createClipboardSetter(this.minecraft), s -> this.font.width(s) <= this.block.maxTextWidth());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.centeredText(this.font, this.title, this.width / 2, 40, -1);
        this.render(graphics);
    }

    private void render(GuiGraphicsExtractor graphics) {
        graphics.pose().pushMatrix();
        float offsetX = this.width / 2.0F;
        float offsetY = this.getYOffset();
        graphics.pose().translate(offsetX, offsetY);
        graphics.pose().pushMatrix();
        graphics.pose().translate(0.0F, 27.0F);
        graphics.pose().scale(3.9F, 3.9F);
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.blockTexture, -12, 13, 0.0F, 0.0F, 24, 24, 24, 24);
        graphics.pose().popMatrix();
        Vector3fc textScale = this.getTextScale();
        graphics.pose().scale(textScale.x(), textScale.y());
        this.renderText(graphics);
        graphics.pose().popMatrix();
    }

    public abstract void renderText(GuiGraphicsExtractor graphics);

    public abstract float getYOffset();

    public Vector3fc getTextScale() {
        return new Vector3f(1.0f,1.0f, 1.0f);
    }

    private void setText(final String text) {
        this.text = text;
        if (this.isFrontText) {
            this.block.setFrontText(text);
        } else {
            this.block.setBackText(text);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        return this.signField.keyPressed(event) ? true : super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        return this.signField.charTyped(event) ? true : super.charTyped(event);
    }

    @Override
    public void removed() {
        if (!this.block.isRemoved()) {
            this.block.updateText(this.text, this.isFrontText);
            ClientPacketListener connection = this.minecraft.getConnection();
            if (connection != null) {
                ClientPlayNetworking.send(new UpdateTextSignPacket(this.block.getBlockPos(), this.isFrontText, this.text));
            }
        }

        this.minecraft.textInputManager().stopTextInput();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    public void closeScreen() {
        this.minecraft.gui.setScreen(null);
    }
}