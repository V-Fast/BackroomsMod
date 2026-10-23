package org.vfast.backrooms.client.render.entity;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;
import org.vfast.backrooms.entity.stilllife.StillLifeAnatomy;
import org.vfast.backrooms.entity.stilllife.StillLifeStance;

/**
 * Must not extend {@code AvatarRenderState}: the renderer dispatcher sends every avatar state to the vanilla player
 * renderer, which would draw a plain player model instead of the Still Life one.
 */
public class StillLifeRenderState extends HumanoidRenderState {
    public PlayerSkin skin = DefaultPlayerSkin.getDefaultSkin();
    public StillLifeStance stance = StillLifeStance.UPRIGHT;
    public StillLifeAnatomy anatomy = StillLifeAnatomy.fromSeed(0);
}
