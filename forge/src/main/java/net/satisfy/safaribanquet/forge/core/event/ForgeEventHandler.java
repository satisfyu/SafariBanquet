package net.satisfy.safaribanquet.forge.core.event;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.satisfy.safaribanquet.SafariBanquet;
import net.satisfy.safaribanquet.core.registry.ObjectRegistry;
import net.satisfy.safaribanquet.forge.core.world.entity.npc.ForgeVillagerTrades;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SafariBanquet.MOD_ID)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType().equals(ForgeVillagerTrades.FOOD_STAMP_TRADER.get())) {
            Map<Integer, List<VillagerTrades.ItemListing>> trades = new HashMap<>(event.getTrades());

            trades.computeIfAbsent(1, k -> new java.util.ArrayList<>()).add(new SellItemStackFactory(ObjectRegistry.SEASONED_CASSOWARY_MEAT_RECIPE_BOOK_STACK.get(), 6, 12, 2));
            trades.get(1).add(new SellItemStackFactory(ObjectRegistry.VENISON_WITH_POTATOES_RECIPE_BOOK_STACK.get(), 8, 12, 2));
            trades.get(1).add(new SellItemStackFactory(ObjectRegistry.TURKEY_WITH_VEGETABLES_RECIPE_BOOK_STACK.get(), 11, 12, 2));
            trades.get(1).add(new SellItemStackFactory(ObjectRegistry.SEASONED_CASSOWARY_MEAT_RECIPE_BOOK_STACK.get(), 6, 12, 2));

            trades.computeIfAbsent(2, k -> new java.util.ArrayList<>()).add(new SellItemStackFactory(ObjectRegistry.HAZELNUT_PIE_RECIPE_BOOK_STACK.get(), 7, 12, 2));
            trades.get(2).add(new SellItemStackFactory(ObjectRegistry.TURKEY_ON_A_BED_OF_SHROOMS_RECIPE_BOOK_STACK.get(), 9, 12, 2));
            trades.get(2).add(new SellItemStackFactory(ObjectRegistry.PELICAN_IN_LILIPADS_WITH_OAT_PATTIES_RECIPE_BOOK_STACK.get(), 10, 12, 2));
            trades.get(2).add(new SellItemStackFactory(ObjectRegistry.HAZELNUT_PIE_RECIPE_BOOK_STACK.get(), 7, 12, 2));

            trades.computeIfAbsent(3, k -> new java.util.ArrayList<>()).add(new SellItemStackFactory(ObjectRegistry.PORKCHOP_WITH_TRUFFLES_RECIPE_BOOK_STACK.get(), 14, 12, 4));
            trades.get(3).add(new SellItemStackFactory(ObjectRegistry.PELICAN_POTROAST_RECIPE_BOOK_STACK.get(), 8, 12, 4));
            trades.get(3).add(new SellItemStackFactory(ObjectRegistry.PORKCHOP_WITH_TRUFFLES_RECIPE_BOOK_STACK.get(), 14, 12, 4));


            event.getTrades().clear();
            event.getTrades().putAll(trades);
        }
    }

    static class SellItemStackFactory implements VillagerTrades.ItemListing {
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
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
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
