package net.satisfy.safaribanquet.fabric.core.world.entity.npc;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.satisfy.safaribanquet.core.registry.ObjectRegistry;
import net.satisfy.safaribanquet.core.util.SafariBanquetIdentifier;

@SuppressWarnings("unused, deprecation")
public class VillagerTrades {

    private static final SafariBanquetIdentifier FOOD_STAMP_TRADER_POI_IDENTIFIER = new SafariBanquetIdentifier("food_stamp_trader_poi");
    public static final PoiType FOOD_STAMP_TRADER_POI = PointOfInterestHelper.register(
            FOOD_STAMP_TRADER_POI_IDENTIFIER, 1, 12, ObjectRegistry.FOOD_STAMP_EXCHANCE_TABLE.get()
    );

    public static final VillagerProfession FOOD_STAMP_TRADER = Registry.register(
            BuiltInRegistries.VILLAGER_PROFESSION,
            new ResourceLocation("safaribanquet", "food_stamp_trader"),
            VillagerProfessionBuilder.create()
                    .id(new ResourceLocation("safaribanquet", "food_stamp_trader"))
                    .workstation(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, FOOD_STAMP_TRADER_POI_IDENTIFIER))
                    .build()
    );

    public static void init() {
        TradeOfferHelper.registerVillagerOffers(FOOD_STAMP_TRADER, 1, factories -> {
            factories.add(new SellItemStackFactory(ObjectRegistry.SEASONED_CASSOWARY_MEAT_RECIPE_BOOK_STACK.get(), 6, 12, 2));
            factories.add(new SellItemStackFactory(ObjectRegistry.VENISON_WITH_POTATOES_RECIPE_BOOK_STACK.get(), 8, 12, 2));
            factories.add(new SellItemStackFactory(ObjectRegistry.TURKEY_WITH_VEGETABLES_RECIPE_BOOK_STACK.get(), 11, 12, 2));
        });

        TradeOfferHelper.registerVillagerOffers(FOOD_STAMP_TRADER, 2, factories -> {
            factories.add(new SellItemStackFactory(ObjectRegistry.HAZELNUT_PIE_RECIPE_BOOK_STACK.get(), 7, 12, 2));
            factories.add(new SellItemStackFactory(ObjectRegistry.TURKEY_ON_A_BED_OF_SHROOMS_RECIPE_BOOK_STACK.get(), 9, 12, 2));
            factories.add(new SellItemStackFactory(ObjectRegistry.PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_RECIPE_BOOK_STACK.get(), 10, 12, 2));
        });

        TradeOfferHelper.registerVillagerOffers(FOOD_STAMP_TRADER, 3, factories -> {
            factories.add(new SellItemStackFactory(ObjectRegistry.PORKCHOP_WITH_TRUFFLES_RECIPE_BOOK_STACK.get(), 14, 12, 4));
            factories.add(new SellItemStackFactory(ObjectRegistry.PELICAN_POTROAST_RECIPE_BOOK_STACK.get(), 8, 12, 4));
        });
    }

    static class SellItemStackFactory implements net.minecraft.world.entity.npc.VillagerTrades.ItemListing {
        private final ItemStack stack;
        private final int price;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellItemStackFactory(ItemStack stack, int price, int maxUses, int experience) {
            this.stack = stack;
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            return new MerchantOffer(
                    new ItemStack(ObjectRegistry.FOOD_STAMP.get(), this.price),
                    this.stack.copy(),
                    this.maxUses,
                    this.experience,
                    this.multiplier
            );
        }
    }
}
