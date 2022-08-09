package com.lumination.backrooms.utils.extensions;

import com.lumination.backrooms.levels.Backroom;
import com.lumination.backrooms.levels.SafetyLevels;
import com.lumination.backrooms.utils.Backrooms;
import net.minecraft.text.Text;

public class ConvertRooms {
    private int room;

    public Backroom convert(Backrooms backroom) {
        if (backroom == Backrooms.OVERWORLD) {
            return new Backroom(0, SafetyLevels.NEUTRAL, Text.translatable("flat_world_preset.minecraft.overworld").getString());
        } else {
            this.room = enumToInt(backroom);
            return new Backroom(room);
        }
    }

    public int enumToInt(Backrooms enumerator) {
        if (enumerator == Backrooms.LEVEL_0) {
            return 0;
        }
        return 0;
    }
}
