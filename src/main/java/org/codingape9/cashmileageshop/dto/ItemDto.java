package org.codingape9.cashmileageshop.dto;

import org.jetbrains.annotations.NotNull;

public record ItemDto(
        int id,
        @NotNull String itemInfo,
        @NotNull String name
) {
    private static final int EMPTY_UUID_ID = -1;

    public ItemDto(@NotNull String itemInfo, @NotNull String name) {
        this(EMPTY_UUID_ID, itemInfo, name);
    }
}
