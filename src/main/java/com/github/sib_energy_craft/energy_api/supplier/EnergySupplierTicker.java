package com.github.sib_energy_craft.energy_api.supplier;

import com.github.sib_energy_craft.energy_api.consumer.EnergyConsumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of energy suppler ticker
 *
 * @since 0.0.1
 * @author sibmaks
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class EnergySupplierTicker {

    /**
     * Default implementation of energy supplying
     *
     * @param supplier energy supplier
     * @param serverWorld server world
     * @param blockEntity energy supplier block
     */
    public static void tick(@NotNull EnergySupplier supplier,
                            @NotNull ServerWorld serverWorld,
                            @NotNull BlockEntity blockEntity) {
        var pos = blockEntity.getPos();
        var directions = supplier.getSupplyingDirections();
        for (var direction : directions) {
            var neighborPos = pos.offset(direction);
            var neighbor = serverWorld.getBlockEntity(neighborPos);
            if (!(neighbor instanceof EnergyConsumer consumer)) {
                continue;
            }
            var opposite = direction.getOpposite();
            if (!consumer.isConsumeFrom(opposite)) {
                continue;
            }
            var energyOffer = supplier.createOffer();
            consumer.receiveOffer(energyOffer);
        }
    }

}
