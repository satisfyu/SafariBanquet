package net.satisfy.safaribanquet.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.safaribanquet.SafariBanquet;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> WILDERNATURE_TABS = DeferredRegister.create(SafariBanquet.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> WILDERNATURE_TAB = WILDERNATURE_TABS.register("safaribanquet", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ObjectRegistry.PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_ITEM.get()))
            .title(Component.translatable("creative_tab.safaribanquet"))
            .displayItems((parameters, out) -> {
                out.accept(ObjectRegistry.FOOD_STAMP_EXCHANCE_TABLE.get());
                out.accept(ObjectRegistry.MINCED_CASSOWARY_MEAT.get());
                out.accept(ObjectRegistry.SEASONED_CASSOWARY_MEAT.get());
                out.accept(ObjectRegistry.BISON_BURGER_ITEM.get());
                out.accept(ObjectRegistry.TURKEY_ON_A_BED_OF_SHROOMS_ITEM.get());
                out.accept(ObjectRegistry.PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_ITEM.get());
                out.accept(ObjectRegistry.PELICAN_POTROAST_ITEM.get());
                out.accept(ObjectRegistry.TURKEY_WITH_VEGETABLES_ITEM.get());
                out.accept(ObjectRegistry.VENISON_WITH_POTATOES_ITEM.get());
                out.accept(ObjectRegistry.PORKCHOP_WITH_TRUFFLES_ITEM.get());
                out.accept(ObjectRegistry.RICH_BISON_BBQ_PLATE_MAIN.get());
                out.accept(ObjectRegistry.HAZELNUT_PIE.get());
                out.accept(ObjectRegistry.HAZELNUT_PIE_SLICE.get());
                out.accept(ObjectRegistry.BURRITO_FLATBREAD.get());
                out.accept(ObjectRegistry.VEGETABLE_BURRITO.get());
                out.accept(ObjectRegistry.CASSOWARY_BURRITO.get());
                out.accept(ObjectRegistry.COMBO_BURRITO.get());
                out.accept(ObjectRegistry.FOOD_STAMP.get());
                out.accept(ObjectRegistry.HAZELNUT_PIE_RECIPE_BOOK_STACK.get());
                out.accept(ObjectRegistry.PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_RECIPE_BOOK_STACK.get());
                out.accept(ObjectRegistry.TURKEY_ON_A_BED_OF_SHROOMS_RECIPE_BOOK_STACK.get());
                out.accept(ObjectRegistry.PORKCHOP_WITH_TRUFFLES_RECIPE_BOOK_STACK.get());
                out.accept(ObjectRegistry.TURKEY_WITH_VEGETABLES_RECIPE_BOOK_STACK.get());
                out.accept(ObjectRegistry.VENISON_WITH_POTATOES_RECIPE_BOOK_STACK.get());
                out.accept(ObjectRegistry.SEASONED_CASSOWARY_MEAT_RECIPE_BOOK_STACK.get());
                out.accept(ObjectRegistry.PELICAN_POTROAST_RECIPE_BOOK_STACK.get());
            })
            .build());

    public static void init() {
        WILDERNATURE_TABS.register();
    }
}
