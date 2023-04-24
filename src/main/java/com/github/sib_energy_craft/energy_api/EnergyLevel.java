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
    L1(0, 32),
    L2(32, 128),
    L3(128, 512),
    L4(512, 2048),
    L5(2048, 8192);

    public final int from;
    public final Energy fromBig;
    public final int to;
    public final Energy toBig;

    EnergyLevel(int from, int to) {
        this.from = from;
        this.to = to;
        this.fromBig = Energy.of(from);
        this.toBig = Energy.of(to);
    }
}
