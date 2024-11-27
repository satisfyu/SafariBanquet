package net.satisfy.safaribanquet.fabric;

import net.fabricmc.api.ModInitializer;
import net.satisfy.safaribanquet.SafariBanquet;

public class SafariBanquetFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SafariBanquet.init();
    }
}
