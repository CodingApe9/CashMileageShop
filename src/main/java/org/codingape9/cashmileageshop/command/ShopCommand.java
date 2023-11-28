package org.codingape9.cashmileageshop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.repository.ItemRepository;
import org.codingape9.cashmileageshop.state.ShopState;
import org.codingape9.cashmileageshop.view.PlayerMessageSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ShopCommand implements TabCompleter, CommandExecutor {
    private static final List<String> EMPTY_AUTOCOMPLETE = List.of();
    private static final List<String> FIRST_AUTOCOMPLETE = List.of("생성", "삭제", "진열", "아이템삭제", "목록", "오픈", "닫기");
    private static final int CREATE_SHOP = 0;
    private static final int DELETE_SHOP = 1;
    private static final int DISPLAY_ITEM = 2;
    private static final int DELETE_ITEM_IN_SHOP = 3;
    private static final int DISPLAY_SHOP_LIST = 4;
    private static final int OPEN_SHOP = 5;
    private static final int CLOSE_SHOP = 6;
    private static final List<String> CREATE_SHOP_EXTRA_COMMAND = List.of("<이름> <줄 수>");
    private static final List<String> DISPLAY_ITEM_EXTRA_COMMAND = List.of("<슬롯번호> <가격> <최대구매가능개수(개인)> <최대구매가능개수(전역)>");
    private static final int NO_SUB_COMMAND = 1;
    private static final String WRONG_COMMAND = "명령어를 잘못 입력했습니다.";
    private static final String EXISTING_SHOP = "이미 존재하는 상점입니다. 상점 이름: ";
    private static final String FAIL_INSERT_SHOP = "상점을 생성하는데 실패했습니다. 상점 이름: ";
    private static final String FAIL_OPEN_SHOP = "상점을 오픈하는데 실패했습니다. 상점 이름: ";
    private static final String UNDELETEABLE_SHOP = "존재하지 않는 상점입니다. 상점 이름: ";
    private static final String UNDEFINED_SHOP = "상점을 찾을 수 없습니다. 상점 이름: ";
    private static final String FAIL_DELETE_SHOP = "상점을 삭제하는데 실패했습니다. 상점 이름: ";
    private static final String UNEXISTING_ITEM = "존재하지 않는 아이템입니다. 아이템 이름: ";
    private static final String FAIL_DISPLAY_ITEM = "아이템을 진열하는데 실패했습니다. [상점 이름: %s 아이템 이름: %s]";
    private static final String UNEXISTING_ITEM_IN_SHOP = "존재하지 않는 아이템입니다. [상점 이름: %s 아이템 이름: %s]";
    private static final String FAIL_DELETE_ITEM_IN_SHOP = "아이템 삭제에 실패했습니다. [상점 이름: %s 아이템 이름: %s]";
    private static final String FAIL_CLOSE_SHOP = "상점을 닫는데 실패했습니다. 상점 이름: ";
    private static final String SUCCESS_INSERT_SHOP = "상점을 생성했습니다. 상점 이름: ";
    private static final String SUCCESS_OPEN_SHOP = "상점을 오픈했습니다. 상점 이름: ";
    private static final String SUCCESS_DELETE_SHOP = "상점을 삭제했습니다. 상점 이름: ";
    private static final String SUCCESS_DISPLAY_ITEM = "아이템을 진열했습니다. [상점 이름: %s 아이템 이름: %s]";
    private static final String SUCCESS_DELETE_ITEM_IN_SHOP = "아이템을 삭제했습니다. [상점 이름: %s 아이템 이름: %s]";
    private static final String SUCCESS_CLOSE_SHOP = "상점을 닫았습니다. 상점 이름: ";

    private final ItemRepository itemRepository;

    protected ShopCommand(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] subCommand
    ) {
        Player player = (Player) sender;
        if (!player.isOp()) {
            return EMPTY_AUTOCOMPLETE;
        }
        int subCommandCount = subCommand.length;

        if (subCommandCount == NO_SUB_COMMAND) {
            return FIRST_AUTOCOMPLETE;
        }

        return getAutoCompleteSubCommand(subCommand);
    }

    private List<String> getAutoCompleteSubCommand(String[] subCommand) {
        String firstSubCommand = subCommand[0];
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(CREATE_SHOP))) {
            return CREATE_SHOP_EXTRA_COMMAND;
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(DELETE_SHOP))) {
            return getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(DISPLAY_ITEM))) {
            return getDisplayItemExtraCommand(subCommand);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(DELETE_ITEM_IN_SHOP))) {
            return getDeleteItemExtraCommand(subCommand);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(DISPLAY_SHOP_LIST))) {
            return getShopInfoList();
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(OPEN_SHOP))) {
            return getShopNameList(ShopState.UNOPEN_SHOP_STATE_LIST);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(CLOSE_SHOP))) {
            return getShopNameList(ShopState.OPEN_SHOP_STATE_LIST);
        }

        return EMPTY_AUTOCOMPLETE;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] subCommand
    ) {
        Player player = (Player) sender;
        if (!player.isOp()) {
            return false;
        }
        Player administer = player;
        int subCommandCount = subCommand.length;
        if (subCommandCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }

        return executeShopOpCommand(administer, subCommand);
    }

    private boolean executeShopOpCommand(Player administer, String[] subCommand) {
        String firstSubCommand = subCommand[0];
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(CREATE_SHOP))) {
            return executeCreateShop(administer, subCommand);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(DELETE_SHOP))) {
            return executeDeleteShop(administer, subCommand);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(DISPLAY_ITEM))) {
            return executeDisplayItem(administer, subCommand);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(DELETE_ITEM_IN_SHOP))) {
            return executeDeleteItemInShop(administer, subCommand);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(DISPLAY_SHOP_LIST))) {
            return executeDisplayShopInfo(administer, subCommand);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(OPEN_SHOP))) {
            return executeOpenShop(administer, subCommand);
        }
        if (firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(CLOSE_SHOP))) {
            return executeCloseShop(administer, subCommand);
        }
        return false;
    }

    private boolean executeCreateShop(Player administer, String[] subCommand) {
        int subCommandCount = subCommand.length;
        if (subCommandCount != 3) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }

        String shopName = subCommand[1];
        int lineNum = Integer.parseInt(subCommand[2]);
        if (isShopNameExist(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, EXISTING_SHOP + shopName);
            return false;
        }
        int insertShopCount = createShop(shopName, lineNum);
        if (insertShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_INSERT_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_INSERT_SHOP + shopName);
        return true;
    }

    private boolean executeDeleteShop(Player administer, String[] subCommand) {
        int subCommandCount = subCommand.length;
        if (subCommandCount != 2) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        String shopName = subCommand[1];
        if (!isShopNameExist(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, UNDELETEABLE_SHOP + shopName);
            return false;
        }
        int deleteShopCount = deleteShop(shopName);
        if (deleteShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_DELETE_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_DELETE_SHOP + shopName);
        return true;
    }

    private boolean executeDisplayItem(Player administer, String[] subCommand) {
        int subCommandCount = subCommand.length;
        if (subCommandCount != 7) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        String shopName = subCommand[1];
        String itemName = subCommand[2];
        if (!isShopNameExist(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, UNDELETEABLE_SHOP + shopName);
            return false;
        }
        if (!isItemExist(itemName)) {
            PlayerMessageSender.sendErrorMessage(administer, UNEXISTING_ITEM + itemName);
            return false;
        }
        int slotNumber = Integer.parseInt(subCommand[3]);
        int price = Integer.parseInt(subCommand[4]);
        int maxBuyableCnt = Integer.parseInt(subCommand[5]);
        int maxBuyableCntServer = Integer.parseInt(subCommand[6]);

        int displayShopItemCount = displayShopItem(shopName, itemName, slotNumber, price, maxBuyableCnt, maxBuyableCntServer);
        if (displayShopItemCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_DISPLAY_ITEM.formatted(shopName, itemName));
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_DISPLAY_ITEM.formatted(shopName, itemName));
        return true;
    }

    private boolean executeDeleteItemInShop(Player administer, String[] subCommand) {
        int subCommandCount = subCommand.length;
        if (subCommandCount != 3) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        String shopName = subCommand[1];
        if (!isShopNameExist(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, UNDELETEABLE_SHOP + shopName);
            return false;
        }
        int slotNum = Integer.parseInt(subCommand[2]);
        List<Integer> shopItemSlotNumList = getShopItemSlotNumberList(shopName);
        if (!shopItemSlotNumList.contains(slotNum)) {
            PlayerMessageSender.sendErrorMessage(administer, UNEXISTING_ITEM_IN_SHOP.formatted(shopName, slotNum));
            return false;
        }
        int deleteShopItemCount = deleteShopItem(shopName, slotNum);
        if (deleteShopItemCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_DELETE_ITEM_IN_SHOP.formatted(shopName, slotNum));
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_DELETE_ITEM_IN_SHOP.formatted(shopName, slotNum));
        return true;
    }

    private boolean executeDisplayShopInfo(Player administer, String[] subCommand) {
        int subCommandCount = subCommand.length;
        if (subCommandCount != 2) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        String shopName = getShopName(subCommand[1]);
        if (!isShopNameExist(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, UNDELETEABLE_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, getShopItemInfo(shopName));
        return true;
    }

    private boolean executeOpenShop(Player administer, String[] subCommand) {
        int subCommandCount = subCommand.length;
        if (subCommandCount != 2) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }

        String shopName = subCommand[1];
        List<String> closedShopNameList = getShopNameList(ShopState.UNOPEN_SHOP_STATE_LIST);
        if (!closedShopNameList.contains(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, UNDEFINED_SHOP + shopName);
        }

        int openShopCount = openShop(shopName);
        if (openShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_OPEN_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_OPEN_SHOP + shopName);
        return true;
    }

    private boolean executeCloseShop(Player administer, String[] subCommand) {
        int subCommandCount = subCommand.length;
        if (subCommandCount != 2) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }

        String shopName = subCommand[1];
        List<String> openedShopNameList = getShopNameList(ShopState.OPEN_SHOP_STATE_LIST);
        if (!openedShopNameList.contains(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, UNDEFINED_SHOP + shopName);
        }
        int closeShopCount = closeShop(shopName);
        if (closeShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_CLOSE_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_CLOSE_SHOP + shopName);
        return true;
    }

    private String getShopItemInfo(String shopName) {
        List<ShopItemDto> shopItemList = getShopItemList(shopName);
        StringBuilder shopItemInfo = new StringBuilder();
        shopItemInfo.append(shopName).append("의 아이템 목록\n");
        shopItemList.forEach(
                shopItem -> shopItemInfo.append(shopItem.toString()).append("\n")
        );
        return shopItemInfo.toString();
    }

    private String getShopName(String shopNameInfo) {
        return shopNameInfo.replace("(닫힘)", "")
                .replace("(오픈)", "")
                .replace("(삭제됨)", "");
    }

    private boolean isItemExist(String itemName) {
        return getItemNameList().contains(itemName);
    }

    private boolean isShopNameExist(String shopName) {
        return getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST).contains(shopName);
    }

    private List<String> getDeleteItemExtraCommand(String[] subCommand) {
        int subCommandCount = subCommand.length;
        String inputShopName = subCommand[1];
        List<String> shopNameList = getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);

        if (subCommandCount > 2 && shopNameList.contains(inputShopName)) {
            return getShopItemSlotNumberList(inputShopName)
                    .stream()
                    .map(String::valueOf)
                    .toList();
        }
        return shopNameList;
    }

    private List<String> getDisplayItemExtraCommand(String[] subCommand) {
        int subCommandCount = subCommand.length;
        String inputShopName = subCommand[1];
        List<String> shopNameList = getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);

        if (subCommandCount > 2 && shopNameList.contains(inputShopName)) {
            List<String> itemNameList = getItemNameList();
            String inputItemName = subCommand[2];

            if (subCommandCount > 3 && itemNameList.contains(inputItemName)) {
                return DISPLAY_ITEM_EXTRA_COMMAND;
            }
            return itemNameList;
        }
        return shopNameList;
    }

    private List<String> getItemNameList() {
        List<ItemDto> itemDtoList = itemRepository.selectItem();
        return itemDtoList.stream()
                .map(ItemDto::name)
                .toList();
    }

    abstract List<String> getShopNameList(List<ShopState> shopStateList);

    abstract List<Integer> getShopItemSlotNumberList(String shopName);

    abstract List<String> getShopInfoList();

    abstract int createShop(String shopName, int lineNum);

    abstract int deleteShop(String shopName);

    abstract int displayShopItem(String shopName, String itemName, int slotNumber, int price, int maxBuyableCnt, int maxBuyableCntServer);

    abstract int deleteShopItem(String shopName, int slotNumber);

    abstract List<ShopItemDto> getShopItemList(String shopName);

    abstract int openShop(String shopName);

    abstract int closeShop(String shopName);
}
