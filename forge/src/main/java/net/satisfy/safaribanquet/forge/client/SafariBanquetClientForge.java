package net.satisfy.safaribanquet.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.satisfy.safaribanquet.SafariBanquet;
import net.satisfy.safaribanquet.client.SafariBanquetClient;

@Mod.EventBusSubscriber(modid = SafariBanquet.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SafariBanquetClientForge {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        SafariBanquetClient.onInitializeClient();
    }

}
