package org.codingape9.cashmileageshop.dto;

import org.jetbrains.annotations.NotNull;

public record UserDto(
        int id,
        @NotNull String uuid,
        int cash,
        int mileage
) {
}
