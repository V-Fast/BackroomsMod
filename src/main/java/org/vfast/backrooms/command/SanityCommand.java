package org.vfast.backrooms.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.AngleArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SanityCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("sanity")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(context -> execute(context.getSource(), EntityArgumentType.getPlayer(context, "target"), 0))
                                .then(
                                        CommandManager.argument("value", IntegerArgumentType.integer(1, 10))
                                                .executes(context -> execute(context.getSource(), EntityArgumentType.getPlayer(context, "target"), IntegerArgumentType.getInteger(context, "value")))
                                ))
        );
    }

    private static int execute(ServerCommandSource source, ServerPlayerEntity player, int value) {
        if (value == 0) {
            source.sendFeedback(() -> {
                int sanity = player.getSanity();
                if (sanity == 1) {
                    return Text.translatable("commands.sanity.getter.success.singular", player.getName(), sanity);
                } else {
                    return Text.translatable("commands.sanity.getter.success.plural", player.getName(), sanity);
                }
            }, false);
        } else {
            player.setSanity(value);
            source.sendFeedback(() -> {
                if (value == 1) {
                    return Text.translatable("commands.sanity.setter.success.singular", player.getName(), value);
                } else {
                    return Text.translatable("commands.sanity.setter.success.plural", player.getName(), value);
                }
            }, false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
