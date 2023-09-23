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
    /**
     * Mod namespace code
     */
    public static final String MOD_NAMESPACE = "sib_energy_craft";

    /**
     * Create identifier with mod namespace
     *
     * @param path identified path
     * @return namespace identifier
     */
    @NotNull
    public static Identifier of(@NotNull String path) {
        return new Identifier(MOD_NAMESPACE, path);
    }

    /**
     * Create identifier as string with mod namespace
     *
     * @param path identified path
     * @return namespace identifier as string
     */
    @NotNull
    public static String asString(@NotNull String path) {
        return String.format("%s:%s", MOD_NAMESPACE, path);
    }
}
