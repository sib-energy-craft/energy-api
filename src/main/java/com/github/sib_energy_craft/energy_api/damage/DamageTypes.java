package com.github.sib_energy_craft.energy_api.damage;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

/**
 * Damage types, can be caused be mod
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DamageTypes {
    /**
     * Type of damage that can be caused by bare cables
     */
    public static final RegistryKey<DamageType> ENERGY;

    static {
        ENERGY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifiers.of("energy"));
    }
}
