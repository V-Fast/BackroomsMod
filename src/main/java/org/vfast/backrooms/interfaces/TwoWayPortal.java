package org.vfast.backrooms.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.attachments.BackroomsAttachments;
import org.vfast.backrooms.attachments.PortalLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A {@link LevelPortal} that remembers where it came from, like a `NetherPortal`: leaving through the portal it generated
 * brings the entity back to the portal it entered from.
 * <p>
 * In `getPortalDestination`, first call {@link #findLinkedExit}. If it returns `null` there is no live link, so pick a
 * position as usual and, when travelling to a new level, call {@link #linkPortals} with it.
 */
public interface TwoWayPortal extends LevelPortal {
    /**
     * @param from  level the entity is leaving
     * @param to    level the entity is going to
     * @param entry the portal block the entity is inside of
     * @return where to put the entity in front of the portal linked to `entry`, or `null` if there is none (anymore)
     */
    default @Nullable SpawnLocation findLinkedExit(ServerLevel from, ServerLevel to, BlockPos entry) {
        // a portal can be several blocks tall, the entity may be inside any of them
        for (BlockPos candidate : new BlockPos[]{entry, entry.below(), entry.above()}) {
            Optional<PortalLink> link = this.getLinks(from).stream().filter(l -> l.from().equals(candidate)).findFirst();
            if (link.isEmpty() || !link.get().to().dimension().equals(to.dimension())) {
                continue;
            }

            BlockPos target = link.get().to().pos();
            BlockState targetState = to.getBlockState(target);
            if (targetState.getBlock() != this) {
                this.removeLink(from, candidate); // portal was broken
                continue;
            }

            Direction facing = targetState.getOptionalValue(HorizontalDirectionalBlock.FACING).orElse(Direction.NORTH);
            return new SpawnLocation(this.exitPosition(to, target, facing), facing.toYRot());
        }

        return null;
    }

    /**
     * Links `entry` with the portal block that was placed behind the arrival position (see
     * {@link LevelPortal#selectStartPosition}). Does nothing if no portal block was placed there.
     */
    default void linkPortals(ServerLevel from, BlockPos entry, ServerLevel to, SpawnLocation arrival) {
        Direction back = Direction.fromYRot(arrival.yRot()).getOpposite();
        BlockPos generated = arrival.position().relative(back);
        if (to.getBlockState(generated).getBlock() != this) {
            return;
        }

        this.addLink(from, new PortalLink(entry, GlobalPos.of(to.dimension(), generated)));
        this.addLink(to, new PortalLink(generated, GlobalPos.of(from.dimension(), entry)));
    }

    /** Standing spot in front of the portal, then on its other sides, else inside of it */
    private BlockPos exitPosition(ServerLevel level, BlockPos portal, Direction facing) {
        List<Direction> sides = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
        sides.remove(facing);
        sides.addFirst(facing);

        for (Direction side : sides) {
            BlockPos position = portal.relative(side);
            if (LevelPortal.canStandAt(position, level)) {
                return position;
            }
        }

        return portal;
    }

    private List<PortalLink> getLinks(ServerLevel level) {
        List<PortalLink> links = level.getAttached(BackroomsAttachments.PORTAL_LINKS);
        return links == null ? List.of() : links;
    }

    private void addLink(ServerLevel level, PortalLink link) {
        List<PortalLink> links = new ArrayList<>(this.getLinks(level));
        links.removeIf(l -> l.from().equals(link.from()));
        links.add(link);
        level.setAttached(BackroomsAttachments.PORTAL_LINKS, links);
    }

    private void removeLink(ServerLevel level, BlockPos from) {
        List<PortalLink> links = new ArrayList<>(this.getLinks(level));
        if (links.removeIf(l -> l.from().equals(from))) {
            level.setAttached(BackroomsAttachments.PORTAL_LINKS, links);
        }
    }
}
