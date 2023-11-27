package org.codingape9.cashmileageshop.dto;

public record ShopItemDto(
        int id,
        int maxBuyableCnt,
        int price,
        int itemId,
        int shopId,
        int maxBuyableCntServer,
        int slotNum,
        int state
) {
    private static final int EMPTY_UUID_ID = -1;

    public ShopItemDto(
            int maxBuyableCnt,
            int price,
            int itemId,
            int shopId,
            int maxBuyableCntServer,
            int slotNum,
            int state
    ) {
        this(EMPTY_UUID_ID, maxBuyableCnt, price, itemId, shopId, maxBuyableCntServer, slotNum, state);
    }
}
