package org.codingape9.cashmileageshop.service;

import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.repository.ItemRepository;
import org.codingape9.cashmileageshop.repository.ShopItemRepository;
import org.codingape9.cashmileageshop.repository.ShopRepository;
import org.codingape9.cashmileageshop.state.ShopState;
import org.codingape9.cashmileageshop.view.PlayerMessageSender;

import java.util.List;

import static org.codingape9.cashmileageshop.util.ShopMessageConstants.*;

public class ShopService {
    protected final ShopRepository shopRepository;
    protected final ShopItemRepository shopItemRepository;
    protected final ItemRepository itemRepository;

    public ShopService(
            ShopRepository shopRepository,
            ShopItemRepository shopItemRepository,
            ItemRepository itemRepository
    ) {
        this.shopRepository = shopRepository;
        this.shopItemRepository = shopItemRepository;
        this.itemRepository = itemRepository;
    }

    public boolean createShop(Player administer, String shopName, int lineNum) {
        int insertShopCount = shopRepository.insertShop(shopName, lineNum);
        if (insertShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_INSERT_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_INSERT_SHOP + shopName);
        return true;
    }

    public boolean deleteShop(Player administer, String shopName) {
        int deleteShopCount = shopRepository.updateShopState(shopName, ShopState.DELETE_STATE.getStateNumber());
        if (deleteShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_DELETE_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_DELETE_SHOP + shopName);
        return true;
    }

    public boolean displayItemOnShop(
            Player administer,
            ShopDto shop,
            ItemDto item,
            ShopItemDto shopItem
    ) {
        int displayShopItemCount = shopItemRepository.insertShopItem(shopItem);
        if (displayShopItemCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_DISPLAY_ITEM.formatted(shop.name(), item.name()));
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_DISPLAY_ITEM.formatted(shop.name(), item.name()));
        return true;
    }

    public boolean deleteItemInShop(Player administer, String shopName, int slotNumber) {
        int deleteShopItemCount = shopItemRepository.updateShopItemState(shopName, slotNumber, ShopState.DELETE_STATE.getStateNumber());
        if (deleteShopItemCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_DELETE_ITEM_IN_SHOP.formatted(shopName, slotNumber));
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_DELETE_ITEM_IN_SHOP.formatted(shopName, slotNumber));
        return true;
    }

    public boolean printShopInfo(Player administer, String shopNameInfo) {
        String shopName = shopNameInfo.replace("(닫힘)", "")
                .replace("(오픈)", "")
                .replace("(삭제됨)", "");

        List<ShopItemDto> shopItemList = shopItemRepository.selectShopItemList(shopName);
        StringBuilder shopItemInfo = new StringBuilder();
        shopItemInfo.append(shopName).append("의 아이템 목록\n");
        shopItemList.forEach(shopItem -> shopItemInfo.append(shopItem.toString()).append("\n"));

        PlayerMessageSender.sendSuccessMessage(administer, shopItemInfo.toString());
        return true;
    }

    public boolean openShop(Player administer, String shopName) {
        int openShopCount = shopRepository.updateShopState(shopName, ShopState.OPEN_STATE.getStateNumber());
        if (openShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_OPEN_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_OPEN_SHOP + shopName);
        return true;
    }

    public boolean closeShop(Player administer, String shopName) {
        int closeShopCount = shopRepository.updateShopState(shopName, ShopState.CLOSE_STATE.getStateNumber());
        if (closeShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_CLOSE_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_CLOSE_SHOP + shopName);
        return true;
    }

    public ShopDto getShop(String shopName) {
        return shopRepository.selectShop(shopName);
    }

    public ItemDto getItem(String itemName) {
        return itemRepository.selectItem(itemName);
    }

    public List<String> getShopNameList(List<ShopState> shopStateList) {
        List<Integer> shopStateNumberList = shopStateList.stream()
                .map(ShopState::getStateNumber)
                .toList();
        return shopRepository.selectShopList(shopStateNumberList)
                .stream()
                .map(ShopDto::name)
                .toList();
    }

    public List<Integer> getShopItemSlotNumberList(String shopName) {
        return shopRepository.selectShopItemList(shopName)
                .stream()
                .map(ShopItemDto::slotNum)
                .toList();
    }

    public List<String> getItemNameList() {
        List<ItemDto> itemDtoList = itemRepository.selectItem();
        return itemDtoList.stream().map(ItemDto::name).toList();
    }

    public List<String> getShopInfoList() {
        List<Integer> allStateNumberList = ShopState.ALL_STATE_LIST
                .stream()
                .map(ShopState::getStateNumber)
                .toList();
        return shopRepository.selectShopList(allStateNumberList)
                .stream()
                .map(shopDto -> String.format("%s(%s)", shopDto.name(), ShopState.of(shopDto.state()).getStateName()))
                .toList();
    }
}
