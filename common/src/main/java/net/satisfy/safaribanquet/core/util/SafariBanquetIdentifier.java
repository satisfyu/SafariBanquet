package net.satisfy.safaribanquet.core.util;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.safaribanquet.SafariBanquet;

@SuppressWarnings("unused")
public class SafariBanquetIdentifier extends ResourceLocation {
    public SafariBanquetIdentifier(String path) {
        super(SafariBanquet.MOD_ID, path);
    }

    public static String asString(String path) {
        return (SafariBanquet.MOD_ID + ":" + path);
    }
}
