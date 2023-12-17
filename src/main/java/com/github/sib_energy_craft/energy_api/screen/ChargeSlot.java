package com.github.sib_energy_craft.energy_api.screen;

import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

/**
 * Screen slot, can accept only items with tag 'chargeable'<br/>
 * Slot can charge or discharge item, depends on slot settings
 *
 * @since 0.0.1
 * @author sibmaks
 */
public class ChargeSlot extends Slot {
    private final boolean charging;

    /**
     * Charge slot constructor
     *
     * @param inventory slot inventory
     * @param index index of slot in inventory
     * @param x screen x position
     * @param y screen y position
     * @param charging true - slot for item charging, false - discharging
     */
    public ChargeSlot(@NotNull Inventory inventory,
                      int index,
                      int x,
                      int y,
                      boolean charging) {
        super(inventory, index, x, y);
        this.charging = charging;
    }

    @Override
    public boolean canInsert(@NotNull ItemStack stack) {
        final var item = stack.getItem();
        if(!(stack.getCount() == 1 && item instanceof ChargeableItem chargeableItem)) {
            return false;
        }
        if(charging) {
            return chargeableItem.hasFreeSpace(stack);
        } else {
            return chargeableItem.hasEnergy(stack);
        }
    }

    @Override
    public int getMaxItemCount(@NotNull ItemStack stack) {
        return 1;
    }

}
