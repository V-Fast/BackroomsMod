package org.vfast.backrooms.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class BackroomsGamerules {
    public static final GameRules.Key<GameRules.BooleanRule> DO_I_HALLCUINATE =
            GameRuleRegistry.register("doIHallucinate", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));

    public static void registerGamerules() {}
}
