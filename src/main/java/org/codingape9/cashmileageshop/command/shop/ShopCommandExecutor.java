package org.codingape9.cashmileageshop.command.shop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.dto.ShopDto;
import org.codingape9.cashmileageshop.dto.ShopItemDto;
import org.codingape9.cashmileageshop.service.ShopService;
import org.codingape9.cashmileageshop.state.ShopState;
import org.codingape9.cashmileageshop.util.PlayerUtil;
import org.codingape9.cashmileageshop.util.validator.ShopCommandExecuteValidator;
import org.codingape9.cashmileageshop.util.validator.ShopCommandValidator;
import org.codingape9.cashmileageshop.view.PlayerMessageSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.codingape9.cashmileageshop.util.ShopMessageConstants.WRONG_COMMAND;

public class ShopCommandExecutor implements CommandExecutor {
    private final ShopService shopService;
    private final ShopCommandExecuteValidator shopCommandExecuteValidator;

    private static final int NO_SUB_COMMAND = 1;
    private static final String CREATE_SHOP = "생성";
    private static final String DELETE_SHOP = "삭제";
    private static final String DISPLAY_ITEM = "진열";
    private static final String DELETE_ITEM_IN_SHOP = "아이템삭제";
    private static final String DISPLAY_SHOP_LIST = "목록";
    private static final String OPEN_SHOP = "오픈";
    private static final String CLOSE_SHOP = "닫기";

    public ShopCommandExecutor(ShopService shopService, ShopCommandExecuteValidator shopCommandExecuteValidator) {
        this.shopService = shopService;
        this.shopCommandExecuteValidator = shopCommandExecuteValidator;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] subCommand
    ) {
        if (!PlayerUtil.hasAdminPrivileges(sender)) {
            return false;
        }
        Player administer = (Player) sender;

        if (ShopCommandValidator.hasValidSubCommandLength(subCommand, NO_SUB_COMMAND)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }

        return executeShopOpCommand(administer, subCommand);
    }

    private boolean executeShopOpCommand(Player administer, String[] subCommand) {
        String firstSubCommand = subCommand[0];
        if (firstSubCommand.equals(CREATE_SHOP)) {
            return executeCreateShopCommand(administer, subCommand);
        }
        if (firstSubCommand.equals(DELETE_SHOP)) {
            return executeDeleteShop(administer, subCommand);
        }
        if (firstSubCommand.equals(DISPLAY_ITEM)) {
            return executeDisplayItemOnShop(administer, subCommand);
        }
        if (firstSubCommand.equals(DELETE_ITEM_IN_SHOP)) {
            return executeDeleteItemInShop(administer, subCommand);
        }
        if (firstSubCommand.equals(DISPLAY_SHOP_LIST)) {
            return executePrintShopInfo(administer, subCommand);
        }
        if (firstSubCommand.equals(OPEN_SHOP)) {
            return executeOpenShop(administer, subCommand);
        }
        if (firstSubCommand.equals(CLOSE_SHOP)) {
            return executeCloseShop(administer, subCommand);
        }
        return false;
    }

    private boolean executeCreateShopCommand(Player administer, String[] subCommand) {
        List<String> exisitShopNameList = shopService.getShopNameList(ShopState.ALL_STATE_LIST);
        if (!shopCommandExecuteValidator.validateCreateShopSubCommand(administer, subCommand, exisitShopNameList)) {
            return false;
        }

        String shopName = subCommand[1];
        int lineNum = Integer.parseInt(subCommand[2]);
        return shopService.createShop(administer, shopName, lineNum);
    }

    private boolean executeDeleteShop(Player administer, String[] subCommand) {
        List<String> undeletedShopNameList = shopService.getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);
        if (!shopCommandExecuteValidator.validateDeleteShopSubCommand(administer, subCommand, undeletedShopNameList)) {
            return false;
        }

        String shopName = subCommand[1];
        return shopService.deleteShop(administer, shopName);
    }

    private boolean executeDisplayItemOnShop(Player administer, String[] subCommand) {
        List<String> undeletedShopNameList = shopService.getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);
        List<String> itemNameList = shopService.getItemNameList();
        if (!shopCommandExecuteValidator.validateDisplayItemOnShopSubCommand(administer, subCommand, undeletedShopNameList, itemNameList)) {
            return false;
        }

        ShopDto shop = shopService.getShop(subCommand[1]);
        ItemDto item = shopService.getItem(subCommand[2]);
        ShopItemDto shopItem = new ShopItemDto(
                item.id(),
                shop.id(),
                Integer.parseInt(subCommand[4]),
                ShopState.CLOSE_STATE.getStateNumber(),
                Integer.parseInt(subCommand[3]),
                Integer.parseInt(subCommand[5]),
                Integer.parseInt(subCommand[6])
        );
        return shopService.displayItemOnShop(administer, shop, item, shopItem);
    }

    private boolean executeDeleteItemInShop(Player administer, String[] subCommand) {
        List<String> undeletedShopNameList = shopService.getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);
        List<Integer> shopItemSlotNumList = shopService.getShopItemSlotNumberList(subCommand[1]);
        if (!shopCommandExecuteValidator.validateDeleteItemInShopSubCommand(administer, subCommand, undeletedShopNameList, shopItemSlotNumList)) {
            return false;
        }

        String shopName = subCommand[1];
        int slotNum = Integer.parseInt(subCommand[2]);
        return shopService.deleteItemInShop(administer, shopName, slotNum);
    }

    private boolean executePrintShopInfo(Player administer, String[] subCommand) {
        List<String> shopNameList = shopService.getShopNameList(ShopState.ALL_STATE_LIST);
        if (!shopCommandExecuteValidator.validatePrintShopInfoSubCommand(administer, subCommand, shopNameList)) {
            return false;
        }

        return shopService.printShopInfo(administer, subCommand[1]);
    }

    private boolean executeOpenShop(Player administer, String[] subCommand) {
        List<String> closedShopNameList = shopService.getShopNameList(ShopState.UNOPEN_SHOP_STATE_LIST);
        if (!shopCommandExecuteValidator.validateOpenShopSubCommand(administer, subCommand, closedShopNameList)) {
            return false;
        }

        String shopName = subCommand[1];
        return shopService.openShop(administer, shopName);
    }

    private boolean executeCloseShop(Player administer, String[] subCommand) {
        List<String> openedShopNameList = shopService.getShopNameList(ShopState.OPEN_SHOP_STATE_LIST);
        if (!shopCommandExecuteValidator.validateCloseShopSubCommand(administer, subCommand, openedShopNameList)) {
            return false;
        }

        String shopName = subCommand[1];
        return shopService.closeShop(administer, shopName);
    }
}
