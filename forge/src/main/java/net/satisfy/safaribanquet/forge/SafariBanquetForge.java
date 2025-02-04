package net.satisfy.safaribanquet.forge;

import dev.architectury.platform.forge.EventBuses;
import net.safaribanquet.Safaribanquet;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Safaribanquet.MOD_ID)
public class SafaribanquetForge {
    public SafaribanquetForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Safaribanquet.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Safaribanquet.init();
    }
}
