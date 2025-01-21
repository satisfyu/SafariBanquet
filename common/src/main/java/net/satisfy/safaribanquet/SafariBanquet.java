package net.satisfy.safaribanquet;

import net.satisfy.safaribanquet.core.registry.MobEffectRegistry;
import net.satisfy.safaribanquet.core.registry.ObjectRegistry;
import net.satisfy.safaribanquet.core.registry.TabRegistry;

public class SafariBanquet {
    public static final String MOD_ID = "safaribanquet";

    public static void init() {
        ObjectRegistry.init();
        TabRegistry.init();
        MobEffectRegistry.init();
    }
}
