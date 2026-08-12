package com.github.sib_energy_craft.energy_api.serialization;

import com.github.sib_energy_craft.energy_api.Energy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Energy codec implementation, based on conversion energy to string.
 *
 * @author sibmaks
 * @since 0.1.4
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnergyPacketCodec implements StreamCodec<FriendlyByteBuf, Energy> {
    /**
     * Energy packet codec instance
     */
    public static final EnergyPacketCodec CODEC = new EnergyPacketCodec();

    @Override
    public Energy decode(FriendlyByteBuf buf) {
        var scale = buf.readInt();
        var bytes = buf.readByteArray();
        var bigInteger = new BigInteger(bytes);
        var bigDecimal = new BigDecimal(bigInteger, scale);
        return new Energy(bigDecimal);
    }

    @Override
    public void encode(FriendlyByteBuf buf, Energy value) {
        var bigDecimal = value.getAmount();
        var scale = bigDecimal.scale();
        buf.writeInt(scale);
        var bytes = bigDecimal.unscaledValue()
                .toByteArray();
        buf.writeByteArray(bytes);
    }
}
