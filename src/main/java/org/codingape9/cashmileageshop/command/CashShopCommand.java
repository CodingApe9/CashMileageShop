package org.codingape9.cashmileageshop.command;

import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.repository.CashItemRepository;
import org.codingape9.cashmileageshop.repository.CashShopRepository;
import org.codingape9.cashmileageshop.repository.ItemRepository;

import java.util.List;

public class CashShopCommand extends ShopCommand {
    private static final List<Integer> ALL_STATE_LIST = List.of(1, 2, 3);
    private static final int CLOSE_STATE = 1;
    private static final int OPEN_STATE = 2;
    private static final int DELETE_STATE = 3;

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
    List<String> getShopNameList(List<Integer> stateList) {
        return cashShopRepository.selectCashShopList(stateList)
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
        return cashShopRepository.selectCashShopList(ALL_STATE_LIST)
                .stream()
                .map(
                        shopDto -> String.format(
                                "%s(%s)",
                                shopDto.name(),
                                parseState(shopDto.state())
                        )
                )
                .toList();
    }

    @Override
    int createShop(String cashShopName, int lineNum) {
        return cashShopRepository.insertCashShop(cashShopName, lineNum);
    }

    @Override
    int deleteShop(String cashShopName) {
        return cashShopRepository.updateCashShopState(cashShopName, DELETE_STATE);
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
                1,
                slotNumber,
                maxBuyableCnt,
                maxBuyableCntServer
        );
        return cashItemRepository.insertCashItem(shopItem);
    }

    @Override
    int deleteShopItem(String cashShopName, int slotNumber) {
        return cashItemRepository.updateCashItemState(cashShopName, slotNumber, DELETE_STATE);
    }

    @Override
    List<ShopItemDto> getShopItemList(String cashShopName) {
        return cashItemRepository.selectCashItemList(cashShopName);
    }

    @Override
    int openShop(String cashShopName) {
        return cashShopRepository.updateCashShopState(cashShopName, OPEN_STATE);
    }

    @Override
    int closeShop(String cashShopName) {
        return cashShopRepository.updateCashShopState(cashShopName, CLOSE_STATE);
    }

    private String parseState(int state) {
        return switch (state) {
            case 1 -> "닫힘";
            case 2 -> "오픈";
            case 3 -> "삭제됨";
            default -> "알수없음";
        };
    }
}
