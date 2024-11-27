package net.satisfy.safaribanquet.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.safaribanquet.registry.ObjectRegistry;
import net.satisfy.safaribanquet.util.SafariBanquetUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class RichBisonBBQPlateBlock extends HorizontalDirectionalBlock {
    public static final IntegerProperty BITES;
    private final int maxBites;
    private final FoodProperties foodComponent;

    public RichBisonBBQPlateBlock(Properties properties, int maxBites, FoodProperties foodComponent) {
        super(properties);

        this.maxBites = maxBites;
        this.foodComponent = foodComponent;
        this.registerDefaultState(this.defaultBlockState().setValue(BITES, 0));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        if (this instanceof BBQPlateMainBlock) {
            return new ItemStack(ObjectRegistry.RICH_BISON_BBQ_PLATE_MAIN.get());
        }
        return super.getCloneItemStack(getter, pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, BITES);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (world.isClientSide) {
            if (tryEat(world, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (itemStack.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }
        return tryEat(world, pos, state, player);
    }

    private InteractionResult tryEat(LevelAccessor world, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            player.getFoodData().eat(foodComponent.getNutrition(), foodComponent.getSaturationModifier());
            world.playSound(null, pos, SoundEvents.FOX_EAT, SoundSource.PLAYERS, 0.5f, world.getRandom().nextFloat() * 0.1f + 0.9f);
            world.gameEvent(player, GameEvent.EAT, pos);

            int bites = state.getValue(BITES);
            if (bites < maxBites - 1) {
                world.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else {
                world.destroyBlock(pos, false);
                world.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    static {
        BITES = IntegerProperty.create("bites", 0, 3);
    }

    public static class BBQPlateHeadBlock extends RichBisonBBQPlateBlock {
        public static final IntegerProperty BITES;

        private static final Supplier<VoxelShape> bottomVoxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.8125, 1, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0, 0.875, 1, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0, 0, 0.8125, 0.8125, 1, 0.875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 0.9375, 1, 0.0625), BooleanOp.OR);
            return shape;
        };
        public static final Map<Direction, VoxelShape> BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, SafariBanquetUtil.rotateShape(Direction.NORTH, direction, bottomVoxelShapeSupplier.get()));
            }
        });

        public BBQPlateHeadBlock(Properties properties, int maxBites, FoodProperties foodComponent) {
            super(properties, maxBites, foodComponent);
            this.registerDefaultState(this.defaultBlockState().setValue(BITES, 0));
        }


        @Override
        public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        }

        public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
            return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        }

        @Override
        public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
            Direction facing = blockState.getValue(FACING);
            BlockPos front = blockPos.relative(facing);
            BlockPos sidePos = blockPos.relative(facing.getCounterClockWise());
            BlockPos diagonalPos = sidePos.relative(facing);

            level.removeBlock(front, false);
            level.removeBlock(sidePos, false);
            level.removeBlock(diagonalPos, false);
            level.removeBlock(blockPos, false);

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
        }

        @Override
        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction facing = state.getValue(FACING);
            return BOTTOM_SHAPE.get(facing);
        }

        static {
            BITES = IntegerProperty.create("bites", 0, 3);
        }
    }

    public static class BBQPlateHeadRightBlock extends RichBisonBBQPlateBlock {
        public static final IntegerProperty BITES;

        private static final Supplier<VoxelShape> bottomVoxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0, 0, 0.8125, 0.1875, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.125, 0, 0, 0.1875, 1, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.8125, 1, 1, 0.875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0, 0.125, 1, 0.0625), BooleanOp.OR);
            return shape;
        };
        public static final Map<Direction, VoxelShape> BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, SafariBanquetUtil.rotateShape(Direction.NORTH, direction, bottomVoxelShapeSupplier.get()));
            }
        });

        public BBQPlateHeadRightBlock(Properties properties, int maxBites, FoodProperties foodComponent) {
            super(properties, maxBites, foodComponent);
            this.registerDefaultState(this.defaultBlockState().setValue(BITES, 0));
        }

        @Override
        public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        }

        public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
            return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
        }

        @Override
        public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
            Direction facing = blockState.getValue(FACING);
            BlockPos front = blockPos.relative(facing);
            BlockPos sidePos = blockPos.relative(facing.getClockWise());
            BlockPos diagonalPos = sidePos.relative(facing);

            level.removeBlock(front, false);
            level.removeBlock(sidePos, false);
            level.removeBlock(diagonalPos, false);
            level.removeBlock(blockPos, false);

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }

        @Override
        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction facing = state.getValue(FACING);
            return BOTTOM_SHAPE.get(facing);
        }

        static {
            BITES = IntegerProperty.create("bites", 0, 3);
        }
    }

    public static class BBQPlateMainBlock extends RichBisonBBQPlateBlock {
        public static final IntegerProperty BITES;

        private static final Supplier<VoxelShape> bottomVoxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0, 1, 1, 0.1875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.1875, 0.875, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.9375, 0.9375, 1, 1), BooleanOp.OR);
            return shape;
        };
        public static final Map<Direction, VoxelShape> BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, SafariBanquetUtil.rotateShape(Direction.NORTH, direction, bottomVoxelShapeSupplier.get()));
            }
        });

        public BBQPlateMainBlock(Properties properties, int maxBites, FoodProperties foodComponent) {
            super(properties, maxBites, foodComponent);
            this.registerDefaultState(this.defaultBlockState().setValue(BITES, 0));
        }

        @Override
        public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        }

        public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
            return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
        }

        @Override
        public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
            Direction facing = blockState.getValue(FACING);
            BlockPos backPos = blockPos.relative(facing.getOpposite());
            BlockPos sidePos = blockPos.relative(facing.getCounterClockWise());
            BlockPos diagonalPos = sidePos.relative(facing.getOpposite());

            level.removeBlock(backPos, false);
            level.removeBlock(sidePos, false);
            level.removeBlock(diagonalPos, false);
            level.removeBlock(blockPos, false);

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
            Level level = blockPlaceContext.getLevel();
            BlockPos mainPos = blockPlaceContext.getClickedPos();
            BlockState blockState = super.getStateForPlacement(blockPlaceContext);
            if (blockState == null) return null;
            Direction facing = blockState.getValue(FACING);
            BlockPos backPos = mainPos.relative(facing.getOpposite());
            BlockPos sidePos = mainPos.relative(facing.getCounterClockWise());
            BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
            BlockPos topPos = diagonalPos.above();
            boolean placeable = canPlace(level, backPos, sidePos, diagonalPos, topPos);
            return placeable ? blockState : null;
        }

        @Override
        public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
            super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
            if (level.isClientSide) return;
            Direction facing = blockState.getValue(FACING);
            BlockPos backPos = blockPos.relative(facing.getOpposite());
            BlockPos sidePos = blockPos.relative(facing.getCounterClockWise());
            BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
            BlockPos topPos = diagonalPos.above();
            if (!canPlace(level, backPos, sidePos, diagonalPos, topPos)) return;
            level.setBlock(backPos, ObjectRegistry.RICH_BISON_BBQ_PLATE_HEAD.get().defaultBlockState().setValue(FACING, facing), 3);
            level.setBlock(sidePos, ObjectRegistry.RICH_BISON_BBQ_PLATE_RIGHT.get().defaultBlockState().setValue(FACING, facing), 3);
            level.setBlock(diagonalPos, ObjectRegistry.RICH_BISON_BBQ_PLATE_HEAD_RIGHT.get().defaultBlockState().setValue(FACING, facing), 3);
        }

        private boolean canPlace(Level level, BlockPos... blockPoses) {
            for (BlockPos blockPos : blockPoses) {
                if (!level.getBlockState(blockPos).isAir()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction facing = state.getValue(FACING);
            return BOTTOM_SHAPE.get(facing);
        }

        static {
            BITES = IntegerProperty.create("bites", 0, 3);
        }
    }

    public static class BBQPlateRightBlock extends RichBisonBBQPlateBlock {
        public static final IntegerProperty BITES;

        private static final Supplier<VoxelShape> shapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.1875, 1, 0.1875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.1875, 0.1875, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.9375, 0.125, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 1, 1, 0.1875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0.1875, 0.1875, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.8125, 0.1875, 0.875, 0.875, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.875, 0.8125, 0.1875, 1, 0.9375, 1), BooleanOp.OR);
            return shape;
        };
        public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, SafariBanquetUtil.rotateShape(Direction.NORTH, direction, shapeSupplier.get()));
            }
        });

        public BBQPlateRightBlock(Properties properties, int maxBites, FoodProperties foodComponent) {
            super(properties, maxBites, foodComponent);
            this.registerDefaultState(this.defaultBlockState().setValue(BITES, 0));
        }

        @Override
        public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
            Direction facing = blockState.getValue(FACING);
            BlockPos backPos = blockPos.relative(facing.getOpposite());
            BlockPos sidePos = blockPos.relative(facing.getClockWise());
            BlockPos diagonalPos = sidePos.relative(facing.getOpposite());

            level.removeBlock(backPos, false);
            level.removeBlock(sidePos, false);
            level.removeBlock(diagonalPos, false);
            level.removeBlock(blockPos, false);

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
        }

        @Override
        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction facing = state.getValue(FACING);
            return SHAPE.get(facing);
        }

        static {
            BITES = IntegerProperty.create("bites", 0, 3);
        }
    }
}
