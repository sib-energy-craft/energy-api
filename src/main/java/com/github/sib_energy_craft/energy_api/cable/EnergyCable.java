package com.github.sib_energy_craft.energy_api.cable;

import com.github.sib_energy_craft.energy_api.constants.Constants;
import com.github.sib_energy_craft.energy_api.EnergyLevel;
import com.github.sib_energy_craft.energy_api.consumer.EnergyConsumer;
import com.github.sib_energy_craft.energy_api.supplier.EnergySupplier;
import com.github.sib_energy_craft.energy_api.EnergyOffer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Energy cable - block that can transfer energy from {@link EnergySupplier} to {@link EnergyConsumer}.<br/>
 * The cable forwards all the most valuables offers to its neighbors and decrease offer amount on cable resistance.<br/>
 * Most valuable offer means that offer contains max more energy than offer from the same supplier.
 *
 * @since 0.0.1
 * @author sibmaks
 */
public interface EnergyCable extends EnergyConsumer {
    /**
     * The resistance of a cable is the amount of energy a packet loses as it travels along that cable.<br/>
     * The resistance precision should be {@link Constants#ENERGY_PRECISION}
     *
     * @return resistance
     */
    @NotNull
    BigDecimal getResistance();

    /**
     * Method should be called on every server tick to update state of wire.<br/>
     * The method has a default behavior and usually does not need to be overridden.
     *
     * @param blockEntity wire block entity
     */
    default void tick(@NotNull BlockEntity blockEntity) {
        EnergyCableTicker.tick(this, blockEntity);
    }

    /**
     * The method should return all offers received on previous tick.
     *
     * @return set of offers
     */
    @NotNull
    Set<EnergyOffer> retrieveUpcomingOffers();

    /**
     * The method returns the energy level of wire.<br/>
     * Specifies the maximum energy level that the wire can handle.<br/>
     * In case if offered energy amount is too big explode happened.
     *
     * @return wire energy level
     */
    @NotNull
    EnergyLevel getEnergyLevel();

    @Override
    default boolean isConsumeFrom(@NotNull Direction direction) {
        return true;
    }
}
