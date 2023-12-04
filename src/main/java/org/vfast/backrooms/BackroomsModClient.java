package org.vfast.backrooms;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.vfast.backrooms.client.hud.CameraRecordHud;
import org.vfast.backrooms.client.hud.SanityHudOverlay;
import org.vfast.backrooms.config.BackroomsConfig;
import org.vfast.backrooms.entity.BackroomsEntities;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

@ClientOnly
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud cameraHud = new CameraRecordHud();
    public static final SanityHudOverlay sanityHud = new SanityHudOverlay();

    public static KeyBinding configKeybind;

    @Override
    public void onInitializeClient(ModContainer mod) {
        QuiltLoader.getModContainer(BackroomsMod.ID).ifPresent(modContainer -> {
            ResourceLoader.registerBuiltinResourcePack(new Identifier(BackroomsMod.ID, "old_ssc"), modContainer, ResourcePackActivationType.NORMAL);
        });
        BackroomsEntities.registerRenderers();
        registerHuds();
        configKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.backrooms.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KeyBinding.MISC_CATEGORY
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeybind.wasPressed()) {
                client.setScreen(BackroomsConfig.getInstance().getScreen(client.currentScreen));
            }
        });
        BackroomsMod.LOGGER.info("Initialized Client Backrooms");
    }

    public void registerHuds() {
        HudRenderCallback.EVENT.register(cameraHud);
        SanityHudOverlay.EVENT.register(sanityHud);
    }
}