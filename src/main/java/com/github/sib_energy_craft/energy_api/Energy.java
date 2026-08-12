package com.github.sib_energy_craft.energy_api;

import com.github.sib_energy_craft.energy_api.constants.Constants;
import com.github.sib_energy_craft.energy_api.exception.NegativeEnergyException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * The class that represents energy, the main source of power to supply mod mechanisms.
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode
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

    private final BigDecimal amount;

    /**
     * Translate passed {@code int} amount into energy with scale to mod accuracy
     *
     * @param amount energy amount
     */
    public Energy(int amount) {
        if (amount < 0) {
            throw new NegativeEnergyException();
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
            throw new NegativeEnergyException();
        }
        this.amount = amount
                .setScale(Constants.ENERGY_PRECISION, RoundingMode.HALF_DOWN)
                .max(Constants.ACCURATE_ZERO);
    }

    /**
     * Translate passed {@link String} amount into energy with scale to mod accuracy
     *
     * @param amount energy amount
     */
    public Energy(String amount) {
        Objects.requireNonNull(amount, "Amount can't be null");
        var bigDecimal = new BigDecimal(amount)
                .setScale(Constants.ENERGY_PRECISION, RoundingMode.HALF_DOWN);
        if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeEnergyException();
        }
        this.amount = bigDecimal.max(Constants.ACCURATE_ZERO);
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
     * Returns a {@code Energy} whose value is {@code (this * delta)}.
     *
     * @param delta value to be multiplied to this {@code Energy}.
     * @return {@code this * delta}
     * @since 0.1.2
     */
    @NotNull
    public Energy multiply(@NotNull Energy delta) {
        var result = amount.multiply(delta.amount);
        return new Energy(result);
    }

    /**
     * Returns a {@code Energy} whose value is {@code (this / divisor)}.
     *
     * @param divisor value to be multiplied to this {@code Energy}.
     * @return {@code this / divisor}
     * @throws ArithmeticException - if divisor==0.
     * @since 0.1.2
     */
    @NotNull
    public Energy divide(@NotNull Energy divisor) {
        var result = amount.divide(divisor.amount, RoundingMode.HALF_DOWN);
        return new Energy(result);
    }

    /**
     * Returns a {@code Energy} whose value is <code>(this<sup>n</sup>)</code>.<br/>
     * The parameter n must be in the range 0 through 999999999, inclusive.
     *
     * @param n power to raise this {@code Energy} to.
     * @return <code>this<sup>n</sup></code>
     * @throws ArithmeticException - if n is out of range.
     * @since 0.1.2
     */
    @NotNull
    public Energy pow(int n) {
        var result = amount.pow(n);
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
     * @param value the value with which the minimum is to be computed.
     * @return the {@code Energy} whose value is the least of this
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
     * @param value the value with which the maximum is to be computed.
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
     * Represent energy as plain string, e.g.:<br/>
     * - 0<br/>
     * - 0.1<br/>
     * - 12.321<br/>
     *
     * @return energy as plain string
     */
    public String toPlainString() {
        return amount
                .stripTrailingZeros()
                .toPlainString();
    }

    /**
     * Write energy into NBT with a passed key
     *
     * @param key nbt energy key
     * @param nbt nbt to write
     */
    public void writeNbt(String key, CompoundTag nbt) {
        var scale = amount.scale();

        var nbtCompound = new CompoundTag();
        nbtCompound.putInt("Scale", scale);

        var bytes = amount.unscaledValue()
                .toByteArray();
        nbtCompound.putByteArray("Bytes", bytes);

        nbt.put(key, nbtCompound);
    }

    /**
     * Read Energy from {@link CompoundTag} by passed key
     *
     * @param key energy key
     * @param nbt source nbt
     * @return energy instance
     */
    public static Energy readNbt(String key, CompoundTag nbt) {
        var compoundOptional = nbt.getCompound(key);
        if (compoundOptional.isEmpty()) {
            return Energy.ZERO;
        }
        var compound = compoundOptional.get();
        var scale = compound.getInt("Scale").orElse(Constants.ENERGY_PRECISION);
        var bytes = compound.getByteArray("Bytes").orElse(new byte[0]);
        var bigInteger = new BigInteger(bytes);
        var bigDecimal = new BigDecimal(bigInteger, scale);
        return new Energy(bigDecimal);
    }
}
