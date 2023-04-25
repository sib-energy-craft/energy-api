package com.github.sib_energy_craft.energy_api.damage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 0.0.4
 * @author sibmaks
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DamageSources {
    private static final Map<World, DamageSource> CACHED = new ConcurrentHashMap<>();

    /**
     * Get damage source for passed world<br/>
     * Damage source cached by world and reused on upcoming calls
     *
     * @param world game world
     * @return damage source
     */
    public static @NotNull DamageSource energy(@NotNull World world) {
        return CACHED.computeIfAbsent(world, it -> {
            var registryManager = world.getRegistryManager();
            var registry = registryManager.get(RegistryKeys.DAMAGE_TYPE);
            var registryEntry = registry.entryOf(DamageTypes.ENERGY);
            return new DamageSource(registryEntry);
        });
    }

}
