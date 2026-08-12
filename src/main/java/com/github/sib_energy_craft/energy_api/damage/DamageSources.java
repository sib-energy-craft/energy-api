package com.github.sib_energy_craft.energy_api.damage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Define mod related damage sources
 *
 * @author sibmaks
 * @since 0.0.4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DamageSources {

    /**
     * Get a damage source for a passed world
     *
     * @param world game world
     * @return damage source
     */
    public static @NotNull DamageSource energy(@NotNull Level world) {
        var registryManager = world.registryAccess();
        var registry = registryManager.lookupOrThrow(Registries.DAMAGE_TYPE);
        var registryEntry = registry.getOrThrow(DamageTypes.ENERGY);
        return new DamageSource(registryEntry);
    }

}
