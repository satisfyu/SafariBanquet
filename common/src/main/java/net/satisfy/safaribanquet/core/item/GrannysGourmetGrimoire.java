package net.satisfy.safaribanquet.core.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class GrannysGourmetGrimoire extends Item {

    public GrannysGourmetGrimoire(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!world.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Definiere den Advancement, der die Rezepte freischaltet
            ResourceLocation advancementId = new ResourceLocation("safaribanquet", "unlock_hazelnut_pie");
            Advancement advancement = serverPlayer.getServer().getAdvancements().getAdvancement(advancementId);

            if (advancement != null) {
                AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
                if (!progress.isDone()) {
                    for (String criterion : advancement.getCriteria().keySet()) {
                        serverPlayer.getAdvancements().award(advancement, criterion);
                    }
                }
            }


        }

        return InteractionResultHolder.success(itemStack);
    }
}
