package org.codingape9.cashmileageshop.dto;

import org.jetbrains.annotations.NotNull;

public record ItemInfoDto(
        int shopItemId,
        int maxBuyableCnt,
        int price,
        int itemId,
        int shopId,
        int maxBuyableCntServer,
        int slotNum,
        int state,
        @NotNull String itemInfo,
        @NotNull String name
) {
}
