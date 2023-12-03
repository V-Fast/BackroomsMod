package org.vfast.backrooms.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.world.BackroomsDimensions;

@ClientOnly
public class SanityHudOverlay implements HudRenderCallback {
    public static final Identifier BRAIN = new Identifier(BackroomsMod.ID, "textures/hud/sanity/brain.png");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        int x = 0;
        int y = 0;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && !client.player.isSpectator() && !client.player.isCreative()) {
            x = client.getWindow().getScaledWidth() / 2;
            y = client.getWindow().getScaledHeight();

            int sanity = client.player.getSanity();

            context.setShaderColor(10.0F / sanity, 1.0F, 1.0F, 1.0F);
            int height = 16;
            if (sanity <= 8) {
                height = sanity * 2;
            }
            context.drawTexture(BRAIN, x - 8, y - 45, 0, 0, 16, height, 16, 16);
        }
    }
}
