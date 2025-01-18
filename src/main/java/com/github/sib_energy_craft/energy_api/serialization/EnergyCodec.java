package com.github.sib_energy_craft.energy_api.serialization;

import com.github.sib_energy_craft.energy_api.Energy;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Energy codec implementation, based on conversion energy to string.
 *
 * @author sibmaks
 * @since 0.1.2
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnergyCodec implements Codec<Energy> {
    /**
     * Energy type codec instance
     */
    public static final EnergyCodec CODEC = new EnergyCodec();

    @Override
    public <T> DataResult<Pair<Energy, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getNumberValue(input)
                .flatMap(num -> {
                    var scale = num.intValue();

                    return ops.getByteBuffer(input)
                            .map(ByteBuffer::array)
                            .map(BigInteger::new)
                            .map(it -> new BigDecimal(it, scale))
                            .map(Energy::new)
                            .map(r -> Pair.of(r, ops.empty()));
                });
    }

    @Override
    public <T> DataResult<T> encode(Energy input, DynamicOps<T> ops, T prefix) {
        var bigDecimal = input.getAmount();
        var scale = ops.createNumeric(bigDecimal);
        return ops.mergeToPrimitive(prefix, scale)
                .flatMap(it -> {
                    var bytes = bigDecimal.unscaledValue()
                            .toByteArray();
                    var buffer = ByteBuffer.wrap(bytes);
                    var byteList = ops.createByteList(buffer);

                    return ops.mergeToPrimitive(it, byteList);
                });
    }

}
