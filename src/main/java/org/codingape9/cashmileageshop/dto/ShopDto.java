package org.codingape9.cashmileageshop.dto;

import org.jetbrains.annotations.NotNull;

public record ShopDto(
        int id,
        @NotNull String name,
        int line_num,
        int state
) {
}
