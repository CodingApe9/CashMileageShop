package org.codingape9.cashmileageshop.command.shop;

import static org.codingape9.cashmileageshop.util.ShopMessageConstants.EXISTING_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.FAIL_CLOSE_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.FAIL_DELETE_ITEM_IN_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.FAIL_DELETE_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.FAIL_DISPLAY_ITEM;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.FAIL_INSERT_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.FAIL_OPEN_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.SUCCESS_CLOSE_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.SUCCESS_DELETE_ITEM_IN_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.SUCCESS_DELETE_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.SUCCESS_DISPLAY_ITEM;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.SUCCESS_INSERT_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.SUCCESS_OPEN_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.UNDEFINED_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.UNDELETEABLE_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.UNEXISTING_ITEM;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.UNEXISTING_ITEM_IN_SHOP;
import static org.codingape9.cashmileageshop.util.ShopMessageConstants.WRONG_COMMAND;
import static org.codingape9.cashmileageshop.util.validator.ShopCommandValidator.hasAdminPrivileges;
import static org.codingape9.cashmileageshop.util.validator.ShopCommandValidator.hasValidSubCommandLength;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.repository.ItemRepository;
import org.codingape9.cashmileageshop.repository.ShopItemRepository;
import org.codingape9.cashmileageshop.repository.ShopRepository;
import org.codingape9.cashmileageshop.state.ShopState;
import org.codingape9.cashmileageshop.view.PlayerMessageSender;
import org.jetbrains.annotations.NotNull;

public class ShopCommandExecutor extends ShopCommand implements CommandExecutor {

    private static final int NO_SUB_COMMAND = 1;
    private static final String CREATE_SHOP = "생성";
    private static final String DELETE_SHOP = "삭제";
    private static final String DISPLAY_ITEM = "진열";
    private static final String DELETE_ITEM_IN_SHOP = "아이템삭제";
    private static final String DISPLAY_SHOP_LIST = "목록";
    private static final String OPEN_SHOP = "오픈";
    private static final String CLOSE_SHOP = "닫기";

    public ShopCommandExecutor(ShopRepository shopRepository, ShopItemRepository shopItemRepository,
                               ItemRepository itemRepository) {
        super(shopRepository, shopItemRepository, itemRepository);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] subCommand) {
        Player player = (Player) sender;
        if (!hasAdminPrivileges(player)) {
            return false;
        }
        Player administer = player;

        if (hasValidSubCommandLength(subCommand, NO_SUB_COMMAND)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }

        return executeShopOpCommand(administer, subCommand);
    }

    private boolean executeShopOpCommand(Player administer, String[] subCommand) {
        String firstSubCommand = subCommand[0];
        if (firstSubCommand.equals(CREATE_SHOP)) {
            return executeCreateShop(administer, subCommand);
        }
        if (firstSubCommand.equals(DELETE_SHOP)) {
            return executeDeleteShop(administer, subCommand);
        }
        if (firstSubCommand.equals(DISPLAY_ITEM)) {
            return executeDisplayItem(administer, subCommand);
        }
        if (firstSubCommand.equals(DELETE_ITEM_IN_SHOP)) {
            return executeDeleteItemInShop(administer, subCommand);
        }
        if (firstSubCommand.equals(DISPLAY_SHOP_LIST)) {
            return executeDisplayShopInfo(administer, subCommand);
        }
        if (firstSubCommand.equals(OPEN_SHOP)) {
            return executeOpenShop(administer, subCommand);
        }
        if (firstSubCommand.equals(CLOSE_SHOP)) {
            return executeCloseShop(administer, subCommand);
        }
        return false;
    }

    private boolean executeCreateShop(Player administer, String[] subCommand) {
        if (!hasValidSubCommandLength(subCommand, 3)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }

        String shopName = subCommand[1];
        int lineNum = Integer.parseInt(subCommand[2]);
        if (getShopNameList(ShopState.ALL_STATE_LIST).contains(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, EXISTING_SHOP + shopName);
            return false;
        }
        int insertShopCount = shopRepository.insertShop(shopName, lineNum);
        if (insertShopCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_INSERT_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_INSERT_SHOP + shopName);
        return true;
    }

    private boolean executeDeleteShop(Player administer, String[] subCommand) {
        if (!hasValidSubCommandLength(subCommand, 2)) {
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
        if (!hasValidSubCommandLength(subCommand, 7)) {
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

        int displayShopItemCount = displayShopItem(shopName, itemName, slotNumber, price, maxBuyableCnt,
                maxBuyableCntServer);
        if (displayShopItemCount == 0) {
            PlayerMessageSender.sendErrorMessage(administer, FAIL_DISPLAY_ITEM.formatted(shopName, itemName));
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, SUCCESS_DISPLAY_ITEM.formatted(shopName, itemName));
        return true;
    }

    private boolean executeDeleteItemInShop(Player administer, String[] subCommand) {
        if (!hasValidSubCommandLength(subCommand, 3)) {
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
        if (!hasValidSubCommandLength(subCommand, 2)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        String shopName = getShopName(subCommand[1]);
        if (!getShopNameList(ShopState.ALL_STATE_LIST).contains(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, UNDELETEABLE_SHOP + shopName);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(administer, getShopItemInfo(shopName));
        return true;
    }

    private boolean executeOpenShop(Player administer, String[] subCommand) {
        if (!hasValidSubCommandLength(subCommand, 2)) {
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
        if (hasValidSubCommandLength(subCommand, 1)) {
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
        shopItemList.forEach(shopItem -> shopItemInfo.append(shopItem.toString()).append("\n"));
        return shopItemInfo.toString();
    }

    private String getShopName(String shopNameInfo) {
        return shopNameInfo.replace("(닫힘)", "").replace("(오픈)", "").replace("(삭제됨)", "");
    }

    private boolean isItemExist(String itemName) {
        return getItemNameList().contains(itemName);
    }

    private List<String> getItemNameList() {
        List<ItemDto> itemDtoList = itemRepository.selectItem();
        return itemDtoList.stream().map(ItemDto::name).toList();
    }

    private int deleteShopItem(String shopName, int slotNumber) {
        return shopItemRepository.updateShopItemState(shopName, slotNumber, ShopState.DELETE_STATE.getStateNumber());
    }

    private int openShop(String shopName) {
        return shopRepository.updateShopState(shopName, ShopState.OPEN_STATE.getStateNumber());
    }

    private int closeShop(String shopName) {
        return shopRepository.updateShopState(shopName, ShopState.CLOSE_STATE.getStateNumber());
    }

    private int deleteShop(String shopName) {
        return shopRepository.updateShopState(shopName, ShopState.DELETE_STATE.getStateNumber());
    }

    private List<ShopItemDto> getShopItemList(String shopName) {
        return shopItemRepository.selectShopItemList(shopName);
    }

    private int displayShopItem(String shopName, String itemName, int slotNumber, int price, int maxBuyableCnt,
                        int maxBuyableCntServer) {
        ShopDto cashShop = shopRepository.selectShop(shopName);
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
        return shopItemRepository.insertShopItem(shopItem);
    }
}