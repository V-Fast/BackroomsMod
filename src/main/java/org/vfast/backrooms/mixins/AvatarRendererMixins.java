package org.vfast.backrooms.mixins;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.items.BackroomsItems;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixins <AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    public AvatarRendererMixins(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", at = @At(value = "RETURN"), cancellable = true)
    private static void camcorderPose(Avatar avatar, ItemStack itemInHand, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        HumanoidModel.ArmPose itemArmPose = getItemArmPose(itemInHand.getUseAnimation());
        if (ItemStack.isSameItem(BackroomsItems.CAMCORDER.getDefaultInstance(), itemInHand) && !avatar.swinging && itemArmPose != null) {
            cir.setReturnValue(itemArmPose);
        }
    }

    @Unique
    private static HumanoidModel.@Nullable ArmPose getItemArmPose(ItemUseAnimation useAnimation) {
        switch (useAnimation) {
            case CROSSBOW -> {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            case BOW -> {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }

            case BLOCK -> {
                return HumanoidModel.ArmPose.BLOCK;
            }

            case SPYGLASS -> {
                return HumanoidModel.ArmPose.SPYGLASS;
            }

            case TRIDENT -> {
                return HumanoidModel.ArmPose.THROW_TRIDENT;
            }
        }

        return null;
    }
}
