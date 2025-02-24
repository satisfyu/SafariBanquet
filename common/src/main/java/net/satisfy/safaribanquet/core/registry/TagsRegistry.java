package net.satisfy.safaribanquet.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.satisfy.safaribanquet.core.util.SafariBanquetIdentifier;

public class TagsRegistry {
    public static final TagKey<Item> CABBAGE = TagKey.create(Registries.ITEM, new SafariBanquetIdentifier("cabbage"));
    public static final TagKey<Item> KNIVES = TagKey.create(Registries.ITEM, new SafariBanquetIdentifier("knives"));
}

