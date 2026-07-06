package org.vfast.backrooms.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsGameRules {
    public static final GameRule<Boolean> FULL_IMMERSION = GameRuleBuilder
            .forBoolean(true)
            .category(GameRuleCategory.PLAYER)
            .buildAndRegister(Identifier.fromNamespaceAndPath(BackroomsMod.ID, "full_immersion"));

    public static final GameRule<Boolean> LIMITED_CHATTING = GameRuleBuilder
            .forBoolean(true)
            .category(GameRuleCategory.CHAT)
            .buildAndRegister(Identifier.fromNamespaceAndPath(BackroomsMod.ID, "limited_chatting"));

    public static void registerGameRules() {
        BackroomsMod.LOGGER.info("[BackroomsMod] GameRules initialized");
    }
}
