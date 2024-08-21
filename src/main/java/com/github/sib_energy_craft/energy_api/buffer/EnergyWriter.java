package com.github.sib_energy_craft.energy_api.buffer;

import com.github.sib_energy_craft.energy_api.Energy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.PacketByteBuf;

/**
 * @author sibmaks
 * @since 0.1.5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnergyWriter {

    /**
     * Writes a {@link Energy} to a {@link PacketByteBuf}
     *
     * @param value  the {@link Energy} to write
     * @param buffer the {@link PacketByteBuf} to write to
     */
    public static void write(Energy value, PacketByteBuf buffer) {
        var bigDecimal = value.getAmount();
        var parts = bigDecimal.unscaledValue()
                .toByteArray();
        buffer.writeByteArray(parts);
        buffer.writeInt(bigDecimal.scale());
    }
}
