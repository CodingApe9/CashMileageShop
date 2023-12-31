package org.codingape9.cashmileageshop.command.shop;

import java.util.List;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.repository.ItemRepository;
import org.codingape9.cashmileageshop.repository.ShopItemRepository;
import org.codingape9.cashmileageshop.repository.ShopRepository;
import org.codingape9.cashmileageshop.state.ShopState;

public abstract class ShopCommand {

    protected final ShopRepository shopRepository;
    protected final ShopItemRepository shopItemRepository;
    protected final ItemRepository itemRepository;

    protected ShopCommand(ShopRepository shopRepository, ShopItemRepository shopItemRepository, ItemRepository itemRepository) {
        this.shopRepository = shopRepository;
        this.shopItemRepository = shopItemRepository;
        this.itemRepository = itemRepository;
    }

    protected boolean isShopNameExist(String shopName) {
        return getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST).contains(shopName);
    }

    protected List<String> getShopNameList(List<ShopState> shopStateList) {
        List<Integer> shopStateNumberList = shopStateList.stream()
                .map(ShopState::getStateNumber)
                .toList();
        return shopRepository.selectShopList(shopStateNumberList)
                .stream()
                .map(ShopDto::name)
                .toList();
    }

    protected List<Integer> getShopItemSlotNumberList(String shopName) {
        return shopRepository.selectShopItemList(shopName)
                .stream()
                .map(ShopItemDto::slotNum)
                .toList();
    }
}
