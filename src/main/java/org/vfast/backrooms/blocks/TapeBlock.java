package org.vfast.backrooms.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.function.Function;

public class TapeBlock extends Block {
    public static final MapCodec<TapeBlock> CODEC = simpleCodec(TapeBlock::new);

    @Override
    public MapCodec<TapeBlock> codec() {
        return CODEC;
    }

    public static final EnumProperty<Direction> FACE = EnumProperty.create("face", Direction.class);

    public static final EnumProperty<TapeBlock.Side> NORTH = EnumProperty.create("north", TapeBlock.Side.class);;
    public static final EnumProperty<TapeBlock.Side> EAST  = EnumProperty.create("east", TapeBlock.Side.class);;
    public static final EnumProperty<TapeBlock.Side> SOUTH = EnumProperty.create("south", TapeBlock.Side.class);;
    public static final EnumProperty<TapeBlock.Side> WEST  = EnumProperty.create("west", TapeBlock.Side.class);;

    public static final BooleanProperty FACE_OPPOSITE = BooleanProperty.create("opposite");

    private static final Map<Direction, Direction[]> TANGENTS_BY_FACE;
    private static final EnumProperty<TapeBlock.Side>[] SLOT_PROPS = new EnumProperty[]{ NORTH, EAST, SOUTH, WEST };

    static {
        Map<Direction, Direction[]> m = Maps.newEnumMap(Direction.class);
        m.put(Direction.DOWN,  new Direction[]{ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST });
        m.put(Direction.UP,    new Direction[]{ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST });
        m.put(Direction.NORTH, new Direction[]{ Direction.UP,    Direction.EAST, Direction.DOWN,  Direction.WEST });
        m.put(Direction.SOUTH, new Direction[]{ Direction.UP,    Direction.WEST, Direction.DOWN,  Direction.EAST });
        m.put(Direction.EAST,  new Direction[]{ Direction.UP,    Direction.NORTH, Direction.DOWN, Direction.SOUTH });
        m.put(Direction.WEST,  new Direction[]{ Direction.UP,    Direction.SOUTH, Direction.DOWN, Direction.NORTH });
        TANGENTS_BY_FACE = ImmutableMap.copyOf(m);
    }

    private static EnumProperty<TapeBlock.Side> slotProp(int slot) {
        return SLOT_PROPS[slot];
    }

    private static final Map<Direction, VoxelShape> DOT_SHAPES = buildDotShapes();

    private final Function<BlockState, VoxelShape> shapes;

