package com.github.sib_energy_craft.energy_api;

import com.github.sib_energy_craft.energy_api.constants.Constants;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@ToString
public class Energy implements Comparable<Energy> {
    /**
     * Zero energy unit
     */
    public static final Energy ZERO = new Energy(0);
    /**
     * One energy unit
     *
     * @since 0.0.5
     */
    public static final Energy ONE = new Energy(1);
    /**
     * Ten energy units
     *
     * @since 0.0.5
     */
    public static final Energy TEN = new Energy(10);

    @Getter
    private final BigDecimal amount;

    /**
     * Translate passed {@link int} amount into energy with scale to mod accuracy
     *
     * @param amount energy amount
     */
    public Energy(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Energy can't be negative!");
        }
        this.amount = BigDecimal.valueOf(amount)
                .setScale(Constants.ENERGY_PRECISION, RoundingMode.HALF_DOWN)
                .max(Constants.ACCURATE_ZERO);
    }

    /**
     * Translate passed {@link BigDecimal} amount into energy with scale to mod accuracy
     *
     * @param amount energy amount
     */
    public Energy(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount can't be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Energy can't be negative!");
        }
        this.amount = amount
                .setScale(Constants.ENERGY_PRECISION, RoundingMode.HALF_DOWN)
                .max(Constants.ACCURATE_ZERO);
    }

    /**
     * Returns a {@code Energy} whose value is {@code (this + delta)}.
     *
     * @param delta value to be added to this {@code Energy}.
     * @return {@code this + delta}
     */
    @NotNull
    public Energy add(@NotNull Energy delta) {
        var result = amount.add(delta.amount);
        if (result.compareTo(BigDecimal.ZERO) <= 0) {
            return ZERO;
        }
        return new Energy(result);
    }

    /**
     * Returns a {@code Energy} whose value is {@code (max(0, this - delta))}.
     *
     * @param delta value to be added to this {@code Energy}.
     * @return {@code max(0, this - delta)}
     */
    @NotNull
    public Energy subtract(@NotNull Energy delta) {
        var result = amount.subtract(delta.amount);
        if (result.compareTo(BigDecimal.ZERO) <= 0) {
            return ZERO;
        }
        return new Energy(result);
    }

    @Override
    public int compareTo(@NotNull Energy o) {
        return amount.compareTo(o.amount);
    }

    /**
     * Convert energy amount into int.
     *
     * @return energy amount as int
     */
    public int asInt() {
        return amount.intValue();
    }

    /**
     * Returns the minimum of this {@code Energy} and {@code value}.
     *
     * @param value value with which the minimum is to be computed.
     * @return the {@code Energy} whose value is the lesser of this
     * {@code Energy} and {@code value}.  If they are equal,
     * as defined by the {@link #compareTo(Energy) compareTo}
     * method, {@code this} is returned.
     * @see #compareTo(Energy)
     */
    @NotNull
    public Energy min(@NotNull Energy value) {
        return this.compareTo(value) <= 0 ? this : value;
    }

    /**
     * Returns the maximum of this {@code Energy} and {@code value}.
     *
     * @param value value with which the maximum is to be computed.
     * @return the {@code Energy} whose value is the greatest of this
     * {@code Energy} and {@code value}.  If they are equal,
     * as defined by the {@link #compareTo(Energy) compareTo}
     * method, {@code this} is returned.
     * @see #compareTo(Energy)
     */
    @NotNull
    public Energy max(@NotNull Energy value) {
        return this.compareTo(value) >= 0 ? this : value;
    }

    /**
     * Write energy into NBT with passed key
     *
     * @param key nbt energy key
     * @param nbt nbt to write
     */
    public void writeNbt(String key, NbtCompound nbt) {
        nbt.putString(key, amount.toPlainString());
    }

    /**
     * Read Energy from {@link NbtCompound} by passed key
     *
     * @param key energy key
     * @param nbt source nbt
     * @return energy instance
     */
    public static Energy readNbt(String key, NbtCompound nbt) {
        var chargeSerialized = nbt.getString(key);
        var charge = new BigDecimal(chargeSerialized);
        return new Energy(charge);
    }
}
