package org.vfast.backrooms.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.network.ThresholdFadePacket;

/** Black fade-in-out played when using a Threshold. Not tied to the level, so it survives the dimension change */
public final class ThresholdFade {
    private static final Identifier HUD_ID = Identifier.fromNamespaceAndPath(BackroomsMod.ID, "threshold_fade");

    private static int fadeIn;
    private static int hold;
    private static int fadeOut;
    private static int tick = -1; // -1: not fading

    private ThresholdFade() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ThresholdFadePacket.TYPE, (payload, context) -> ThresholdFade.start(payload));
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> ThresholdFade.tick());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, minecraft) -> tick = -1);
        HudElementRegistry.attachElementAfter(VanillaHudElements.SUBTITLES, HUD_ID, ThresholdFade::render);
    }

    private static void start(ThresholdFadePacket packet) {
        fadeIn = Math.max(packet.fadeIn(), 1);
        hold = Math.max(packet.hold(), 0);
        fadeOut = Math.max(packet.fadeOut(), 1);
        tick = 0;
    }

    private static void tick() {
        if (tick < 0) return;

        tick++;
        if (tick > fadeIn + hold + fadeOut) {
            tick = -1;
        }
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (tick < 0) return;

        float time = tick + deltaTracker.getGameTimeDeltaPartialTick(false);
        float alpha;
        if (time < fadeIn) {
            alpha = time / fadeIn;
        } else if (time < fadeIn + hold) {
            alpha = 1.0f;
        } else {
            alpha = 1.0f - (time - fadeIn - hold) / fadeOut;
        }

        int color = ARGB.colorFromFloat(Mth.clamp(alpha, 0.0f, 1.0f), 0.0f, 0.0f, 0.0f);
        graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
    }
}
