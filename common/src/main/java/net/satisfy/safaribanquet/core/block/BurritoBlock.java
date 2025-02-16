package net.satisfy.safaribanquet.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.entity.player.Player;
import net.satisfy.safaribanquet.core.registry.ObjectRegistry;
import net.satisfy.safaribanquet.core.registry.TagsRegistry;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class BurritoBlock extends Block {
    public static final BooleanProperty MEAT = BooleanProperty.create("meat");
    public static final BooleanProperty VEGETABLES = BooleanProperty.create("vegetables");

    private static final VoxelShape SHAPE = Shapes.box(1.0 / 16.0, 0.0, 1.0 / 16.0, 15.0 / 16.0, 14.0 / 16.0, 15.0 / 16.0);

    public BurritoBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MEAT, false).setValue(VEGETABLES, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MEAT, VEGETABLES);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(ObjectRegistry.SEASONED_CASSOWARY_MEAT.get())) {
            world.setBlock(pos, state.setValue(MEAT, true), 3);
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            return InteractionResult.SUCCESS;
        } else if (heldItem.is(TagsRegistry.CABBAGE)) {
            world.setBlock(pos, state.setValue(VEGETABLES, true), 3);
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown()) {
            if (state.getValue(MEAT) && state.getValue(VEGETABLES)) {
                popResource(world, pos, new ItemStack(ObjectRegistry.COMBO_BURRITO.get()));
                world.levelEvent(2001, pos, Block.getId(state));
                world.removeBlock(pos, false);
                return InteractionResult.SUCCESS;
            } else if (state.getValue(MEAT)) {
                popResource(world, pos, new ItemStack(ObjectRegistry.CASSOWARY_BURRITO.get()));
                world.levelEvent(2001, pos, Block.getId(state));
                world.removeBlock(pos, false);
                return InteractionResult.SUCCESS;
            } else if (state.getValue(VEGETABLES)) {
                popResource(world, pos, new ItemStack(ObjectRegistry.VEGETABLE_BURRITO.get()));
                world.levelEvent(2001, pos, Block.getId(state));
                world.removeBlock(pos, false);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }
}
