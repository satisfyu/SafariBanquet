package net.satisfy.safaribanquet.core.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class RepulsionEffect extends MobEffect {

    public RepulsionEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel && entity.level().getGameTime() % 400 == 0) {
            double radius = 6.0;
            List<Entity> enemies = entity.level().getEntities(entity, entity.getBoundingBox().inflate(radius), e -> e instanceof LivingEntity && e != entity);

            for (Entity enemy : enemies) {
                Vec3 direction = enemy.position().subtract(entity.position()).normalize().scale(4.0);
                enemy.setDeltaMovement(direction.x, 0.5, direction.z);
                enemy.hurtMarked = true;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
