package com.github.sib_energy_craft.energy_api.serialization;

import com.github.sib_energy_craft.energy_api.Energy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Energy codec implementation, based on conversion energy to string.
 *
 * @author sibmaks
 * @since 0.1.4
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnergyPacketCodec implements PacketCodec<PacketByteBuf, Energy> {
    public static final EnergyPacketCodec CODEC = new EnergyPacketCodec();

    @Override
    public Energy decode(PacketByteBuf buf) {
        var scale = buf.readInt();
        var bytes = buf.readByteArray();
        var bigInteger = new BigInteger(bytes);
        var bigDecimal = new BigDecimal(bigInteger, scale);
        return new Energy(bigDecimal);
    }

    @Override
    public void encode(PacketByteBuf buf, Energy value) {
        var bigDecimal = value.getAmount();
        var scale = bigDecimal.scale();
        buf.writeInt(scale);
        var bytes = bigDecimal.unscaledValue()
                .toByteArray();
        buf.writeByteArray(bytes);
    }
}
