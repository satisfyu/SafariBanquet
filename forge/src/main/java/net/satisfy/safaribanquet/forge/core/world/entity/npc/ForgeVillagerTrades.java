package net.satisfy.safaribanquet.forge.core.world.entity.npc;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.safaribanquet.SafariBanquet;
import net.satisfy.safaribanquet.core.registry.ObjectRegistry;

public class ForgeVillagerTrades {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, SafariBanquet.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, SafariBanquet.MOD_ID);

    public static final RegistryObject<PoiType> FOOD_STAMP_TRADER_POI = POI_TYPES.register("food_stamp_trader_poi", () ->
            new PoiType(ImmutableSet.copyOf(ObjectRegistry.FOOD_STAMP_EXCHANCE_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final RegistryObject<VillagerProfession> FOOD_STAMP_TRADER = VILLAGER_PROFESSIONS.register("food_stamp_trader", () ->
            new VillagerProfession("food_stamp_trader", x -> x.get() == FOOD_STAMP_TRADER_POI.get(), x -> x.get() == FOOD_STAMP_TRADER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_SHEPHERD));


    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
