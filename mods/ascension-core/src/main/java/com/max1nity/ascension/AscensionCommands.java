package com.max1nity.ascension;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;

/** /ascension status, and (op only) /ascension add <path> <amount> for testing. */
public final class AscensionCommands {
    private AscensionCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ascension")
                .then(Commands.literal("status")
                        .executes(AscensionCommands::status))
                .then(Commands.literal("add")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("path", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        Arrays.stream(AscensionPath.values()).map(AscensionPath::getId),
                                        builder))
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 250))
                                        .executes(AscensionCommands::add)))));
    }

    private static int status(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        for (AscensionPath path : AscensionPath.values()) {
            int points = AscensionData.getPoints(player, path);
            int tier = AscensionData.tierOf(points);
            String progress;
            if (tier >= AscensionData.MAX_TIER) {
                progress = "max tier";
            } else {
                int toNext = (tier + 1) * AscensionData.POINTS_PER_TIER - points;
                progress = toNext + " to next tier";
            }
            context.getSource().sendSuccess(() -> Component.translatable(path.getTranslationKey())
                    .append(Component.literal(": " + points + " points, tier " + tier + " (" + progress + ")")),
                    false);
        }
        int balanced = AscensionData.getBalancedTier(player);
        context.getSource().sendSuccess(
                () -> Component.literal("Balanced tier: " + balanced), false);
        return 1;
    }

    private static int add(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String pathId = StringArgumentType.getString(context, "path");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        AscensionPath path = AscensionPath.fromId(pathId);
        if (path == null) {
            context.getSource().sendFailure(Component.literal(
                    "Unknown path '" + pathId + "'. Use tech, arcane or wanderer."));
            return 0;
        }
        AscensionData.addPoints(player, path, amount);
        context.getSource().sendSuccess(
                () -> Component.literal("Added " + amount + " " + path.getId() + " points."), true);
        return 1;
    }
}
