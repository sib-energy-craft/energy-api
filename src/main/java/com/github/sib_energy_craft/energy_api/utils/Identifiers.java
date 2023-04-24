package com.github.sib_energy_craft.energy_api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.1
 * @author sibmaks
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Identifiers {
    public static final String MOD_NAMESPACE = "sib_energy_craft";

    @NotNull
    public static Identifier of(@NotNull String path) {
        return new Identifier(MOD_NAMESPACE, path);
    }

    @NotNull
    public static String asString(@NotNull String path) {
        return String.format("%s:%s", MOD_NAMESPACE, path);
    }
}
