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
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 4);
    private final FoodProperties foodComponent;

    public RichBisonBBQPlateBlock(Properties properties, FoodProperties foodComponent) {
        super(properties);
        this.foodComponent = foodComponent;
        this.registerDefaultState(this.defaultBlockState().setValue(BITES, 4).setValue(FACING, Direction.NORTH));
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
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(BITES, 4);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BITES);
    }

    private boolean isEdible(BlockState state) {
        return state.getValue(BITES) > 0;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            if (this.isEdible(state)) {
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
        return this.tryEat(world, pos, state, player);
    }

    private InteractionResult tryEat(Level world, BlockPos pos, BlockState state, Player player) {
        if (!this.isEdible(state) || !player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            player.getFoodData().eat(foodComponent.getNutrition(), foodComponent.getSaturationModifier());
            world.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.5f, world.getRandom().nextFloat() * 0.1f + 0.9f);
            world.gameEvent(player, GameEvent.EAT, pos);

            int bites = state.getValue(BITES);
            if (bites > 0) {
                world.setBlock(pos, state.setValue(BITES, bites - 1), 3);
                spawnBlockBreakParticles(world, pos, state);
            }

            if (areAllBitesZero(world, pos, state)) {
                destroyAllBlocks(world, pos, state);
            }

            return InteractionResult.SUCCESS;
        }
    }

    private void spawnBlockBreakParticles(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            return;
        }
        world.levelEvent(2001, pos, Block.getId(state));
    }

    private boolean areAllBitesZero(Level world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        BlockPos[] allPositions = getAllBlockPositions(pos, facing, state.getBlock());

        for (BlockPos blockPos : allPositions) {
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.getBlock() instanceof RichBisonBBQPlateBlock) {
                int bites = blockState.getValue(BITES);
                if (bites > 0) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private void destroyAllBlocks(Level world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        BlockPos[] allPositions = getAllBlockPositions(pos, facing, state.getBlock());

        for (BlockPos blockPos : allPositions) {
            world.destroyBlock(blockPos, false);
        }
    }

    private BlockPos[] getOtherBlockPositions(BlockPos pos, Direction facing, Block blockType) {
        if (blockType instanceof BBQPlateMainBlock) {
            BlockPos backPos = pos.relative(facing.getOpposite());
            BlockPos sidePos = pos.relative(facing.getCounterClockWise());
            BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
            return new BlockPos[]{backPos, sidePos, diagonalPos};
        } else if (blockType instanceof BBQPlateHeadBlock) {
            BlockPos mainPos = pos.relative(facing);
            BlockPos sidePos = pos.relative(facing.getCounterClockWise());
            BlockPos diagonalPos = sidePos.relative(facing);
            return new BlockPos[]{mainPos, sidePos, diagonalPos};
        } else if (blockType instanceof BBQPlateRightBlock) {
            BlockPos mainPos = pos.relative(facing.getClockWise());
            BlockPos backPos = pos.relative(facing.getOpposite());
            BlockPos diagonalPos = backPos.relative(facing.getClockWise());
            return new BlockPos[]{mainPos, backPos, diagonalPos};
        } else if (blockType instanceof BBQPlateHeadRightBlock) {
            BlockPos mainPos = pos.relative(facing.getClockWise().getOpposite());
            BlockPos backPos = pos.relative(facing.getOpposite());
            BlockPos sidePos = pos.relative(facing.getClockWise());
            return new BlockPos[]{mainPos, backPos, sidePos};
        } else {
            return new BlockPos[0];
        }
    }

    private BlockPos[] getAllBlockPositions(BlockPos pos, Direction facing, Block blockType) {
        BlockPos[] positions = new BlockPos[4];
        positions[0] = pos;
        BlockPos[] otherPositions = getOtherBlockPositions(pos, facing, blockType);
        System.arraycopy(otherPositions, 0, positions, 1, otherPositions.length);
        return positions;
    }

    public static class BBQPlateHeadBlock extends RichBisonBBQPlateBlock {
        private static final Supplier<VoxelShape> VoxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.875, 0.0625, 0.875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.0625, 0.0625, 0.1875, 0.6875, 0.5625, 0.8125), BooleanOp.OR);
            return shape;
        };
        public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, SafariBanquetUtil.rotateShape(Direction.NORTH, direction, VoxelShapeSupplier.get()));
            }
        });

        public BBQPlateHeadBlock(Properties properties, FoodProperties foodComponent) {
            super(properties, foodComponent);
            this.registerDefaultState(this.defaultBlockState().setValue(BITES, 4).setValue(FACING, Direction.NORTH));
        }

        @Override
        public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean bl) {
        }

        @Override
        public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }

        @Override
        public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
            if (!state.is(newState.getBlock())) {
                Direction facing = state.getValue(FACING);
                BlockPos mainPos = pos.relative(facing);
                BlockPos sidePos = pos.relative(facing.getCounterClockWise());
                BlockPos diagonalPos = sidePos.relative(facing);

                level.removeBlock(mainPos, false);
                level.removeBlock(sidePos, false);
                level.removeBlock(diagonalPos, false);
                super.onRemove(state, level, pos, newState, moved);
            }
        }

        @Override
        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction facing = state.getValue(FACING);
            return SHAPE.get(facing);
        }
    }

    public static class BBQPlateHeadRightBlock extends RichBisonBBQPlateBlock {
        private static final Supplier<VoxelShape> VoxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0.125, 0, 0, 1, 0.0625, 0.875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.0625, 0.0625, 0.9375, 0.3125, 0.8125), BooleanOp.OR);
            return shape;
        };
        public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, SafariBanquetUtil.rotateShape(Direction.NORTH, direction, VoxelShapeSupplier.get()));
            }
        });

        public BBQPlateHeadRightBlock(Properties properties, FoodProperties foodComponent) {
            super(properties, foodComponent);
            this.registerDefaultState(this.defaultBlockState().setValue(BITES, 4).setValue(FACING, Direction.NORTH));
        }

        @Override
        public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean bl) {
        }

        @Override
        public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }

        @Override
        public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
            if (!state.is(newState.getBlock())) {
                Direction facing = state.getValue(FACING);
                BlockPos mainPos = pos.relative(facing.getClockWise().getOpposite());
                BlockPos backPos = pos.relative(facing.getOpposite());
                BlockPos sidePos = pos.relative(facing.getClockWise());

                level.removeBlock(mainPos, false);
                level.removeBlock(backPos, false);
                level.removeBlock(sidePos, false);
                super.onRemove(state, level, pos, newState, moved);
            }
        }

        @Override
        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction facing = state.getValue(FACING);
            return SHAPE.get(facing);
        }
    }

    public static class BBQPlateMainBlock extends RichBisonBBQPlateBlock {
        public BBQPlateMainBlock(Properties properties, FoodProperties foodComponent) {
            super(properties, foodComponent);
            this.registerDefaultState(this.defaultBlockState().setValue(BITES, 4).setValue(FACING, Direction.NORTH));
        }

        @Override
        public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean bl) {
        }

        @Override
        public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }

        @Override
        public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
            Level level = context.getLevel();
            BlockPos mainPos = context.getClickedPos();
            BlockState state = super.getStateForPlacement(context);
            if (state == null) return null;
            Direction facing = state.getValue(FACING);
            BlockPos backPos = mainPos.relative(facing.getOpposite());
            BlockPos sidePos = mainPos.relative(facing.getCounterClockWise());
            BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
            if (!canPlace(level, backPos, sidePos, diagonalPos)) return null;
            return state.setValue(BITES, 4);
        }

        private boolean canPlace(Level level, BlockPos... positions) {
            for (BlockPos pos : positions) {
                if (!level.getBlockState(pos).canBeReplaced()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
            super.setPlacedBy(level, pos, state, entity, stack);
            if (level.isClientSide) return;
            Direction facing = state.getValue(FACING);
            BlockPos backPos = pos.relative(facing.getOpposite());
            BlockPos sidePos = pos.relative(facing.getCounterClockWise());
            BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
            if (!canPlace(level, backPos, sidePos, diagonalPos)) return;
            level.setBlock(backPos, ObjectRegistry.RICH_BISON_BBQ_PLATE_HEAD.get().defaultBlockState().setValue(FACING, facing).setValue(BITES, 4), 3);
            level.setBlock(sidePos, ObjectRegistry.RICH_BISON_BBQ_PLATE_RIGHT.get().defaultBlockState().setValue(FACING, facing).setValue(BITES, 4), 3);
            level.setBlock(diagonalPos, ObjectRegistry.RICH_BISON_BBQ_PLATE_HEAD_RIGHT.get().defaultBlockState().setValue(FACING, facing).setValue(BITES, 4), 3);
        }
    }

    public static class BBQPlateRightBlock extends RichBisonBBQPlateBlock {
        private static final Supplier<VoxelShape> shapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.125, 1, 0.0625, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.0625, 0.1875, 0.9375, 0.25, 0.9375), BooleanOp.OR);
            return shape;
        };
        public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, SafariBanquetUtil.rotateShape(Direction.NORTH, direction, shapeSupplier.get()));
            }
        });

        public BBQPlateRightBlock(Properties properties, FoodProperties foodComponent) {
            super(properties, foodComponent);
            this.registerDefaultState(this.defaultBlockState().setValue(BITES, 4).setValue(FACING, Direction.NORTH));
        }

        @Override
        public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
            if (!state.is(newState.getBlock())) {
                Direction facing = state.getValue(FACING);
                BlockPos mainPos = pos.relative(facing.getClockWise());
                BlockPos backPos = pos.relative(facing.getOpposite());
                BlockPos diagonalPos = backPos.relative(facing.getClockWise());

                level.removeBlock(mainPos, false);
                level.removeBlock(backPos, false);
                level.removeBlock(diagonalPos, false);
                super.onRemove(state, level, pos, newState, moved);
            }
        }

        @Override
        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction facing = state.getValue(FACING);
            return SHAPE.get(facing);
        }
    }
}
