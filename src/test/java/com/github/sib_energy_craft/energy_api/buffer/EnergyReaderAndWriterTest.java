package com.github.sib_energy_craft.energy_api.buffer;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.constants.Constants;
import net.minecraft.network.PacketByteBuf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EnergyReaderAndWriterTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "0",
            "0.9223372036854775807",
            "1",
            "9223372036854775807",
            "9223372036854775807.9223372036854775807"
    })
    void testConvertEnergy(String value) {
        var buffer = mock(PacketByteBuf.class);
        var energy = new Energy(new BigDecimal(value));
        EnergyWriter.write(energy, buffer);

        var once = times(1);
        var dataCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(buffer, once)
                .writeByteArray(dataCaptor.capture());

        var scaleCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(buffer, once)
                .writeInt(scaleCaptor.capture());

        var data = dataCaptor.getValue();
        assertNotNull(data);

        var scale = scaleCaptor.getValue();
        assertNotNull(scale);
        assertEquals(Constants.ENERGY_PRECISION, scale);

        when(buffer.readByteArray())
                .thenReturn(data);
        when(buffer.readInt())
                .thenReturn(scale);

        var actual = EnergyReader.read(buffer);
        assertEquals(energy, actual);
    }
}