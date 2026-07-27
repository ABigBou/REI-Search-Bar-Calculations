package me.BigBou.rei_search_bar_calculations.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorCommand {
    private static final Pattern TRAILING_NUMBER_PATTERN = Pattern.compile("[0-9)]\\s+[0-9(]");

    public static int executeCommandWithArg(CommandContext<FabricClientCommandSource> context) {
        String value = StringArgumentType.getString(context, "value");

        Matcher matcher = TRAILING_NUMBER_PATTERN.matcher(value);
        if (matcher.find()) {
            context.getSource().getPlayer().sendSystemMessage(Component.literal("Invalid expression: unexpected trailing number"));
            return 0;
        }

        context.getSource().getPlayer().sendSystemMessage(Component.nullToEmpty(CalculatorSearch.format(value)));
        return 1;
    }
}
