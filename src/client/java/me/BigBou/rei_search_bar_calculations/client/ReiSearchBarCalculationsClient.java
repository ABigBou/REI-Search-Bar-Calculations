package me.BigBou.rei_search_bar_calculations.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import me.shedaniel.rei.api.client.REIRuntime;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.shedaniel.rei.api.client.gui.screen.DisplayScreen;

public class ReiSearchBarCalculationsClient implements ClientModInitializer {

    private static final Logger log = LoggerFactory.getLogger(ReiSearchBarCalculationsClient.class);

    public static Screen createGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("REI Search Bar Calculations Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Change Text Location"))
                        .option(Option.<Integer>createBuilder()
                                .name(Text.literal("X Position Offset"))
                                .description(OptionDescription.of(Text.literal("Change the horizontal position of the calculation text next to the REI search bar.")))
                                .binding(0, () -> MyConfig.HANDLER.instance().xOffset, val -> MyConfig.HANDLER.instance().xOffset = val)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(-400, 400)
                                        .step(1))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.literal("Y Position Offset"))
                                .description(OptionDescription.of(Text.literal("Change the vertical position of the calculation text next to the REI search bar.")))
                                .binding(0, () -> MyConfig.HANDLER.instance().yOffset, val -> MyConfig.HANDLER.instance().yOffset = val)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(-400, 400)
                                        .step(1))
                                .build())
                        .build())
                .save(MyConfig.HANDLER::save)
                .build()
                .generateScreen(parent);
    }

    @Override
    public void onInitializeClient() {
        MyConfig.HANDLER.load();
        ScreenEvents.BEFORE_INIT.register((client, screen, sw, sh) -> {
            if (screen instanceof HandledScreen<?> handled || screen instanceof DisplayScreen displayScreen) {
                ScreenEvents.afterRender(screen).register((scr, context, mouseX, mouseY, delta) -> {
                    TextRenderer tr = MinecraftClient.getInstance().textRenderer;
                    int centerX = screen.width / 2;
                    int bottomY = screen.height;
                    int textPosX = centerX - 94 + MyConfig.HANDLER.instance().xOffset;
                    int textPosY = bottomY - 32 - MyConfig.HANDLER.instance().yOffset;
                    String text = CalculatorSearch.format(REIRuntime.getInstance().getSearchTextField().getText());
                    if (text.contains("=")) {
                        GameMode gameMode = ObjectUtils.defaultIfNull(client.player.getGameMode(), GameMode.SURVIVAL);
                        if (!gameMode.isCreative()) textPosX += 10;
                        context.drawText(tr, Text.literal(text), textPosX, textPosY, 0xFF55FF55, false);
                    }
                });
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("calc")
                    .then(ClientCommandManager.argument("value", StringArgumentType.greedyString())
                            .executes(CalculatorCommand::executeCommandWithArg)));
        });
    }
}
