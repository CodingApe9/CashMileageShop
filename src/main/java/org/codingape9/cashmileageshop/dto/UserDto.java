package org.codingape9.cashmileageshop.dto;

import org.jetbrains.annotations.NotNull;

public record UserDto(
        int id,
        @NotNull String uuid,
        int cash,
        int mileage
) {
    public static int DEFAULT_MONEY = 0;
    public static int EMPTY_UUID = -1;

    public UserDto(@NotNull String uuid) {
        this(EMPTY_UUID, uuid, DEFAULT_MONEY, DEFAULT_MONEY);
    }
}
