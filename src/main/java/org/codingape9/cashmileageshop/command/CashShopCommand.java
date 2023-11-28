package org.codingape9.cashmileageshop.command;

import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.repository.CashItemRepository;
import org.codingape9.cashmileageshop.repository.CashShopRepository;
import org.codingape9.cashmileageshop.repository.ItemRepository;
import org.codingape9.cashmileageshop.state.ShopState;

import java.util.List;

public class CashShopCommand extends ShopCommand {
    private final CashShopRepository cashShopRepository;
    private final CashItemRepository cashItemRepository;
    private final ItemRepository itemRepository;

    public CashShopCommand(
            ItemRepository itemRepository,
            CashShopRepository cashShopRepository,
            CashItemRepository cashItemRepository
    ) {
        super(itemRepository);
        this.cashShopRepository = cashShopRepository;
        this.cashItemRepository = cashItemRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    List<String> getShopNameList(List<ShopState> shopStateList) {
        List<Integer> shopStateNumberList = shopStateList.stream()
                .map(ShopState::getStateNumber)
                .toList();
        return cashShopRepository.selectCashShopList(shopStateNumberList)
                .stream()
                .map(ShopDto::name)
                .toList();
    }

    @Override
    List<Integer> getShopItemSlotNumberList(String cashShopName) {
        return cashShopRepository.selectCashShopItemList(cashShopName)
                .stream()
                .map(ShopItemDto::slotNum)
                .toList();
    }

    @Override
    List<String> getShopInfoList() {
        return cashShopRepository.selectCashShopList(ShopState.ALL_STATE_LIST)
                .stream()
                .map(shopDto -> String.format("%s(%s)", shopDto.name(), ShopState.of(shopDto.state()).getStateName()))
                .toList();
    }

    @Override
    int createShop(String cashShopName, int lineNum) {
        return cashShopRepository.insertCashShop(cashShopName, lineNum);
    }

    @Override
    int deleteShop(String cashShopName) {
        return cashShopRepository.updateCashShopState(cashShopName, ShopState.DELETE_STATE.getStateNumber());
    }

    @Override
    int displayShopItem(
            String cashShopName,
            String itemName,
            int slotNumber,
            int price,
            int maxBuyableCnt,
            int maxBuyableCntServer
    ) {
        ShopDto cashShop = cashShopRepository.selectCashShop(cashShopName);
        ItemDto item = itemRepository.selectItemByName(itemName);
        ShopItemDto shopItem = new ShopItemDto(
                item.id(),
                cashShop.id(),
                price,
                ShopState.CLOSE_STATE.getStateNumber(),
                slotNumber,
                maxBuyableCnt,
                maxBuyableCntServer
        );
        return cashItemRepository.insertCashItem(shopItem);
    }

    @Override
    int deleteShopItem(String cashShopName, int slotNumber) {
        return cashItemRepository.updateCashItemState(cashShopName, slotNumber, ShopState.DELETE_STATE.getStateNumber());
    }

    @Override
    List<ShopItemDto> getShopItemList(String cashShopName) {
        return cashItemRepository.selectCashItemList(cashShopName);
    }

    @Override
    int openShop(String cashShopName) {
        return cashShopRepository.updateCashShopState(cashShopName, ShopState.OPEN_STATE.getStateNumber());
    }

    @Override
    int closeShop(String cashShopName) {
        return cashShopRepository.updateCashShopState(cashShopName, ShopState.CLOSE_STATE.getStateNumber());
    }
}
