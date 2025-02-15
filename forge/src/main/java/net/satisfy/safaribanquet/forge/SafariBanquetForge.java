package net.satisfy.safaribanquet.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.safaribanquet.SafariBanquet;
import net.satisfy.safaribanquet.forge.core.world.entity.npc.ForgeVillagerTrades;

@Mod(SafariBanquet.MOD_ID)
public class SafariBanquetForge {
    public SafariBanquetForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(SafariBanquet.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SafariBanquet.init();
        ForgeVillagerTrades.register(modEventBus);

    }
}
