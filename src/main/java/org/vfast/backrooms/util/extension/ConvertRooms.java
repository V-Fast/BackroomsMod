package org.vfast.backrooms.util.extension;

import org.vfast.backrooms.level.Backroom;
import org.vfast.backrooms.level.SafetyLevels;
import org.vfast.backrooms.util.Backrooms;
import net.minecraft.text.Text;

public class ConvertRooms {
    private int room;

    public Backroom convert(Backrooms backroom) {
        if (backroom == Backrooms.OVERWORLD) {
            Backroom overworld = new Backroom(0, SafetyLevels.NEUTRAL, Text.translatable("flat_world_preset.minecraft.overworld").getString());
            overworld.isBackroom(false);
            return overworld;
        } else if (backroom == Backrooms.UNKNOWN) {
            Backroom unknown = new Backroom(0, SafetyLevels.UNKNOWN, "???");
            unknown.isBackroom(false);
            return unknown;
        } else {
            this.room = enumToInt(backroom);
            Backroom lvl = new Backroom(this.room);
            lvl.isBackroom(true);
            return lvl;
        }
    }

    public Backroom convert(String dimension) {
        if (dimension.contains("type")) {
            dimension = dimension.replace("_type", "");
        }
        if (dimension.equalsIgnoreCase("level_0")) {
            return convert(Backrooms.LEVEL_0);
        } else if (dimension.equals("overworld")) {
            return convert(Backrooms.OVERWORLD);
        }
        return convert(Backrooms.UNKNOWN);
    }

    public int enumToInt(Backrooms enumerator) {
        if (enumerator == Backrooms.LEVEL_0) {
            return 0;
        }
        return 0;
    }

    /*
     * public void find(MinecraftServer server, Identifier dimId) {
     *     HashMap<Identifier, RegistryKey<World>> dims = new HashMap<>();
     *     for (RegistryKey<World> registryKey : server.getWorldRegistryKeys()) {
     *         dims.put(registryKey.getValue(), registryKey);
     *     }
     * }
     */
}
