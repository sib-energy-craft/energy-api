package com.github.sib_energy_craft.energy_api;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnergyTest {

    @ParameterizedTest
    @MethodSource("equalsCases")
    void testEquals(String lhs, String rhs) {
        var left = new Energy(lhs);
        var right = new Energy(rhs);

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @ParameterizedTest
    @MethodSource("addCases")
    void testAdd(String lhs, String rhs, Energy excepted) {
        var left = new Energy(lhs);
        var right = new Energy(rhs);

        var actual = left.add(right);

        assertEquals(excepted, actual);
    }

    @ParameterizedTest
    @MethodSource("subtractCases")
    void testSubtract(String lhs, String rhs, Energy excepted) {
        var left = new Energy(lhs);
        var right = new Energy(rhs);

        var actual = left.subtract(right);

        assertEquals(excepted, actual);
    }

    @ParameterizedTest
    @MethodSource("toPlainStringCases")
    void testSubtract(Energy value, String excepted) {
        var actual = value.toPlainString();

        assertEquals(excepted, actual);
    }

    public static Stream<Arguments> equalsCases() {
        return Stream.of(
                Arguments.of("0", "0.0"),
                Arguments.of("1", "1.0"),
                Arguments.of("9223372036854775807", "9223372036854775807.0"),
                Arguments.of("9223372036854775807.9223372036854775807", "9223372036854775807.92233720368547758070")
        );
    }

    public static Stream<Arguments> addCases() {
        return Stream.of(
                Arguments.of("0", "0.0", Energy.ZERO),
                Arguments.of("1", "1.0", new Energy(2)),
                Arguments.of("1", "0.1", new Energy("1.1"))
        );
    }

    public static Stream<Arguments> subtractCases() {
        return Stream.of(
                Arguments.of("0", "0.0", Energy.ZERO),
                Arguments.of("1", "1.0", Energy.ZERO),
                Arguments.of("1", "0.1", new Energy("0.9")),
                Arguments.of("0.1", "1", Energy.ZERO)
        );
    }

    public static Stream<Arguments> toPlainStringCases() {
        return Stream.of(
                Arguments.of(Energy.ZERO, "0"),
                Arguments.of(Energy.ONE, "1"),
                Arguments.of(new Energy("10"), "10"),
                Arguments.of(new Energy("0.123456789"), "0.123456789"),
                Arguments.of(new Energy("0.12345678900"), "0.123456789")
        );
    }
}