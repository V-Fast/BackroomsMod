package com.lumination.backrooms.mixin;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.items.interactables.CameraItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
    private static final CameraRecordHud hud = BackroomsModClient.CameraHud;
    private static final Identifier RECORD_HUD = new Identifier(BackroomsMod.MOD_ID + "textures/hud/vhs.png");

    @Inject(at = @At("HEAD"), method = "render")
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ClientPlayerEntity cpe = MinecraftClient.getInstance().player;
        ItemStack handItemStack = cpe.getMainHandStack();
        ItemStack cameraItemStack = ModItems.CAMERA.getDefaultStack();

        if (handItemStack == cameraItemStack && hud.getVisible()) {
            hud.render(matrices);
        }
    }
}
