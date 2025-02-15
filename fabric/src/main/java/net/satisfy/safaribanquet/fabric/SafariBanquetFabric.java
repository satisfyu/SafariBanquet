package net.satisfy.safaribanquet.fabric;

import net.fabricmc.api.ModInitializer;
import net.satisfy.safaribanquet.SafariBanquet;
import net.satisfy.safaribanquet.fabric.core.world.entity.npc.VillagerTrades;

public class SafariBanquetFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SafariBanquet.init();
        VillagerTrades.init();
    }
}
