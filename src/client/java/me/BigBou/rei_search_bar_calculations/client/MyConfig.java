package me.BigBou.rei_search_bar_calculations.client;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

public class MyConfig {
    public static final ConfigClassHandler<MyConfig> HANDLER = ConfigClassHandler.createBuilder(MyConfig.class)
            .id(Identifier.fromNamespaceAndPath("rei_search_bar_calculations", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("rei_search_bar_calculations.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public int xOffset = 0;

    @SerialEntry
    public int yOffset = 0;
}
