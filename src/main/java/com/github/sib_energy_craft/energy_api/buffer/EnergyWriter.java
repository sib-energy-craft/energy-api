package com.github.sib_energy_craft.energy_api.buffer;

import com.github.sib_energy_craft.energy_api.Energy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Energy writer into serialized state.
 *
 * @author sibmaks
 * @since 0.1.5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnergyWriter {

    /**
     * Writes a {@link Energy} to a {@link FriendlyByteBuf}
     *
     * @param value  the {@link Energy} to write
     * @param buffer the {@link FriendlyByteBuf} to write to
     */
    public static void write(Energy value, FriendlyByteBuf buffer) {
        var bigDecimal = value.getAmount();
        var parts = bigDecimal.unscaledValue()
                .toByteArray();
        buffer.writeByteArray(parts);
        buffer.writeInt(bigDecimal.scale());
    }
}
