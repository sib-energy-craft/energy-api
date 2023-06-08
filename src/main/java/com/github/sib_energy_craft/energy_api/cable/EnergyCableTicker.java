package com.github.sib_energy_craft.energy_api.cable;

import com.github.sib_energy_craft.energy_api.EnergyOffer;
import com.github.sib_energy_craft.energy_api.consumer.EnergyConsumer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Default implementation of energy cable tick algorithm
 *
 * @since 0.0.1
 * @author sibmaks
 */
final class EnergyCableTicker {
    private static final List<Direction> SUPPLYING_DIRECTIONS = Arrays.asList(
            Direction.UP, Direction.DOWN,
            Direction.NORTH, Direction.SOUTH,
            Direction.EAST, Direction.WEST
    );

    /**
     * The method update cable state, proceed incoming energy offers and forward it if needed.
     *
     * @param wire wire
     * @param serverWorld server world
     * @param blockEntity wire block entity
     */
    public static void tick(@NotNull EnergyCable wire,
                            @NotNull ServerWorld serverWorld,
                            @NotNull BlockEntity blockEntity) {
        var energyOffers = wire.retrieveUpcomingOffers();

        for (var energyOffer : energyOffers.values()) {
            var source = energyOffer.getSource();
            if(source.isSupplierDead()) {
                continue;
            }
            if (assertOffer(wire, blockEntity, serverWorld, energyOffer)) {
                return;
            }

            forwardOffer(wire, blockEntity, serverWorld, energyOffer);
        }
    }

    private static boolean assertOffer(@NotNull EnergyCable wire,
                                       @NotNull BlockEntity blockEntity,
                                       @NotNull ServerWorld serverWorld,
                                       @NotNull EnergyOffer energyOffer) {
        var wireEnergyLevel = wire.getEnergyLevel();
        if (wireEnergyLevel.toBig.compareTo(energyOffer.getEnergyAmount()) < 0) {
            if (energyOffer.acceptOffer()) {
                var pos = blockEntity.getPos();
                serverWorld.breakBlock(pos, false);
                return true;
            }
        }
        return false;
    }

    private static void forwardOffer(@NotNull EnergyCable wire,
                                     @NotNull BlockEntity blockEntity,
                                     @NotNull World world,
                                     @NotNull EnergyOffer energyOffer) {
        var resistance = wire.getResistance();
        var forked = energyOffer.fork(resistance);
        if (forked == null) {
            return;
        }

        var pos = blockEntity.getPos();
        for (var direction : SUPPLYING_DIRECTIONS) {
            var neighborPos = pos.offset(direction);
            var neighbor = world.getBlockEntity(neighborPos);
            if (!(neighbor instanceof EnergyConsumer consumer)) {
                continue;
            }
            var opposite = direction.getOpposite();
            if (consumer.isConsumeFrom(opposite)) {
                consumer.receiveOffer(forked);
            }
        }
    }

}
