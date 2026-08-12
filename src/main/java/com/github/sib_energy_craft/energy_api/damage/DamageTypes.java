package com.github.sib_energy_craft.energy_api.damage;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * Damage types can be caused be mod
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DamageTypes {
    /**
     * Type of damage that can be caused by bare cables
     */
    public static final ResourceKey<DamageType> ENERGY;

    static {
        ENERGY = ResourceKey.create(Registries.DAMAGE_TYPE, Identifiers.of("energy"));
    }
}
