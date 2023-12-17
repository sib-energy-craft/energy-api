package com.github.sib_energy_craft.energy_api.items;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

/**
 * Interface that add ability to charge items.<br/>
 * Chargeable item has two fields:<br/>
 * - charge - amount of having energy<br/>
 * - maxCharge - max amount of item energy
 *
 * @author sibmaks
 * @since 0.0.1
 */
public interface ChargeableItem {
    /**
     * NBT Charge attribute identifier
     */
    String CHARGE = Identifiers.asString("Charge");

    /**
     * Get item max charge
     *
     * @return max charge
     */
    Energy getMaxCharge();

    /**
     * Get energy free space.<br/>
     * Should be between 0 and max charge.<br/>
     * Calculates as max charge - charge.
     *
     * @param itemStack item stack
     * @return free space
     */
    default Energy getFreeSpace(@NotNull ItemStack itemStack) {
        var maxCharge = getMaxCharge();
        var itemCharge = getCharge(itemStack);
        return maxCharge.subtract(itemCharge);
    }

    /**
     * Get item charge.<br/>
     * Should be between 0 and {@link #getMaxCharge()}.
     *
     * @param itemStack item stack
     * @return item charge
     */
    default Energy getCharge(@NotNull ItemStack itemStack) {
        var nbt = itemStack.getNbt();
        if (nbt == null) {
            return Energy.ZERO;
        }
        return Energy.readNbt(CHARGE, nbt);
    }

    /**
     * Check item condition that item is not fully charged
     *
     * @param itemStack item stack
     * @return true - has free space, false - otherwise
     */
    default boolean hasFreeSpace(@NotNull ItemStack itemStack) {
        var maxCharge = getMaxCharge();
        var itemCharge = getCharge(itemStack);
        return maxCharge.compareTo(itemCharge) > 0;
    }

    /**
     * Check item condition that item has any energy
     *
     * @param itemStack item stack
     * @return true - if stack has energy, false - otherwise
     */
    default boolean hasEnergy(@NotNull ItemStack itemStack) {
        var itemCharge = getCharge(itemStack);
        return itemCharge.compareTo(Energy.ZERO) > 0;
    }

    /**
     * The method add charge to item.<br/>
     * Item use only required amount of energy.<br/>
     * Not used energy returned.
     *
     * @param itemStack item stack
     * @param energy    energy for charge
     * @return not used energy
     */
    default Energy charge(@NotNull ItemStack itemStack, Energy energy) {
        if (itemStack.getCount() != 1) {
            return energy;
        }
        var charge = getCharge(itemStack);
        var maxCharge = getMaxCharge();
        var used = maxCharge.subtract(charge)
                .min(energy);
        setCharge(itemStack, charge.add(used));
        return energy.subtract(used);
    }

    /**
     * Set item charge.<br/>
     * In case if item is not {@link ChargeableItem}, then {@link IllegalArgumentException} will be thrown
     *
     * @param itemStack item stack
     * @param charge    amount of energy
     */
    default void setCharge(@NotNull ItemStack itemStack, Energy charge) {
        if (itemStack.getItem() instanceof ChargeableItem chargeableItem) {
            var nbt = itemStack.getOrCreateNbt();
            var maxCharge = chargeableItem.getMaxCharge();
            maxCharge
                    .min(charge)
                    .writeNbt(CHARGE, nbt);
        } else {
            throw new IllegalArgumentException("Item must be Chargeable: %s".formatted(itemStack.getItem()));
        }
    }

    /**
     * The method remove charge from item.<br/>
     * Item discharges only in case if it has required amount of energy.<br/>
     * If item has not required energy false will be returned.
     *
     * @param itemStack item stack
     * @param energy    energy for charge
     * @return not used energy
     */
    default boolean discharge(@NotNull ItemStack itemStack, Energy energy) {
        if (itemStack.getCount() != 1) {
            return false;
        }
        var charge = getCharge(itemStack);
        if (charge.compareTo(energy) < 0) {
            return false;
        }
        setCharge(itemStack, charge.subtract(energy));
        return true;
    }

    /**
     * The method setup chargeable item after item crafted.<br/>
     * The method should be called every time new item is crafted.
     *
     * @param itemStack crafted item stack
     */
    default void onCraft(@NotNull ItemStack itemStack) {
        var nbt = itemStack.getOrCreateNbt();
        if (!nbt.contains(CHARGE, NbtElement.INT_TYPE)) {
            Energy.ZERO.writeNbt(CHARGE, nbt);
        }
    }

    /**
     * The method can be used to add charge info tooltips
     *
     * @param itemStack item stack
     * @param tooltip   result tooltips
     */
    default void appendTooltip(@NotNull ItemStack itemStack,
                               @NotNull List<Text> tooltip) {
        tooltip.add(Text.translatable("attribute.name.sib_energy_craft.charge", getCharge(itemStack))
                .setStyle(Style.EMPTY.withColor(Color.GRAY.getRGB())));
        tooltip.add(Text.translatable("attribute.name.sib_energy_craft.max_charge", getMaxCharge())
                .setStyle(Style.EMPTY.withColor(Color.GRAY.getRGB())));
    }
}
