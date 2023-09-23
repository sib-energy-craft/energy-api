package com.github.sib_energy_craft.energy_api;

/**
 * Enum present energy levels supported by modification.<br/>
 * Each of energy: consumer, supplier or cable should have own energy level.<br/>
 * In case than some of them received an energy packet exceeding the energy level, then blowing up should occur.
 *
 * @since 0.0.1
 * @author sibmaks
 */
public enum EnergyLevel {
    /**
     * 1st energy level
     */
    L1(0, 32),
    /**
     * 2nd energy level
     */
    L2(32, 128),
    /**
     * 3rd energy level
     */
    L3(128, 512),
    /**
     * 4th energy level
     */
    L4(512, 2048),
    /**
     * 5th energy level
     */
    L5(2048, 8192);

    /**
     * Left bound of energy level as energy
     */
    public final Energy from;
    /**
     * Right bound of energy level as energy
     */
    public final Energy to;

    EnergyLevel(int from, int to) {
        this.from = new Energy(from);
        this.to = new Energy(to);
    }
}
