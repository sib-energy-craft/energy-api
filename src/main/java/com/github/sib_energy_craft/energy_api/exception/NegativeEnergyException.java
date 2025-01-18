package com.github.sib_energy_craft.energy_api.exception;

/**
 * This exception is thrown when a negative amount of energy is requested.
 *
 * @author sibmaks
 * @since 0.2.1
 */
public class NegativeEnergyException extends IllegalStateException {
    /**
     * Default constructor, which use static message
     */
    public NegativeEnergyException() {
        super("Energy can't be negative!");
    }
}
