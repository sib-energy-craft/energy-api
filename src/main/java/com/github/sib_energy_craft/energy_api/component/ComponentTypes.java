package com.github.sib_energy_craft.energy_api.component;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.serialization.EnergyCodec;
import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

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
    public static final DataComponentType<Energy> CHARGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifiers.of("charge"),
            DataComponentType.<Energy>builder()
                    .persistent(EnergyCodec.CODEC)
                    .build()
    );
}
