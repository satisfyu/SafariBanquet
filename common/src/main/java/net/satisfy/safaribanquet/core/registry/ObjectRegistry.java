package net.satisfy.safaribanquet.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.farm_and_charm.core.block.FoodBlock;
import net.satisfy.farm_and_charm.core.item.food.EffectBlockItem;
import net.satisfy.farm_and_charm.core.item.food.EffectFoodItem;
import net.satisfy.farm_and_charm.core.item.food.EffectItem;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;
import net.satisfy.safaribanquet.SafariBanquet;
import net.satisfy.safaribanquet.core.block.BurritoBlock;
import net.satisfy.safaribanquet.core.block.HazelnutCakeBlock;
import net.satisfy.safaribanquet.core.block.RichBisonBBQPlateBlock;
import net.satisfy.safaribanquet.core.item.GrannysGourmetGrimoire;
import net.satisfy.safaribanquet.core.util.SafariBanquetFoodProperties;
import net.satisfy.safaribanquet.core.util.SafariBanquetIdentifier;
import net.satisfy.safaribanquet.core.util.SafariBanquetUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;



public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(SafariBanquet.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(SafariBanquet.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Item> MINCED_CASSOWARY_MEAT = registerItem("minced_cassowary_meat", () -> new Item(getSettings().food(SafariBanquetFoodProperties.MINCED_CASSOWARY_MEAT)));
    public static final RegistrySupplier<Block> TURKEY_ON_A_BED_OF_SHROOMS_BLOCK = registerWithoutItem("turkey_on_a_bed_of_shrooms_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, SafariBanquetFoodProperties.TURKEY_ON_A_BED_OF_SHROOMS));
    public static final RegistrySupplier<Item> TURKEY_ON_A_BED_OF_SHROOMS_ITEM = registerItem("turkey_on_a_bed_of_shrooms", () -> new EffectBlockItem(TURKEY_ON_A_BED_OF_SHROOMS_BLOCK.get(), new Item.Properties().food(SafariBanquetFoodProperties.TURKEY_ON_A_BED_OF_SHROOMS)));
    public static final RegistrySupplier<Block> PORKCHOP_WITH_TRUFFLES_BLOCK = registerWithoutItem("porkchop_with_truffles_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 5, SafariBanquetFoodProperties.PORKCHOP_WITH_TRUFFLES));
    public static final RegistrySupplier<Item> PORKCHOP_WITH_TRUFFLES_ITEM = registerItem("porkchop_with_truffles", () -> new EffectBlockItem(PORKCHOP_WITH_TRUFFLES_BLOCK.get(), new Item.Properties().food(SafariBanquetFoodProperties.PORKCHOP_WITH_TRUFFLES)));
    public static final RegistrySupplier<Block> PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_BLOCK = registerWithoutItem("pelican_in_lilipads_with_oat_patties_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 5, SafariBanquetFoodProperties.PELICAN_IN_LILIPADS_WITH_OAT_PATTIES));
    public static final RegistrySupplier<Item> PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_ITEM = registerItem("pelican_in_lilipads_with_oat_patties", () -> new EffectBlockItem(PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_BLOCK.get(), new Item.Properties().food(SafariBanquetFoodProperties.PELICAN_IN_LILIPADS_WITH_OAT_PATTIES)));
    public static final RegistrySupplier<Block> BISON_BURGER_BLOCK = registerWithoutItem("bison_burger_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 5, SafariBanquetFoodProperties.BISON_BURGER));
    public static final RegistrySupplier<Item> BISON_BURGER_ITEM = registerItem("bison_burger", () -> new EffectBlockItem(BISON_BURGER_BLOCK.get(), new Item.Properties().food(SafariBanquetFoodProperties.BISON_BURGER)));
    public static final RegistrySupplier<Item> HAZELNUT_CAKE_SLICE = registerItem("hazelnut_cake_slice", () -> new EffectItem(new Item.Properties().food(SafariBanquetFoodProperties.HAZELNUT_CAKE), 700, false));
    public static final RegistrySupplier<Block> HAZELNUT_CAKE_BLOCK = registerWithItem("hazelnut_cake_block", () -> new HazelnutCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), HAZELNUT_CAKE_SLICE));
    public static final RegistrySupplier<Block> PELICAN_POTROAST_BLOCK = registerWithoutItem("pelican_potroast_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build()));
    public static final RegistrySupplier<Item> PELICAN_POTROAST_ITEM = registerItem("pelican_potroast", () -> new EffectBlockItem(PELICAN_POTROAST_BLOCK.get(), getFoodItemSettings(8, 0.8f, MobEffects.SLOW_FALLING, 2400)));
    public static final RegistrySupplier<Block> TURKEY_WITH_VEGETABLES_BLOCK = registerWithoutItem("turkey_with_vegetables_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 5, new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build()));
    public static final RegistrySupplier<Item> TURKEY_WITH_VEGETABLES_ITEM = registerItem("turkey_with_vegetables", () -> new EffectBlockItem(TURKEY_WITH_VEGETABLES_BLOCK.get(), getFoodItemSettings(8, 0.8f, MobEffectRegistry.FEAST.get(), 4800)));
    public static final RegistrySupplier<Block> VENISON_WITH_PASTA_AND_SAUCE_BLOCK = registerWithoutItem("venison_with_pasta_and_sauce_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, SafariBanquetFoodProperties.VENISON_WITH_PASTA_AND_SAUCE));
    public static final RegistrySupplier<Item> VENISON_WITH_PASTA_AND_SAUCE_ITEM = registerItem("venison_with_pasta_and_sauce", () -> new EffectBlockItem(VENISON_WITH_PASTA_AND_SAUCE_BLOCK.get(), getFoodItemSettings(8, 0.8f, MobEffects.INVISIBILITY, 1200)));
    public static final RegistrySupplier<Item> BURRITO_BEEF = registerItem("burrito_beef", () -> new EffectItem(getFoodItemSettings(6, 0.6f, MobEffectRegistry.RESTED.get(), 1800), 900, true));
    public static final RegistrySupplier<Block> BURRITO_FLATBREAD = registerWithItem("burrito_flatbread", () -> new BurritoBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistrySupplier<Item> COMBO_BURRITO = registerItem("combo_burrito", () -> new EffectItem(getFoodItemSettings(6, 0.6f, MobEffectRegistry.RESTED.get(), 1800), 1800, false));
    public static final RegistrySupplier<Item> CASSOWARY_BURRITO = registerItem("cassowary_burrito", () -> new EffectItem(getFoodItemSettings(6, 0.6f, MobEffectRegistry.RESTED.get(), 1800), 1800, false));
    public static final RegistrySupplier<Item> VEGETABLE_BURRITO = registerItem("vegetable_burrito", () -> new EffectItem(getFoodItemSettings(6, 0.6f, MobEffectRegistry.RESTED.get(), 1800), 1800, false));
    public static final RegistrySupplier<Item> DRIED_VENISON = registerItem("dried_venison", () -> new EffectFoodItem(getFoodItemSettings(6, 0.6f, MobEffectRegistry.SATIATION.get(), 900), 900));
    public static final RegistrySupplier<Block> RICH_BISON_BBQ_PLATE_HEAD = registerWithoutItem("rich_bison_bbq_plate_head", () -> new RichBisonBBQPlateBlock.BBQPlateHeadBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).pushReaction(PushReaction.IGNORE).instabreak(), SafariBanquetFoodProperties.RICH_BISON_BBQ_PLATE));
    public static final RegistrySupplier<Block> RICH_BISON_BBQ_PLATE_HEAD_RIGHT = registerWithoutItem("rich_bison_bbq_plate_head_right", () -> new RichBisonBBQPlateBlock.BBQPlateHeadRightBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).pushReaction(PushReaction.IGNORE).instabreak(), SafariBanquetFoodProperties.RICH_BISON_BBQ_PLATE));
    public static final RegistrySupplier<Block> RICH_BISON_BBQ_PLATE_MAIN = registerWithItem("rich_bison_bbq_plate_main", () -> new RichBisonBBQPlateBlock.BBQPlateMainBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).pushReaction(PushReaction.IGNORE).instabreak(), SafariBanquetFoodProperties.RICH_BISON_BBQ_PLATE));
    public static final RegistrySupplier<Block> RICH_BISON_BBQ_PLATE_RIGHT = registerWithoutItem("rich_bison_bbq_plate_right", () -> new RichBisonBBQPlateBlock.BBQPlateRightBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).pushReaction(PushReaction.IGNORE).instabreak(), SafariBanquetFoodProperties.RICH_BISON_BBQ_PLATE));
    public static final RegistrySupplier<Item> MEAL_TOKEN = registerItem("meal_token", () -> new Item(getSettings()));
    public static final RegistrySupplier<Item> GRANNYS_GOURMET_GRIMOIRE = registerItem("grannys_gourmet_grimoire", () -> new GrannysGourmetGrimoire(getSettings()));

    public static void init() {
        ITEMS.register();
        BLOCKS.register();
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(settings -> {
        });
    }

    private static Item.Properties getFoodItemSettings(int nutrition, float saturationMod, MobEffect effect, int duration) {
        return new Item.Properties().food(new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturationMod).effect(new MobEffectInstance(effect, duration), 1.0f).build());
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return SafariBanquetUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, new SafariBanquetIdentifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return SafariBanquetUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, new SafariBanquetIdentifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return SafariBanquetUtil.registerItem(ITEMS, ITEM_REGISTRAR, new SafariBanquetIdentifier(path), itemSupplier);
    }
}
