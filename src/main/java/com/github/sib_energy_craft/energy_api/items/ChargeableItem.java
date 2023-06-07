package com.github.sib_energy_craft.energy_api.items;

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
 * - charge - amount of having energy</br>
 * - maxCharge - max amount of item energy
 *
 * @since 0.0.1
 * @author sibmaks
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
    int getMaxCharge();

    /**
     * Get energy free space.<br/>
     * Should be between 0 and max charge.<br/>
     * Calculates as max charge - charge.
     *
     * @param itemStack item stack
     * @return free space
     */
    default int getFreeSpace(@NotNull ItemStack itemStack) {
        return getMaxCharge() - getCharge(itemStack);
    }

    /**
     * Get item charge.<br/>
     * Should be between 0 and max charge.
     *
     * @param itemStack item stack
     * @return item charge
     */
    default int getCharge(@NotNull ItemStack itemStack) {
        var nbt = itemStack.getNbt();
        return nbt != null ? nbt.getInt(CHARGE) : 0;
    }

    /**
     * Check item condition that item is not fully charged
     *
     * @param itemStack item stack
     * @return true - has free space, false - otherwise
     */
    default boolean hasFreeSpace(@NotNull ItemStack itemStack) {
        return getMaxCharge() > getCharge(itemStack);
    }

    /**
     * Check item condition that item has any energy
     *
     * @param itemStack item stack
     * @return true - if stack has energy, false - otherwise
     */
    default boolean hasEnergy(@NotNull ItemStack itemStack) {
        return getCharge(itemStack) > 0;
    }

    /**
     * The method add charge to item.<br/>
     * Item use only required amount of energy.<br/>
     * Not used energy returned.
     *
     * @param itemStack item stack
     * @param energy energy for charge
     * @return not used energy
     */
    default int charge(@NotNull ItemStack itemStack, int energy) {
        if(itemStack.getCount() != 1) {
            return energy;
        }
        int charge = getCharge(itemStack);
        int used = Math.min(getMaxCharge() - charge, energy);
        setCharge(itemStack, charge + used);
        return energy - used;
    }

    /**
     * Set item charge.<br/>
     * In case if item is not {@link ChargeableItem}, then {@link IllegalArgumentException} will be thrown
     *
     * @param itemStack item stack
     * @param charge amount of energy
     */
    default void setCharge(@NotNull ItemStack itemStack, int charge) {
        if(itemStack.getItem() instanceof ChargeableItem chargeableItem) {
            var nbt = itemStack.getOrCreateNbt();
            nbt.putInt(CHARGE, Math.min(chargeableItem.getMaxCharge(), charge));
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
     * @param energy energy for charge
     * @return not used energy
     */
    default boolean discharge(@NotNull ItemStack itemStack, int energy) {
        if(itemStack.getCount() != 1) {
            return false;
        }
        int charge = getCharge(itemStack);
        if(charge < energy) {
            return false;
        }
        setCharge(itemStack, charge - energy);
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
        if(!nbt.contains(CHARGE, NbtElement.INT_TYPE)) {
            nbt.putInt(CHARGE, 0);
        }
    }

    /**
     * The method can be used to add charge info tooltips
     *
     * @param itemStack item stack
     * @param tooltip result tooltips
     */
    default void appendTooltip(@NotNull ItemStack itemStack,
                               @NotNull List<Text> tooltip) {
        tooltip.add(Text.translatable("attribute.name.sib_energy_craft.charge", getCharge(itemStack))
                .setStyle(Style.EMPTY.withColor(Color.GRAY.getRGB())));
        tooltip.add(Text.translatable("attribute.name.sib_energy_craft.max_charge", getMaxCharge())
                .setStyle(Style.EMPTY.withColor(Color.GRAY.getRGB())));
    }
}
