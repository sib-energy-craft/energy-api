package com.github.sib_energy_craft.energy_api.consumer;

import com.github.sib_energy_craft.energy_api.EnergyOffer;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

/**
 * Energy consumer - awaiting energy offers and accept it if needed.<br/>
 * Destination point of energy offers.
 *
 * @since 0.0.1
 * @author sibmaks
 */
public interface EnergyConsumer {

    /**
     * Check is a consumer can consume offer from passed direction. <br/>
     * Can be usefully in case if consumer has supplying and consuming sides.
     *
     * @param direction consuming direction
     * @return true - can consume, false - otherwise
     */
    boolean isConsumeFrom(@NotNull Direction direction);

    /**
     * The method called every time when someone want to offer energy to consumer.<br/>
     * Assuming, that consumer not use offer immediately, but store it to use on it's one tick.
     *
     * @param energyOffer offered energy
     */
    void receiveOffer(@NotNull EnergyOffer energyOffer);
}
