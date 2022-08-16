package com.lumination.backrooms.utils;

import com.lumination.backrooms.BackroomsMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NoClipper {
    public static class NoClip {
        public BlockPos destination;

        private Entity target;
        private BlockPos oldPos;
        private boolean targetPlayer = false;
        private boolean client = false;
        private boolean clipping = false;

        public NoClip(Entity target, BlockPos destination) {
            setTarget(target);
        }

        public void setTarget(Entity target) {
            this.targetPlayer = target.isPlayer();
            this.oldPos = target.getBlockPos();
        }

        public Entity getTarget() {
            if (targetPlayer) {
                if (!this.target.world.isClient()) {
                    return (PlayerEntity) this.target;
                } else {
                    this.client = true;
                    return (ClientPlayerEntity) this.target;
                }
            } else {
                return this.target;
            }
        }

        public void start() {
            if (!this.clipping) {
                if (this.targetPlayer && this.client) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    Entity camera = client.getCameraEntity();
                    camera.move(MovementType.SELF, new Vec3d(1d, 1d, 1d));
                    // make camera and player "vibrate"
                }
            }
        }

        public void stop() {
            if (this.clipping) {
                // is clipping
                // stop the clipping
            }
        }

        public void teleport() {
            if (this.clipping) {
                // is clipping
                // make user fall through blocks and tp
            }
        }

        public boolean isClipping() {
            return this.clipping;
        }
    }
}
