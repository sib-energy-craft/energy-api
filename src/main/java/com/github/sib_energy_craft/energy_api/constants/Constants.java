package com.github.sib_energy_craft.energy_api.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @since 0.0.1
 * @author sibmaks
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final int ENERGY_PRECISION = 10;

    public static final BigDecimal ACCURATE_ZERO = accurate(BigDecimal.ZERO);
    public static final BigDecimal ACCURATE_ONE = accurate(BigDecimal.ONE);
    public static final BigDecimal ACCURATE_TEN = accurate(BigDecimal.TEN);
    public static final BigDecimal ACCURATE_HUNDRED = ACCURATE_TEN.multiply(ACCURATE_TEN);

    /**
     * Method return big decimal with default mod accuracy
     *
     * @param amount base amount
     * @return accurate amount
     */
    @NotNull
    private static BigDecimal accurate(@NotNull BigDecimal amount) {
        return amount.setScale(ENERGY_PRECISION, RoundingMode.HALF_DOWN);
    }
}
