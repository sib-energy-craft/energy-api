package com.github.sib_energy_craft.energy_api;

import com.github.sib_energy_craft.energy_api.supplier.EnergySupplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The class represent offer of energy from supplier to any consumer.<br/>
 * By one offer several consumers can retrieve energy if supplier has it.
 *
 * @since 0.0.1
 * @author sibmaks
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class EnergyOffer implements Comparable<EnergyOffer> {
    @Getter
    private final EnergySupplier source;
    @Getter
    private final Energy energyAmount;
    private final Energy baseEnergyAmount;

    public EnergyOffer(@NotNull EnergySupplier source,
                       @NotNull Energy energyAmount) {
        this.source = source;
        this.energyAmount = energyAmount;
        this.baseEnergyAmount = energyAmount;
    }

    /**
     * Try to accept offer.<br/>
     * Source supplier called to energy supplying.<br/>
     * In case if supplier can't supply requested amount of energy, then false returned.
     *
     * @return true - offer accepted, false - otherwise
     */
    public boolean acceptOffer() {
        return source.supplyEnergy(baseEnergyAmount);
    }

    /**
     * Fork offer for a new one and reduce offered amount of energy.<br/>
     * If energy not enough to create new offer then null will be returned
     *
     * @param resistance amount of losing energy
     * @return forked offer
     */
    @Nullable
    public EnergyOffer fork(@NotNull BigDecimal resistance) {
        var energyAmount = this.energyAmount.subtract(resistance);
        if(energyAmount.compareTo(Energy.ZERO) <= 0) {
            return null;
        }
        return new EnergyOffer(source, energyAmount, baseEnergyAmount);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (EnergyOffer) o;
        return source.equals(that.source) && energyAmount.equals(that.energyAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, energyAmount);
    }

    @Override
    public int compareTo(@NotNull EnergyOffer o) {
        return energyAmount.compareTo(o.getEnergyAmount());
    }
}
