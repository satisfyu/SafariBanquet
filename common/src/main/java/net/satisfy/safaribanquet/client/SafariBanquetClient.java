package net.satisfy.safaribanquet.client;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;

import static net.satisfy.safaribanquet.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class SafariBanquetClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(), PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_BLOCK.get(), TURKEY_ON_A_BED_OF_SHROOMS_BLOCK.get(), PORKCHOP_WITH_TRUFFLES_BLOCK.get(), BISON_BURGER_BLOCK.get(), PELICAN_POTROAST_BLOCK.get(), TURKEY_WITH_VEGETABLES_BLOCK.get(), VENISON_WITH_PASTA_AND_SAUCE_BLOCK.get(), BURRITO_FLATBREAD.get()
        );
    }
}