    public TapeBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACE,  Direction.DOWN)
                        .setValue(NORTH, TapeBlock.Side.NONE)
                        .setValue(EAST,  TapeBlock.Side.NONE)
                        .setValue(SOUTH, TapeBlock.Side.NONE)
                        .setValue(WEST,  TapeBlock.Side.NONE)
                        .setValue(FACE_OPPOSITE, false)
        );

        this.shapes = this.getShapeForEachState(this::computeShape);

        this.defaultBlockState()
                .setValue(NORTH, TapeBlock.Side.SIDE)
                .setValue(EAST, TapeBlock.Side.SIDE)
                .setValue(SOUTH, TapeBlock.Side.SIDE)
                .setValue(WEST, TapeBlock.Side.SIDE)
                .setValue(FACE_OPPOSITE, false);
    }

    private static Map<Direction, VoxelShape> buildDotShapes() {
        VoxelShape floorDot = Block.box(3, 0,  3, 13,  1, 13);
        VoxelShape ceilDot  = Block.box(3, 15, 3, 13, 16, 13);
        VoxelShape northDot = Block.box(3,  3, 0, 13, 13,  1);
        VoxelShape southDot = Block.box(3,  3, 15, 13, 13, 16);
        VoxelShape westDot  = Block.box(0,  3, 3,  1, 13, 13);
        VoxelShape eastDot  = Block.box(15, 3, 3, 16, 13, 13);

        return ImmutableMap.<Direction, VoxelShape>builder()
                .put(Direction.DOWN,  floorDot)
                .put(Direction.UP,    ceilDot)
                .put(Direction.NORTH, northDot)
                .put(Direction.SOUTH, southDot)
                .put(Direction.WEST,  westDot)
                .put(Direction.EAST,  eastDot)
                .build();
    }

    private VoxelShape computeShape(BlockState state) {
        Direction face = state.getValue(FACE);
        VoxelShape shape = DOT_SHAPES.get(face);

        boolean isOpposite = state.getValue(FACE_OPPOSITE);
        if (isOpposite) {
            shape = Shapes.or(shape, DOT_SHAPES.get(face.getOpposite()));
        }

        Direction[] tangents = TANGENTS_BY_FACE.get(face);

        for (int slot = 0; slot < 4; slot++) {
            TapeBlock.Side side = state.getValue(slotProp(slot));
            if (side == TapeBlock.Side.NONE) continue;

            Direction armDir = tangents[slot];
            VoxelShape arm = buildArmShape(face, armDir, side == TapeBlock.Side.UP);

            if (arm != null) {
                shape = Shapes.or(shape, arm);
            }

            if (isOpposite) {
                VoxelShape armOpposite = buildArmShape(face.getOpposite(), armDir, false); // always flat cause it's done in `arm`
                if (armOpposite != null) {
                    shape = Shapes.or(shape, armOpposite);
                }
            }
        }

        return shape;
    }

    private static VoxelShape buildArmShape(Direction face, Direction armDir, boolean bendUp) {
        if (armDir == face || armDir == face.getOpposite()) return null;

        final double W = 3.0, E = 13.0;

        double x0, y0, z0, x1, y1, z1;

        switch (face) {
            case DOWN -> {
                y0 = 0; y1 = 1;
                switch (armDir) {
                    case NORTH -> { x0=W; x1=E; z0=0;  z1=8;  }
                    case SOUTH -> { x0=W; x1=E; z0=8;  z1=16; }
                    case WEST  -> { x0=0; x1=8; z0=W;  z1=E;  }
                    default    -> { x0=8; x1=16; z0=W; z1=E;  }
                }
                VoxelShape flat = Block.box(x0, y0, z0, x1, y1, z1);
                if (!bendUp) return flat;
                VoxelShape rise = buildRise(face, armDir);
                return rise == null ? flat : Shapes.or(flat, rise);
            }
            case UP -> {
                y0 = 15; y1 = 16;
                switch (armDir) {
                    case NORTH -> { x0=W; x1=E; z0=0;  z1=8;  }
                    case SOUTH -> { x0=W; x1=E; z0=8;  z1=16; }
                    case WEST  -> { x0=0; x1=8; z0=W;  z1=E;  }
                    default    -> { x0=8; x1=16; z0=W; z1=E;  }
                }
                VoxelShape flat = Block.box(x0, y0, z0, x1, y1, z1);
                if (!bendUp) return flat;
                VoxelShape rise = buildRise(face, armDir);
                return rise == null ? flat : Shapes.or(flat, rise);
            }
            case NORTH -> {
                z0 = 0; z1 = 1;
                switch (armDir) {
                    case WEST  -> { x0=0;  x1=8;  y0=W; y1=E; }
                    case EAST  -> { x0=8;  x1=16; y0=W; y1=E; }
                    case DOWN  -> { x0=W;  x1=E;  y0=0; y1=8; }
                    default    -> { x0=W;  x1=E;  y0=8; y1=16; }
                }
                VoxelShape flat = Block.box(x0, y0, z0, x1, y1, z1);
                if (!bendUp) return flat;
                VoxelShape rise = buildRise(face, armDir);
                return rise == null ? flat : Shapes.or(flat, rise);
            }
            case SOUTH -> {
                z0 = 15; z1 = 16;
                switch (armDir) {
                    case WEST  -> { x0=0;  x1=8;  y0=W; y1=E; }
                    case EAST  -> { x0=8;  x1=16; y0=W; y1=E; }
                    case DOWN  -> { x0=W;  x1=E;  y0=0; y1=8; }
                    default    -> { x0=W;  x1=E;  y0=8; y1=16; }
                }
                VoxelShape flat = Block.box(x0, y0, z0, x1, y1, z1);
                if (!bendUp) return flat;
                VoxelShape rise = buildRise(face, armDir);
                return rise == null ? flat : Shapes.or(flat, rise);
            }
            case WEST -> {
                x0 = 0; x1 = 1;
                switch (armDir) {
                    case NORTH -> { z0=0;  z1=8;  y0=W; y1=E; }
                    case SOUTH -> { z0=8;  z1=16; y0=W; y1=E; }
                    case DOWN  -> { z0=W;  z1=E;  y0=0; y1=8; }
                    default    -> { z0=W;  z1=E;  y0=8; y1=16; }
                }
                VoxelShape flat = Block.box(x0, y0, z0, x1, y1, z1);
                if (!bendUp) return flat;
                VoxelShape rise = buildRise(face, armDir);
                return rise == null ? flat : Shapes.or(flat, rise);
            }
            case EAST -> {
                x0 = 15; x1 = 16;
                switch (armDir) {
                    case NORTH -> { z0=0;  z1=8;  y0=W; y1=E; }
                    case SOUTH -> { z0=8;  z1=16; y0=W; y1=E; }
                    case DOWN  -> { z0=W;  z1=E;  y0=0; y1=8; }
                    default    -> { z0=W;  z1=E;  y0=8; y1=16; }
                }
                VoxelShape flat = Block.box(x0, y0, z0, x1, y1, z1);
                if (!bendUp) return flat;
                VoxelShape rise = buildRise(face, armDir);
                return rise == null ? flat : Shapes.or(flat, rise);
            }
            default -> { return null; }
        }
    }

    private static VoxelShape buildRise(Direction face, Direction armDir) {
        final double W = 3.0, E = 13.0;

        return switch (face) {
            case DOWN, UP -> switch (armDir) {
                case NORTH -> Block.box(W, 0, 0, E, 16, 1);
                case SOUTH -> Block.box(W, 0, 15, E, 16, 16);
                case WEST -> Block.box(0, 0, W,  1, 16,  E);
                case EAST -> Block.box(15, 0, W, 16, 16,  E);
                default -> null;
            };
            case NORTH, SOUTH -> switch (armDir) {
                case WEST -> Block.box(0, W, 0, 1,  E, 16);
                case EAST -> Block.box(15, W, 0, 16,  E, 16);
                case DOWN -> Block.box(W, 0, 0, E, 1, 16);
                case UP -> Block.box(W, 15, 0, E, 16, 16);
                default -> null;
            };
            case WEST, EAST -> switch (armDir) {
                case NORTH -> Block.box(0, W,  0,  16,  E,  1);
                case SOUTH -> Block.box(0, W, 15,  16,  E, 16);
                case DOWN -> Block.box(0,  0, W,  16,  1,  E);
                case UP -> Block.box(0, 15, W,  16, 16,  E);
                default -> null;
            };
        };
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapes.apply(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        Direction face = clickedFace.getOpposite();
        BlockState base = this.defaultBlockState().setValue(FACE, face);
        return this.getConnectionState(context.getLevel(), base, context.getClickedPos());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction face = state.getValue(FACE);
        BlockPos supportPos = pos.relative(face);
        BlockState supportState = level.getBlockState(supportPos);
        return supportState.isFaceSturdy(level, supportPos, face.getOpposite());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        Direction face = state.getValue(FACE);

        if (directionToNeighbour == face) {
            return state.canSurvive(level, pos) ? this.getConnectionState(level, state, pos) : Blocks.AIR.defaultBlockState();
        }

        return this.getConnectionState(level, state, pos);
    }

    private BlockState getConnectionState(BlockGetter level, BlockState state, BlockPos pos) {
        Direction face = state.getValue(FACE);
        boolean wasDot = isDot(state);

        state = this.getTapeConnections(level, this.defaultBlockState().setValue(FACE, face), pos);

        if (wasDot && isDot(state)) return state;

        boolean s0 = state.getValue(slotProp(0)).isConnected();
        boolean s1 = state.getValue(slotProp(1)).isConnected();
        boolean s2 = state.getValue(slotProp(2)).isConnected();
        boolean s3 = state.getValue(slotProp(3)).isConnected();

        boolean axis02Empty = !s0 && !s2;
        boolean axis13Empty = !s1 && !s3;

        if (!s0 && axis13Empty) state = state.setValue(slotProp(0), TapeBlock.Side.SIDE);
        if (!s2 && axis13Empty) state = state.setValue(slotProp(2), TapeBlock.Side.SIDE);
        if (!s1 && axis02Empty) state = state.setValue(slotProp(1), TapeBlock.Side.SIDE);
        if (!s3 && axis02Empty) state = state.setValue(slotProp(3), TapeBlock.Side.SIDE);

        return state;
    }

    private BlockState getTapeConnections(BlockGetter level, BlockState state, BlockPos pos) {
        Direction face = state.getValue(FACE);
        Direction[] tangents = TANGENTS_BY_FACE.get(face);

        for (int slot = 0; slot < 4; slot++) {
            Direction tangent = tangents[slot];
            TapeBlock.Side connection = getConnectingSide(level, pos, tangent, face);
            state = state.setValue(slotProp(slot), connection);
        }
        return state;
    }

    private TapeBlock.Side getConnectingSide(BlockGetter level, BlockPos pos, Direction dir, Direction face) {
        BlockPos neighbourPos = pos.relative(dir);
        BlockState neighbourState = level.getBlockState(neighbourPos);

        if (isTapeOnFace(neighbourState, face)) {
            return TapeBlock.Side.SIDE;
        }

        BlockPos cornerPos = neighbourPos.relative(face);
        BlockState cornerState = level.getBlockState(cornerPos);

        if (isTapeOnFace(cornerState, dir)) {
            return TapeBlock.Side.UP;
        }

        return TapeBlock.Side.NONE;
    }

    private static boolean isTapeOnFace(BlockState state, Direction face) {
        return state.getBlock() instanceof TapeBlock && state.getValue(FACE) == face;
    }

    private static boolean isDot(BlockState state) {
        return !state.getValue(NORTH).isConnected()
                && !state.getValue(SOUTH).isConnected()
                && !state.getValue(EAST).isConnected()
                && !state.getValue(WEST).isConnected();
    }

    private static boolean isCross(BlockState state) {
        return state.getValue(NORTH).isConnected()
                && state.getValue(SOUTH).isConnected()
                && state.getValue(EAST).isConnected()
                && state.getValue(WEST).isConnected();
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        Direction newFace = rotation.rotate(state.getValue(FACE));
        state = state.setValue(FACE, newFace);

        return switch (rotation) {
            case CLOCKWISE_180 -> state
                    .setValue(NORTH, state.getValue(SOUTH))
                    .setValue(EAST,  state.getValue(WEST))
                    .setValue(SOUTH, state.getValue(NORTH))
                    .setValue(WEST,  state.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> state
                    .setValue(NORTH, state.getValue(EAST))
                    .setValue(EAST,  state.getValue(SOUTH))
                    .setValue(SOUTH, state.getValue(WEST))
                    .setValue(WEST,  state.getValue(NORTH));
            case CLOCKWISE_90 -> state
                    .setValue(NORTH, state.getValue(WEST))
                    .setValue(EAST,  state.getValue(NORTH))
                    .setValue(SOUTH, state.getValue(EAST))
                    .setValue(WEST,  state.getValue(SOUTH));
            default -> state;
        };
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state
                    .setValue(NORTH, state.getValue(SOUTH))
                    .setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state
                    .setValue(EAST,  state.getValue(WEST))
                    .setValue(WEST,  state.getValue(EAST));
            default -> super.mirror(state, mirror);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE, NORTH, EAST, SOUTH, WEST, FACE_OPPOSITE);
    }

    public enum Side implements StringRepresentable {
        NONE("none"),
        SIDE("side"),
        UP("up");

        private final String name;

        Side(String name) {
            this.name = name;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        public boolean isConnected() {
            return this != NONE;
        }
    }
}