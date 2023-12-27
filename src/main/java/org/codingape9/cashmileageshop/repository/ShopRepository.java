package org.codingape9.cashmileageshop.repository;

import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;

import java.util.List;

public interface ShopRepository {
    List<ShopDto> selectShopList(List<Integer> stateList);
    List<ShopItemDto> selectShopItemList(String shopName);
    ShopDto selectShop(String shopName);
    int insertShop(String shopName, int lineNum);
    int updateShopState(String shopName, int state);
}
