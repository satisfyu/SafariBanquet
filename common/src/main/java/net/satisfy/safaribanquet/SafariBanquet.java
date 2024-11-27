package net.satisfy.safaribanquet;

import net.satisfy.safaribanquet.registry.MobEffectRegistry;
import net.satisfy.safaribanquet.registry.ObjectRegistry;
import net.satisfy.safaribanquet.registry.TabRegistry;

public class SafariBanquet {
    public static final String MOD_ID = "safaribanquet";

    public static void init() {
        ObjectRegistry.init();
        TabRegistry.init();
        MobEffectRegistry.init();
    }
}
