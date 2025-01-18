package com.github.sib_energy_craft.energy_api.buffer;

import com.github.sib_energy_craft.energy_api.Energy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.PacketByteBuf;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Energy reader from serialized state.
 *
 * @author sibmaks
 * @since 0.1.5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnergyReader {

    /**
     * Reads a {@link Energy} from a {@link PacketByteBuf}.
     *
     * @param buffer the buffer to read from
     * @return the read {@link Energy}
     */
    public static Energy read(PacketByteBuf buffer) {
        var parts = buffer.readByteArray();
        var scale = buffer.readInt();
        var bigInt = new BigInteger(parts);
        var bigDecimal = new BigDecimal(bigInt, scale);
        return new Energy(bigDecimal);
    }
}