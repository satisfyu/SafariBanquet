package net.satisfy.safaribanquet.core.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.safaribanquet.core.registry.TagsRegistry;
import net.satisfy.safaribanquet.core.util.SafariBanquetUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"deprecation"})
public class HazelnutCakeBlock extends Block {
    public static final IntegerProperty CUTS = IntegerProperty.create("cuts", 0, 3);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Supplier<VoxelShape> fullShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.125, 0.5, 0.3125, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.5, 0.5, 0.3125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.125, 0.875, 0.3125, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.5, 0.875, 0.3125, 0.875), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> FULL_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, fullShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> threeShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.5, 0.5, 0.3125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.125, 0.875, 0.3125, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.5, 0.875, 0.3125, 0.875), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> THREE_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, threeShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> halfShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.5, 0.5, 0.3125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.5, 0.875, 0.3125, 0.875), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> HALF_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, halfShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> quarterShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.5, 0.875, 0.3125, 0.875), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> QUARTER_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, quarterShapeSupplier.get()));
        }
    });
    public final Supplier<Item> Slice;

    public HazelnutCakeBlock(Properties settings, Supplier<Item> slice) {
        super(settings);
        this.Slice = slice;
        this.registerDefaultState(this.defaultBlockState().setValue(CUTS, 0).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CUTS, FACING);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return getMaxCuts() - blockState.getValue(CUTS);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public ItemStack getPieSliceItem() {
        return new ItemStack(this.Slice.get());
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (!level.isClientSide && !player.isShiftKeyDown() && state.getValue(CUTS) == 0 && heldStack.isEmpty()) {
            Direction direction = player.getDirection().getOpposite();
            double xMotion = direction.getStepX() * 0.13;
            double yMotion = 0.35;
            double zMotion = direction.getStepZ() * 0.13;
            SafariBanquetUtil.spawnSlice(level, new ItemStack(this), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, xMotion, yMotion, zMotion);
            level.removeBlock(pos, false);
            return InteractionResult.SUCCESS;
        }

        if (player.isShiftKeyDown() && (heldStack.isEmpty() || heldStack.is(TagsRegistry.KNIVES))) {
            return this.consumeBite(level, pos, state, player);
        }
        if (!player.isShiftKeyDown() && heldStack.is(TagsRegistry.KNIVES)) {
            return cutSlice(level, pos, state, player);
        }

        return InteractionResult.PASS;
    }

    protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player playerIn) {
        if (!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            ItemStack sliceStack = this.getPieSliceItem();
            FoodProperties sliceFood = sliceStack.getItem().getFoodProperties();

            playerIn.getFoodData().eat(sliceStack.getItem(), sliceStack);
            if (this.getPieSliceItem().getItem().isEdible() && sliceFood != null) {
                for (Pair<MobEffectInstance, Float> pair : sliceFood.getEffects()) {
                    if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
                        playerIn.addEffect(new MobEffectInstance(pair.getFirst()));
                    }
                }
            }

            int cuts = state.getValue(CUTS);
            if (cuts < getMaxCuts() - 1) {
                level.setBlock(pos, state.setValue(CUTS, cuts + 1), 3);
            } else {
                level.destroyBlock(pos, false);
            }
            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            return InteractionResult.SUCCESS;
        }
    }

    protected InteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player) {
        int cuts = state.getValue(CUTS);
        if (cuts < getMaxCuts() - 1) {
            level.setBlock(pos, state.setValue(CUTS, cuts + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        Direction direction = player.getDirection().getOpposite();
        double xMotion = direction.getStepX() * 0.13;
        double yMotion = 0.35;
        double zMotion = direction.getStepZ() * 0.13;

        SafariBanquetUtil.spawnSlice(level, this.getPieSliceItem(), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, xMotion, yMotion, zMotion);
        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.PLAYERS, 0.75F, 0.75F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return SafariBanquetUtil.isFullAndSolid(levelReader, blockPos);
    }

    public int getMaxCuts() {
        return 4;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
        tooltip.add(Component.translatable("tooltip.safaribanquet.canbeplaced").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.safaribanquet.cake_1").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.translatable("tooltip.safaribanquet.cake_2").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.translatable("tooltip.safaribanquet.cake_3").withStyle(ChatFormatting.WHITE));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        int cuts = state.getValue(CUTS);
        Map<Direction, VoxelShape> shape = switch (cuts) {
            case 1 -> THREE_SHAPE;
            case 2 -> HALF_SHAPE;
            case 3 -> QUARTER_SHAPE;
            default -> FULL_SHAPE;
        };
        Direction direction = state.getValue(FACING);
        return shape.get(direction);
    }
}
