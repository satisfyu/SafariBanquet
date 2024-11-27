package net.satisfy.safaribanquet.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.satisfy.safaribanquet.client.SafariBanquetClient;

public class SafariBanquetFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SafariBanquetClient.onInitializeClient();
    }
}
