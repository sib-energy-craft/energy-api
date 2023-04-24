package com.github.sib_energy_craft.energy_api.supplier;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.EnergyOffer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Energy supplier - generate energy offers each tick, and supply energy by request.
 *
 * @since 0.0.1
 * @author sibmaks
 */
public interface EnergySupplier {

    /**
     * Proceed energy supplier logic on server tick
     *
     * @param blockEntity energy supplier block
     */
    default void tick(@NotNull BlockEntity blockEntity) {
        EnergySupplierTicker.tick(this, blockEntity);
    }

    /**
     * Get energy supplying directions of block
     *
     * @return not null set of directions
     */
    @NotNull
    Set<Direction> getSupplyingDirections();

    /**
     * Create energy offer, without actual energy lose
     *
     * @return energy offer
     */
    @NotNull
    EnergyOffer createOffer();

    /**
     * Supply passed energy amount
     *
     * @param energyAmount requested amount
     * @return true - all energy supplied, false - otherwise
     */
    boolean supplyEnergy(@NotNull Energy energyAmount);

}
