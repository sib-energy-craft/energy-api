package com.github.sib_energy_craft.energy_api.items;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.component.ComponentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

/**
 * Interface that adds the ability to charge items.<br/>
 * Chargeable item has two fields:<br/>
 * - charge - amount of having energy<br/>
 * - maxCharge - max amount of item energy
 *
 * @author sibmaks
 * @since 0.0.1
 */
public interface ChargeableItem {

    /**
     * Get item max charge
     *
     * @return max charge
     */
    @NotNull
    Energy getMaxCharge();

    /**
     * Get energy free space.<br/>
     * Should be between zero and max charge.<br/>
     * Calculates as max charge - charge.
     *
     * @param itemStack item stack
     * @return free space
     */
    @NotNull
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
    @NotNull
    default Energy getCharge(@NotNull ItemStack itemStack) {
        return itemStack.getOrDefault(ComponentTypes.CHARGE, Energy.ZERO);
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
     * The method adds charge to item.<br/>
     * Item use only required amount of energy.<br/>
     * Not used energy returned.
     *
     * @param itemStack item stack
     * @param energy    energy for charge
     * @return not used energy
     */
    @NotNull
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
        if (!(itemStack.getItem() instanceof ChargeableItem chargeableItem)) {
            throw new IllegalArgumentException("Item must be Chargeable: %s".formatted(itemStack.getItem()));
        }
        var maxCharge = chargeableItem.getMaxCharge();
        charge = maxCharge.min(charge);
        itemStack.set(ComponentTypes.CHARGE, charge);
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
        if (!itemStack.has(ComponentTypes.CHARGE)) {
            itemStack.set(ComponentTypes.CHARGE, Energy.ZERO);
        }
    }

    /**
     * The method can be used to add charge info tooltips
     *
     * @param itemStack item stack
     * @param tooltip   result tooltips
     */
    default void appendTooltip(@NotNull ItemStack itemStack,
                               @NotNull List<Component> tooltip) {
        var itemCharge = getCharge(itemStack)
                .toPlainString();
        var maxCharge = getMaxCharge()
                .toPlainString();
        tooltip.add(Component.translatable("attribute.name.sib_energy_craft.charge", itemCharge)
                .setStyle(Style.EMPTY.withColor(Color.GRAY.getRGB())));
        tooltip.add(Component.translatable("attribute.name.sib_energy_craft.max_charge", maxCharge)
                .setStyle(Style.EMPTY.withColor(Color.GRAY.getRGB())));
    }
}
