package org.codingape9.cashmileageshop.repository;

import org.codingape9.cashmileageshop.dto.ShopItemDto;

import java.util.List;

public interface ShopItemRepository {
    int insertShopItem(ShopItemDto shopItem);

    int updateShopItemState(String shopName, int slotNumber, int state);

    List<ShopItemDto> selectShopItemList(String shopName);
}
