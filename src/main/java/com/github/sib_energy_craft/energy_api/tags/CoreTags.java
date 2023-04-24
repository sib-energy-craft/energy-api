package com.github.sib_energy_craft.energy_api.tags;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.NotNull;

/**
 * Mod core tags
 *
 * @since 0.0.1
 * @author sibmaks
 */
public class CoreTags {
    private static final TagKey<Item> CHARGEABLE;
    private static final TagKey<Block> ENERGY_CONDUCTOR;

    static {
        CHARGEABLE =  TagKey.of(RegistryKeys.ITEM, Identifiers.of("chargeable"));
        ENERGY_CONDUCTOR =  TagKey.of(RegistryKeys.BLOCK, Identifiers.of("energy_conductor"));
    }

    /**
     * Method allow check item stack to ability to charge.<br/>
     * Validation is performed by item tag, not item type
     *
     * @param itemStack stack to check
     * @return true - item is chargeable, false - otherwise
     */
    public static boolean isChargeable(@NotNull ItemStack itemStack) {
        return itemStack.streamTags().anyMatch(it -> it.equals(CoreTags.CHARGEABLE));
    }

    /**
     * Method allow check block stack to ability to conduct energy.<br/>
     * Validation is performed by item tag, not item type
     *
     * @param blockState block to check
     * @return true - block is energy conductor, false - otherwise
     */
    public static boolean isEnergyConductor(@NotNull BlockState blockState) {
        return blockState.streamTags().anyMatch(it -> it.equals(CoreTags.ENERGY_CONDUCTOR));
    }
}
