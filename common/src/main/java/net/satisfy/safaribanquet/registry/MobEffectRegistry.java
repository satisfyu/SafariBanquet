package net.satisfy.safaribanquet.registry;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.satisfy.safaribanquet.SafariBanquet;
import net.satisfy.safaribanquet.effect.FlameguardEffect;
import net.satisfy.safaribanquet.effect.RepulsionEffect;
import net.satisfy.safaribanquet.effect.WaterWalkingEffect;
import net.satisfy.safaribanquet.util.SafariBanquetIdentifier;

import java.util.function.Supplier;

public class MobEffectRegistry {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(SafariBanquet.MOD_ID, Registries.MOB_EFFECT);
    private static final Registrar<MobEffect> MOB_EFFECTS_REGISTRAR = MOB_EFFECTS.getRegistrar();

    public static final RegistrySupplier<MobEffect> FLAMEGUARD;
    public static final RegistrySupplier<MobEffect> REPULSION;
    public static final RegistrySupplier<MobEffect> WATER_WALKING;

    private static RegistrySupplier<MobEffect> registerEffect(String name, Supplier<MobEffect> effect) {
        if (Platform.isForge()) {
            return MOB_EFFECTS.register(name, effect);
        }
        return MOB_EFFECTS_REGISTRAR.register(new SafariBanquetIdentifier(name), effect);
    }

    public static void init() {
        MOB_EFFECTS.register();
    }

    static {
        FLAMEGUARD = registerEffect("flameguard", FlameguardEffect::new);
        REPULSION = registerEffect("repulsion", RepulsionEffect::new);
        WATER_WALKING = registerEffect("water_walking", WaterWalkingEffect::new);

    }
}
