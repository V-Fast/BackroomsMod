package org.vfast.backrooms.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class ShapeUtils {
    private static final double EPS = 1.0E-7;

    private ShapeUtils() {}

    public record AttachDirection(AttachFace face, Direction direction) {}

    /**
     * Rotates a shape from one AttachDirection to another.
     * The shape is assumed to lie inside the 0..1 block space (0..16 in Block.box units).
     */
    public static VoxelShape rotate(VoxelShape shape, AttachDirection from, AttachDirection to) {
        if (shape.isEmpty() || from.equals(to) || isFullCube(shape)) {
            return shape;
        }

        int fromX = xTurns(from.face());
        int fromY = yTurns(from.face(), from.direction());
        int toX = xTurns(to.face());
        int toY = yTurns(to.face(), to.direction());

        VoxelShape result = Shapes.empty();
        for (AABB box : shape.toAabbs()) {
            AABB rotated = box;
            rotated = rotY(rotated, -fromY);
            rotated = rotX(rotated, -fromX);
            rotated = rotX(rotated, toX);
            rotated = rotY(rotated, toY);

            result = Shapes.or(result, Shapes.box(clamp(rotated.minX), clamp(rotated.minY), clamp(rotated.minZ), clamp(rotated.maxX), clamp(rotated.maxY), clamp(rotated.maxZ)));
        }
        return result.optimize();
    }

    /** Quarter turns around X (vanilla "x" / 90). */
    private static int xTurns(AttachFace face) {
        return switch (face) {
            case FLOOR -> 0;
            case WALL -> 1;
            case CEILING -> 2;
        };
    }

    /** Quarter turns around Y (vanilla "y" / 90), matching vanilla lever/button blockstates. */
    private static int yTurns(AttachFace face, Direction dir) {
        int base = switch (dir) {
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> throw new IllegalArgumentException("AttachDirection direction must be horizontal, got: " + dir);
        };
        return face == AttachFace.CEILING ? (base + 2) & 3 : base;
    }

    /** One quarter turn clockwise seen from above: (x, z) -> (1 - z, x). North -> East. */
    private static AABB rotY(AABB b, int turns) {
        int t = ((turns % 4) + 4) % 4;
        for (int i = 0; i < t; i++) {
            b = new AABB(1 - b.maxZ, b.minY, b.minX, 1 - b.minZ, b.maxY, b.maxX);
        }
        return b;
    }

    /** One quarter turn like vanilla x=90: (y, z) -> (z, 1 - y). Floor -> Wall (attached to the south side for NORTH). */
    private static AABB rotX(AABB b, int turns) {
        int t = ((turns % 4) + 4) % 4;
        for (int i = 0; i < t; i++) {
            b = new AABB(b.minX, b.minZ, 1 - b.maxY, b.maxX, b.maxZ, 1 - b.minY);
        }
        return b;
    }

    private static double clamp(double v) {
        return Math.clamp(v, 0.0, 1.0);
    }

    /** A full 16x16x16 cube is symmetric under every rotation, so it is returned untouched. */
    private static boolean isFullCube(VoxelShape shape) {
        if (shape == Shapes.block()) return true;
        AABB b = shape.bounds();
        return shape.toAabbs().size() == 1 && Math.abs(b.minX) < EPS && Math.abs(b.minY) < EPS && Math.abs(b.minZ) < EPS && Math.abs(b.maxX - 1) < EPS && Math.abs(b.maxY - 1) < EPS && Math.abs(b.maxZ - 1) < EPS;
    }
}