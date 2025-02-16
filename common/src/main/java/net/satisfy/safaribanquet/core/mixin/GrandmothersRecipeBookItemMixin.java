package net.satisfy.safaribanquet.core.mixin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.satisfy.farm_and_charm.core.item.GrandmothersRecipeBookItem;
import net.satisfy.farm_and_charm.core.recipe.RecipeUnlockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrandmothersRecipeBookItem.class)
public abstract class GrandmothersRecipeBookItemMixin {

    @Inject(method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;", at = @At("HEAD"), cancellable = true)
    private void checkPersistentUnlock(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                RecipeManager manager = level.getRecipeManager();
                List<Recipe<?>> recipes = new ArrayList<>();
                if (tag.contains("Recipes")) {
                    ListTag list = tag.getList("Recipes", 8);
                    for (int i = 0; i < list.size(); i++) {
                        ResourceLocation id = new ResourceLocation(list.getString(i));
                        manager.byKey(id).ifPresent(recipes::add);
                    }
                } else if (tag.contains("Recipe")) {
                    ResourceLocation id = new ResourceLocation(tag.getString("Recipe"));
                    manager.byKey(id).ifPresent(recipes::add);
                }
                if (!recipes.isEmpty()) {
                    for (Recipe<?> recipe : recipes) {
                        if (!RecipeUnlockManager.isRecipeLocked(serverPlayer, recipe.getId())) {
                            MutableComponent message = Component.translatable("tooltip.farm_and_charm.recipe_unlocker.already_unlocked")
                                    .withStyle(style -> style.withItalic(true).withColor(0xFF5555));
                            serverPlayer.displayClientMessage(message, false);
                            cir.setReturnValue(InteractionResultHolder.fail(stack));
                            return;
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;", at = @At(value = "INVOKE", target = "Lnet/satisfy/farm_and_charm/core/item/GrandmothersRecipeBookItem;spawnLevelUpEffect(Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.BEFORE))
    private void injectUnlock(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                RecipeManager manager = level.getRecipeManager();
                List<Recipe<?>> recipes = new ArrayList<>();
                if (tag.contains("Recipes")) {
                    ListTag list = tag.getList("Recipes", 8);
                    for (int i = 0; i < list.size(); i++) {
                        ResourceLocation id = new ResourceLocation(list.getString(i));
                        manager.byKey(id).ifPresent(recipes::add);
                    }
                } else if (tag.contains("Recipe")) {
                    ResourceLocation id = new ResourceLocation(tag.getString("Recipe"));
                    manager.byKey(id).ifPresent(recipes::add);
                }
                if (!recipes.isEmpty()) {
                    RecipeUnlockManager.unlockRecipes(serverPlayer, recipes);
                }
            }
        }
    }
}
