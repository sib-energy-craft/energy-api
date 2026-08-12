package com.github.sib_energy_craft.energy_api.tags;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
        CHARGEABLE =  TagKey.create(Registries.ITEM, Identifiers.of("chargeable"));
        ENERGY_CONDUCTOR =  TagKey.create(Registries.BLOCK, Identifiers.of("energy_conductor"));
    }

    /**
     * Method allow check item stack to ability to charge.<br/>
     * Validation is performed by item tag, not item type
     *
     * @param itemStack stack to check
     * @return true - item is chargeable, false - otherwise
     */
    public static boolean isChargeable(@NotNull ItemStack itemStack) {
        return itemStack.typeHolder().is(CoreTags.CHARGEABLE);
    }

    /**
     * Method allow check block stack to ability to conduct energy.<br/>
     * Validation is performed by item tag, not item type
     *
     * @param blockState block to check
     * @return true - block is energy conductor, false - otherwise
     */
    public static boolean isEnergyConductor(@NotNull BlockState blockState) {
        return blockState.typeHolder().is(CoreTags.ENERGY_CONDUCTOR);
    }
}
