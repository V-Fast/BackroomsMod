package com.lumination.backrooms.utils;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import org.slf4j.Logger;

public class NoClipper {
    public static class EntityClip {
        public Entity entity = null;

        private boolean clipping = false;
        private Logger console = BackroomsMod.LOGGER; // bad javascript habit

        public EntityClip(Entity entity) {
            this.entity = entity;
        }

        public boolean isClipping() {
            return clipping;
        }

        public void startClipping() {
            if (this.entity != null && !this.clipping) {
                this.clipping = true;
                Double height = this.entity.getY();
            } else {
                // errors
                if (this.clipping) {
                    console.error("Cannot start clipping when entity is already clipping");
                }
                if (this.entity == null) {
                    console.error("Cannot start clipping without entity");
                }
            }
        }

        public void startClipping(Entity entity) {
            this.entity = entity;
            startClipping();
        }

        public void clip(boolean noCheck) {
            if (noCheck) {
                int timeGap = 3;
            }
        }

        private void entityClip(Identifier dimensionId) {
            DimensionType entityDimension = this.entity.getWorld().getDimension();
            RegistryKey<DimensionType> destination = BackroomDimensions.BR_TYPE_KEY;
        }
    }
}
