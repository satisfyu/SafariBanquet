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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.farm_and_charm.core.block.FoodBlock;
import net.satisfy.farm_and_charm.core.item.GrandmothersRecipeBookItem;
import net.satisfy.farm_and_charm.core.item.food.EffectBlockItem;
import net.satisfy.farm_and_charm.core.item.food.EffectItem;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;
import net.satisfy.safaribanquet.SafariBanquet;
import net.satisfy.safaribanquet.core.block.BurritoBlock;
import net.satisfy.safaribanquet.core.block.HazelnutCakeBlock;
import net.satisfy.safaribanquet.core.block.RichBisonBBQPlateBlock;
import net.satisfy.safaribanquet.core.util.SafariBanquetIdentifier;
import net.satisfy.safaribanquet.core.util.SafariBanquetUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(SafariBanquet.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(SafariBanquet.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();


    public static final RegistrySupplier<Block> FOOD_STAMP_EXCHANCE_TABLE = registerWithItem("food_stamp_exchance_table", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Item> MINCED_CASSOWARY_MEAT = registerItem("minced_cassowary_meat", () -> new Item(getSettings().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.15F).build())));
    public static final RegistrySupplier<Item> SEASONED_CASSOWARY_MEAT = registerItem("seasoned_cassowary_meat", () -> new EffectItem(getFoodItemSettings(6, 0.6f, MobEffectRegistry.RESTED.get(), 1800), 600, true));
    public static final RegistrySupplier<Item> HAZELNUT_PIE_SLICE = registerItem("hazelnut_pie_slice", () -> new EffectItem(getFoodItemSettings(6, 0.75f, MobEffectRegistry.SATIATION.get(), 800), 800, false));
    public static final RegistrySupplier<Block> HAZELNUT_PIE = registerWithItem("hazelnut_pie", () -> new HazelnutCakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), HAZELNUT_PIE_SLICE));
    public static final RegistrySupplier<Block> VENISON_WITH_POTATOES_BLOCK = registerWithoutItem("venison_with_potatoes_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build()));
    public static final RegistrySupplier<Item> VENISON_WITH_POTATOES_ITEM = registerItem("venison_with_potatoes", () -> new EffectBlockItem(VENISON_WITH_POTATOES_BLOCK.get(), getFoodItemSettings(8, 0.8f, MobEffects.INVISIBILITY, 1200)));
    public static final RegistrySupplier<Block> TURKEY_ON_A_BED_OF_SHROOMS_BLOCK = registerWithoutItem("turkey_on_a_bed_of_shrooms_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(12).saturationMod(1.0F).build()));
    public static final RegistrySupplier<Item> TURKEY_ON_A_BED_OF_SHROOMS_ITEM = registerItem("turkey_on_a_bed_of_shrooms", () -> new EffectBlockItem(TURKEY_ON_A_BED_OF_SHROOMS_BLOCK.get(), getFoodItemSettings(12, 1.0f, MobEffectRegistry.SATIATION.get(), 3200)));
    public static final RegistrySupplier<Block> PORKCHOP_WITH_TRUFFLES_BLOCK = registerWithoutItem("porkchop_with_truffles_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 5, new FoodProperties.Builder().nutrition(14).saturationMod(1.2F).build()));
    public static final RegistrySupplier<Item> PORKCHOP_WITH_TRUFFLES_ITEM = registerItem("porkchop_with_truffles", () -> new EffectBlockItem(PORKCHOP_WITH_TRUFFLES_BLOCK.get(), getFoodItemSettings(14, 1.2f, MobEffectRegistry.SATIATION.get(), 4800)));
    public static final RegistrySupplier<Block> PELICAN_POTROAST_BLOCK = registerWithoutItem("pelican_potroast_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(10).saturationMod(0.9F).build()));
    public static final RegistrySupplier<Item> PELICAN_POTROAST_ITEM = registerItem("pelican_potroast", () -> new EffectBlockItem(PELICAN_POTROAST_BLOCK.get(), getFoodItemSettings(10, 0.9f, SBMobEffectRegistry.WATER_WALKING.get(), 3200)));
    public static final RegistrySupplier<Block> TURKEY_WITH_VEGETABLES_BLOCK = registerWithoutItem("turkey_with_vegetables_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 5, new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build()));
    public static final RegistrySupplier<Item> TURKEY_WITH_VEGETABLES_ITEM = registerItem("turkey_with_vegetables", () -> new EffectBlockItem(TURKEY_WITH_VEGETABLES_BLOCK.get(), getFoodItemSettings(8, 0.8f, MobEffectRegistry.FEAST.get(), 4800)));
    public static final RegistrySupplier<Block> PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_BLOCK = registerWithoutItem("pelican_in_lilipads_with_oat_patties_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 5, new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build()));
    public static final RegistrySupplier<Item> PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_ITEM = registerItem("pelican_in_lilipads_with_oat_patties", () -> new EffectBlockItem(PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_BLOCK.get(), getFoodItemSettings(8, 0.8f, MobEffectRegistry.SUSTENANCE.get(), 4800)));
    public static final RegistrySupplier<Block> BISON_BURGER_BLOCK = registerWithoutItem("bison_burger_block", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 5, new FoodProperties.Builder().nutrition(7).saturationMod(0.6F).build()));
    public static final RegistrySupplier<Item> BISON_BURGER_ITEM = registerItem("bison_burger", () -> new EffectBlockItem(BISON_BURGER_BLOCK.get(), getFoodItemSettings(7, 0.6f, SBMobEffectRegistry.FLAMEGUARD.get(), 2800)));
    public static final RegistrySupplier<Block> RICH_BISON_BBQ_PLATE_HEAD = registerWithoutItem("rich_bison_bbq_plate_head", () -> new RichBisonBBQPlateBlock.BBQPlateHeadBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).pushReaction(PushReaction.IGNORE).instabreak(), new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).effect(new MobEffectInstance(SBMobEffectRegistry.REPULSION.get(), 600, 0), 1.0F).build()));
    public static final RegistrySupplier<Block> RICH_BISON_BBQ_PLATE_HEAD_RIGHT = registerWithoutItem("rich_bison_bbq_plate_head_right", () -> new RichBisonBBQPlateBlock.BBQPlateHeadRightBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).pushReaction(PushReaction.IGNORE).instabreak(), new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).effect(new MobEffectInstance(SBMobEffectRegistry.REPULSION.get(), 600, 0), 1.0F).build()));
    public static final RegistrySupplier<Block> RICH_BISON_BBQ_PLATE_MAIN = registerWithItem("rich_bison_bbq_plate_main", () -> new RichBisonBBQPlateBlock.BBQPlateMainBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).pushReaction(PushReaction.IGNORE).instabreak(), new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).effect(new MobEffectInstance(SBMobEffectRegistry.REPULSION.get(), 600, 0), 1.0F).build()));
    public static final RegistrySupplier<Block> RICH_BISON_BBQ_PLATE_RIGHT = registerWithoutItem("rich_bison_bbq_plate_right", () -> new RichBisonBBQPlateBlock.BBQPlateRightBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).pushReaction(PushReaction.IGNORE).instabreak(), new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).effect(new MobEffectInstance(SBMobEffectRegistry.REPULSION.get(), 600, 0), 1.0F).build()));
    public static final RegistrySupplier<Item> FOOD_STAMP = registerItem("food_stamp", () -> new Item(getSettings().rarity(Rarity.UNCOMMON)));
    public static final RegistrySupplier<Block> BURRITO_FLATBREAD = registerWithItem("burrito_flatbread", () -> new BurritoBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistrySupplier<Item> COMBO_BURRITO = registerItem("combo_burrito", () -> new EffectItem(getFoodItemSettings(8, 0.7f, MobEffectRegistry.RESTED.get(), 3600), 3600, false));
    public static final RegistrySupplier<Item> CASSOWARY_BURRITO = registerItem("cassowary_burrito", () -> new EffectItem(getFoodItemSettings(7, 0.6f, MobEffectRegistry.RESTED.get(), 3600), 3600, false));
    public static final RegistrySupplier<Item> VEGETABLE_BURRITO = registerItem("vegetable_burrito", () -> new EffectItem(getFoodItemSettings(7, 0.6f, MobEffectRegistry.RESTED.get(), 3600), 3600, false));
    public static final RegistrySupplier<GrandmothersRecipeBookItem> HAZELNUT_PIE_RECIPE_BOOK = registerItem("hazelnut_pie_recipe_book", () -> new GrandmothersRecipeBookItem(new Item.Properties()));
    public static final RegistrySupplier<GrandmothersRecipeBookItem> PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_RECIPE_BOOK = registerItem("pelican_in_lilipads_with_oat_patties_recipe_book", () -> new GrandmothersRecipeBookItem(new Item.Properties()));
    public static final RegistrySupplier<GrandmothersRecipeBookItem> PELICAN_POTROAST_RECIPE_BOOK = registerItem("pelican_potroast_recipe_book", () -> new GrandmothersRecipeBookItem(new Item.Properties()));
    public static final RegistrySupplier<GrandmothersRecipeBookItem> PORKCHOP_WITH_TRUFFLES_RECIPE_BOOK = registerItem("porkchop_with_truffles_recipe_book", () -> new GrandmothersRecipeBookItem(new Item.Properties()));
    public static final RegistrySupplier<GrandmothersRecipeBookItem> SEASONED_CASSOWARY_MEAT_RECIPE_BOOK = registerItem("seasoned_cassowary_meat_recipe_book", () -> new GrandmothersRecipeBookItem(new Item.Properties()));
    public static final RegistrySupplier<GrandmothersRecipeBookItem> TURKEY_ON_A_BED_OF_SHROOMS_RECIPE_BOOK = registerItem("turkey_on_a_bed_of_shrooms_recipe_book", () -> new GrandmothersRecipeBookItem(new Item.Properties()));
    public static final RegistrySupplier<GrandmothersRecipeBookItem> TURKEY_WITH_VEGETABLES_RECIPE_BOOK = registerItem("turkey_with_vegetables_recipe_book", () -> new GrandmothersRecipeBookItem(new Item.Properties()));
    public static final RegistrySupplier<GrandmothersRecipeBookItem> VENISON_WITH_POTATOES_RECIPE_BOOK = registerItem("venison_with_potatoes_recipe_book", () -> new GrandmothersRecipeBookItem(new Item.Properties()));
    public static final Supplier<ItemStack> HAZELNUT_PIE_RECIPE_BOOK_STACK = () -> GrandmothersRecipeBookItem.createUnlockerForRecipes(HAZELNUT_PIE_RECIPE_BOOK.get(), "farm_and_charm:stove/hazelnut_pie");
    public static final Supplier<ItemStack> PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_RECIPE_BOOK_STACK = () -> GrandmothersRecipeBookItem.createUnlockerForRecipes(PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_RECIPE_BOOK.get(), "farm_and_charm:stove/pelican_in_lilipads_with_oat_patties");
    public static final Supplier<ItemStack> PELICAN_POTROAST_RECIPE_BOOK_STACK = () -> GrandmothersRecipeBookItem.createUnlockerForRecipes(PELICAN_POTROAST_RECIPE_BOOK.get(), "farm_and_charm:pot_cooking/pelican_potroast");
    public static final Supplier<ItemStack> PORKCHOP_WITH_TRUFFLES_RECIPE_BOOK_STACK = () -> GrandmothersRecipeBookItem.createUnlockerForRecipes(PORKCHOP_WITH_TRUFFLES_RECIPE_BOOK.get(), "farm_and_charm:roaster/porkchop_with_truffles");
    public static final Supplier<ItemStack> SEASONED_CASSOWARY_MEAT_RECIPE_BOOK_STACK = () -> GrandmothersRecipeBookItem.createUnlockerForRecipes(SEASONED_CASSOWARY_MEAT_RECIPE_BOOK.get(), "farm_and_charm:pot_cooking/seasoned_cassowary_meat");
    public static final Supplier<ItemStack> TURKEY_ON_A_BED_OF_SHROOMS_RECIPE_BOOK_STACK = () -> GrandmothersRecipeBookItem.createUnlockerForRecipes(TURKEY_ON_A_BED_OF_SHROOMS_RECIPE_BOOK.get(), "farm_and_charm:stove/turkey_on_a_bed_of_shrooms");
    public static final Supplier<ItemStack> TURKEY_WITH_VEGETABLES_RECIPE_BOOK_STACK = () -> GrandmothersRecipeBookItem.createUnlockerForRecipes(TURKEY_WITH_VEGETABLES_RECIPE_BOOK.get(), "farm_and_charm:roaster/turkey_with_vegetables");
    public static final Supplier<ItemStack> VENISON_WITH_POTATOES_RECIPE_BOOK_STACK = () -> GrandmothersRecipeBookItem.createUnlockerForRecipes(VENISON_WITH_POTATOES_RECIPE_BOOK.get(), "farm_and_charm:roaster/venison_with_potatoes");

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
