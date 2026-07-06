package org.vfast.backrooms.blocks.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.blocks.entity.TextSignBlockEntity;

public class TextSignBlockEntityRenderer implements BlockEntityRenderer<TextSignBlockEntity, TextSignBlockEntityRenderState> {
    private final Font font;

    public TextSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.font();
    }

    @Override
    public TextSignBlockEntityRenderState createRenderState() {
        return new TextSignBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TextSignBlockEntity blockEntity, TextSignBlockEntityRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.frontText = blockEntity.getFrontText();
        state.backText = blockEntity.getBackText();
        state.rotation = blockEntity.getRotation();
    }

    @Override
    public void submit(TextSignBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.submitFront(state, matrices, submitNodeCollector);
        this.submitBack(state, matrices, submitNodeCollector);
    }

    private void submitFront(TextSignBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue) {
        if (state.frontText != null) {
            float width = this.font.width(state.frontText);
            float denominator = width > 10 ? width * 1.3f : width * 2.8f;

            matrices.pushPose();
            this.rotateMatrices(state, matrices, true);
            matrices.scale(1 / denominator, 1 / denominator, 1 / denominator);

            queue.submitText(
                    matrices,
                    -width / 2,
                    -4f,
                    Component.literal(state.frontText).getVisualOrderText(),
                    false,
                    Font.DisplayMode.POLYGON_OFFSET,
                    state.lightCoords,
                    0xfffa3232,
                    0,
                    0xff000000
            );

            matrices.popPose();
        }
    }

    private void submitBack(TextSignBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue) {
        if (state.backText != null) {
            float width = this.font.width(state.backText);
            float denominator = width > 10 ? width * 1.3f : width * 2.8f;

            matrices.pushPose();
            this.rotateMatrices(state, matrices, false);
            matrices.scale(1 / denominator, 1 / denominator, 1 / denominator);

            queue.submitText(
                    matrices,
                    -width / 2,
                    -4f,
                    Component.literal(state.backText).getVisualOrderText(),
                    false,
                    Font.DisplayMode.POLYGON_OFFSET,
                    state.lightCoords,
                    0xfffa3232,
                    0,
                    0xff000000
            );

            matrices.popPose();
        }
    }

    private void rotateMatrices(TextSignBlockEntityRenderState state, PoseStack matrices, boolean isFrontText) {
        switch (state.rotation) {
            case NORTH -> {
                if (isFrontText) {
                    matrices.translate(0.5, 5.5f / 16f, 0.483);
                    matrices.mulPose(Axis.ZP.rotationDegrees(180));
                } else {
                    matrices.translate(0.5, 5.5f / 16f, 0.517);
                    matrices.mulPose(Axis.YP.rotationDegrees(180));
                    matrices.mulPose(Axis.ZP.rotationDegrees(180));
                }
            }
            case SOUTH -> {
                if (isFrontText) {
                    matrices.translate(0.5, 5.5f / 16f, 0.517);
                    matrices.mulPose(Axis.YP.rotationDegrees(180));
                    matrices.mulPose(Axis.ZP.rotationDegrees(180));
                } else {
                    matrices.translate(0.5, 5.5f / 16f, 0.483);
                    matrices.mulPose(Axis.ZP.rotationDegrees(180));
                }
            }
            case EAST -> {
                if (isFrontText) {
                    matrices.translate(0.517, 5.5f / 16f, 0.5);
                    matrices.mulPose(Axis.ZP.rotationDegrees(180));
                    matrices.mulPose(Axis.YP.rotationDegrees(90));
                } else {
                    matrices.translate(0.483, 5.5f / 16f, 0.5);
                    matrices.mulPose(Axis.YP.rotationDegrees(90));
                    matrices.mulPose(Axis.ZP.rotationDegrees(180));
                }
            }
            case WEST -> {
                if (isFrontText) {
                    matrices.translate(0.483, 5.5f / 16f, 0.5);
                    matrices.mulPose(Axis.YP.rotationDegrees(90));
                    matrices.mulPose(Axis.ZP.rotationDegrees(180));
                } else {
                    matrices.translate(0.517, 5.5f / 16f, 0.5);
                    matrices.mulPose(Axis.ZP.rotationDegrees(180));
                    matrices.mulPose(Axis.YP.rotationDegrees(90));
                }
            }
        }
    }
}
