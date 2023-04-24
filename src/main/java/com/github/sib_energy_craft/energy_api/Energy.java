package com.github.sib_energy_craft.energy_api;

import com.github.sib_energy_craft.energy_api.constants.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @since 0.0.1
 * @author sibmaks
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Energy implements Comparable<Energy> {
    public static Energy ZERO = Energy.of(0);

    @Getter
    private final BigDecimal amount;

    @NotNull
    public static Energy of(int amount) {
        var decimalAmount = BigDecimal.valueOf(amount)
                .setScale(Constants.ENERGY_PRECISION, RoundingMode.HALF_DOWN)
                .max(Constants.ACCURATE_ZERO);
        return new Energy(decimalAmount);
    }

    @NotNull
    public static Energy of(@NotNull BigDecimal amount) {
        amount = amount
                .setScale(Constants.ENERGY_PRECISION, RoundingMode.HALF_DOWN)
                .max(Constants.ACCURATE_ZERO);
        return new Energy(amount);
    }

    @NotNull
    public Energy subtract(@NotNull Energy energy) {
        var result = amount.subtract(energy.amount);
        return of(result);
    }

    @NotNull
    public Energy subtract(@NotNull BigDecimal energy) {
        var result = amount.subtract(energy);
        return of(result);
    }

    @NotNull
    public Energy subtract(int energy) {
        return this.subtract(Energy.of(energy));
    }

    @Override
    public int compareTo(@NotNull Energy o) {
        return amount.compareTo(o.amount);
    }

    public int intValue() {
        return amount.intValue();
    }

    @NotNull
    public Energy min(@NotNull Energy energy) {
        return this.compareTo(energy) <= 0 ? this : energy;
    }

    @NotNull
    public Energy add(@NotNull Energy energy) {
        var result = amount.add(energy.amount);
        return of(result);
    }

    @NotNull
    public Energy add(int amount) {
        return this.add(Energy.of(amount));
    }
}
