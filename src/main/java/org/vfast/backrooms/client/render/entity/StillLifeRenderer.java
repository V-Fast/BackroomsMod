package org.vfast.backrooms.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.client.BackroomsModelLayers;
import org.vfast.backrooms.entity.stilllife.StillLife;

public class StillLifeRenderer extends LivingEntityRenderer<StillLife, StillLifeRenderState, StillLifeModel> {
    /** Same skins and order as {@code DefaultPlayerSkin}, which keeps its list private. */
    private static final PlayerSkin[] DEFAULT_SKINS = {
            skin("entity/player/slim/alex", PlayerModelType.SLIM),
            skin("entity/player/slim/ari", PlayerModelType.SLIM),
            skin("entity/player/slim/efe", PlayerModelType.SLIM),
            skin("entity/player/slim/kai", PlayerModelType.SLIM),
            skin("entity/player/slim/makena", PlayerModelType.SLIM),
            skin("entity/player/slim/noor", PlayerModelType.SLIM),
            skin("entity/player/slim/steve", PlayerModelType.SLIM),
            skin("entity/player/slim/sunny", PlayerModelType.SLIM),
            skin("entity/player/slim/zuri", PlayerModelType.SLIM),
            skin("entity/player/wide/alex", PlayerModelType.WIDE),
            skin("entity/player/wide/ari", PlayerModelType.WIDE),
            skin("entity/player/wide/efe", PlayerModelType.WIDE),
            skin("entity/player/wide/kai", PlayerModelType.WIDE),
            skin("entity/player/wide/makena", PlayerModelType.WIDE),
            skin("entity/player/wide/noor", PlayerModelType.WIDE),
            skin("entity/player/wide/steve", PlayerModelType.WIDE),
            skin("entity/player/wide/sunny", PlayerModelType.WIDE),
            skin("entity/player/wide/zuri", PlayerModelType.WIDE)
    };

    private final PlayerSkinRenderCache skinRenderCache;
    /** Slim skins need the slim arms, so the wide renderer hands those over to a slim one. */
    private final @Nullable StillLifeRenderer slimRenderer;

    public StillLifeRenderer(EntityRendererProvider.Context context) {
        this(context, false);
    }

    private StillLifeRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, new StillLifeModel(context.bakeLayer(slim ? BackroomsModelLayers.STILL_LIFE_SLIM : BackroomsModelLayers.STILL_LIFE)), 0.5F);
        this.skinRenderCache = context.getPlayerSkinRenderCache();
        this.slimRenderer = slim ? null : new StillLifeRenderer(context, true);
    }

    private static PlayerSkin skin(String texture, PlayerModelType model) {
        return new PlayerSkin(new ClientAsset.ResourceTexture(Identifier.withDefaultNamespace(texture)), null, null, model, true);
    }

    @Override
    public void submit(StillLifeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (this.slimRenderer != null && state.skin.model() == PlayerModelType.SLIM) {
            this.slimRenderer.submit(state, poseStack, submitNodeCollector, camera);
        } else {
            super.submit(state, poseStack, submitNodeCollector, camera);
        }
    }

    @Override
    public StillLifeRenderState createRenderState() {
        return new StillLifeRenderState();
    }

    @Override
    public void extractRenderState(StillLife entity, StillLifeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, this.itemModelResolver);
        state.skin = entity.hasCustomProfile()
                ? this.skinRenderCache.getOrDefault(entity.getProfile()).playerSkin()
                : DEFAULT_SKINS[entity.getDefaultSkin()];
        state.stance = entity.getStance();
        state.anatomy = entity.getAnatomy();
    }

    @Override
    protected boolean shouldShowName(StillLife entity, double distanceToCameraSq) {
        if (!super.shouldShowName(entity, distanceToCameraSq)) return false;
        // a copy of someone always wears their name, the others follow the usual mob name tag rules
        return entity.hasCustomProfile()
                || entity.shouldShowName()
                || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity;
    }

    @Override
    protected @Nullable Component getNameTag(StillLife entity) {
        if (entity.hasCustomProfile() && !entity.hasCustomName()) {
            // the resolved profile also knows the name of profiles only given by UUID
            return Component.literal(this.skinRenderCache.getOrDefault(entity.getProfile()).gameProfile().name());
        }
        return super.getNameTag(entity);
    }

    @Override
    public Identifier getTextureLocation(StillLifeRenderState state) {
        return state.skin.body().texturePath();
    }

    @Override
    protected void scale(StillLifeRenderState state, PoseStack poseStack) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public Vec3 getRenderOffset(StillLifeRenderState state) {
        Vec3 offset = super.getRenderOffset(state);
        return state.isCrouching ? offset.add(0.0, state.scale * -2.0F / 16.0, 0.0) : offset;
    }
}
