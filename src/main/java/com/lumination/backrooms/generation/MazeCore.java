package com.lumination.backrooms.generation;

import java.util.Random;

import com.lumination.backrooms.generation.type.ShapeType;
import com.lumination.backrooms.generation.type.StructureType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MazeCore extends GenerationCore {
    private ShapeType shape;
    private StructureType type;
    private int iterations;

    private Orientation next = null;

    public MazeCore(Block floor, Block walls, Block roof, int iterations) {
        super(floor, walls, roof);
        this.iterations = iterations;
    }

    @Override
    public void generate(World world, BlockPos origin) {
        super.generate(world, origin);
        this.shape = getShape();
        this.type = getType();

        for (int i = 0; i < iterations; i++) {
            // walls
            if (type == StructureType.BLOCKY && shape == ShapeType.BOX) {
                if (hasVariants == VariantType.WALLS || hasVariants == VariantType.ALL) {
                    fillVariants(walls, wallsVariants, origin.add(-sizeX / 2 - 1, 0, -sizeZ / 2 - 1), origin.add(sizeX / 2 + 1, sizeY + 1, sizeZ / 2 + 1), VariantType.WALLS);
                } else {
                    fill(walls.getDefaultState(), origin.add(-sizeX / 2 - 1, 0, -sizeZ / 2 - 1), origin.add(sizeX / 2 + 1, sizeY + 1, sizeZ / 2 + 1));
                }
                fill(Blocks.AIR.getDefaultState(), origin.add(-sizeX / 2, 1, -sizeZ / 2), origin.add(sizeX / 2, sizeY + 1, sizeZ / 2));
            }

            // floor
            if (hasVariants == VariantType.FLOOR || hasVariants == VariantType.ALL) {
                fillVariants(floor, floorVariants, origin.add(-sizeX / 2, 0, -sizeZ / 2), origin.add(sizeX / 2, 0, sizeZ / 2), VariantType.FLOOR);
            } else {
                fill(floor.getDefaultState(), origin.add(-sizeX / 2, 0, -sizeZ / 2), origin.add(sizeX / 2, 0, sizeZ / 2));
            }

            // roof
            if (hasVariants == VariantType.FLOOR || hasVariants == VariantType.ALL) {
                fillVariants(roof, roofVariants, origin.add(-sizeX / 2, sizeY + 1, -sizeZ / 2), origin.add(sizeX / 2, sizeY + 1, sizeZ / 2), VariantType.ROOF);
            } else {
                fill(roof.getDefaultState(), origin.add(-sizeX / 2 - 1, sizeY + 1, -sizeZ / 2 - 1), origin.add(sizeX / 2 + 1, sizeY + 1, sizeZ / 2 + 1));
            }

            // remove random wall and continue
            if (i != iterations) {
                this.next = Orientation.values()[new Random().nextInt(Orientation.values().length)];

                BlockPos a = toFill(next, origin).posA;
                BlockPos b = toFill(next, origin).posB;

                fill(Blocks.AIR.getDefaultState(), a, b);

                origin = updateOrigin(origin, next);
            }
        }
    }

    /**
     * Move the origin
     * @param origin
     * @param orientation
     * @return
     */
    private BlockPos updateOrigin(BlockPos origin, Orientation orientation) {
        switch (orientation) {
            case EAST -> {
                return origin.east((int) (getSize().z) + 1);
            }
            case WEST -> {
                return origin.west((int) (getSize().z) + 1);
            }
            case NORTH -> {
                return origin.north((int) (getSize().x) + 1);
            }
            case SOUTH -> {
                return origin.south((int) (getSize().x) + 1);
            }
        }
        return origin.north((int) (getSize().x) + 1);
    }

    public Scale3d toFill(Orientation orientation, BlockPos origin) {
        BlockPos start = null;
        BlockPos end = null;

        switch (orientation) {
            case EAST -> {
                start = origin.add(sizeX / 2, sizeY, sizeZ / 2);
                end = origin.add(sizeX / 2, 0, -sizeZ / 2);
            }
            case WEST -> {
                start = origin.add(-sizeX / 2, sizeY, sizeZ / 2);
                end = origin.add(-sizeX / 2, 0, -sizeZ / 2);
            }
            case NORTH -> {
                start = origin.add(sizeX / 2, sizeY, sizeZ / 2);
                end = origin.add(-sizeX / 2, 0, sizeZ / 2);
            }
            case SOUTH -> {
                start = origin.add(sizeX / 2, sizeY, -sizeZ / 2);
                end = origin.add(-sizeX / 2, 0, -sizeZ / 2);
            }
        }

        return new Scale3d(start, end);
    }
}
