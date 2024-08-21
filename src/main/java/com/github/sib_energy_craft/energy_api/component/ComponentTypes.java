package com.github.sib_energy_craft.energy_api.component;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.serialization.EnergyCodec;
import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Energy component types extension.
 *
 * @author sibmaks
 * @since 0.1.2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComponentTypes {
    /**
     * Mod adds new component type - energy.
     */
    public static final ComponentType<Energy> CHARGE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifiers.of("charge"),
            ComponentType.<Energy>builder()
                    .codec(new EnergyCodec())
                    .build()
    );
}
