package me.BigBou.rei_search_bar_calculations.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import me.shedaniel.rei.api.client.REIRuntime;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rei_search_bar_calculationsClient implements ClientModInitializer {

    private static final Logger log = LoggerFactory.getLogger(Rei_search_bar_calculationsClient.class);

    @Override
    public void onInitializeClient() {
        ScreenEvents.BEFORE_INIT.register((client, screen, sw, sh) -> {
            if (screen instanceof HandledScreen<?> handled) {
                ScreenEvents.afterRender(screen).register((scr, context, mouseX, mouseY, delta) -> {
                    TextRenderer tr = MinecraftClient.getInstance().textRenderer;
                    int centerX = handled.width / 2;
                    int bottomY = handled.height;
                    int textPosX = centerX - 94;
                    int textPosY = bottomY - 32;
                    String text = CalculatorSearch.format(REIRuntime.getInstance().getSearchTextField().getText());
                    if (text.contains("=")) {
                        if (!client.player.getGameMode().isCreative()) textPosX += 10;
                        context.drawText(tr, Text.literal(text), textPosX, textPosY, 0x55FF55, false);
                    }
                });
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("calc")
                    .then(ClientCommandManager.argument("value", StringArgumentType.string())
                            .executes(CalculatorCommand::executeCommandWithArg)));
        });
    }
}
