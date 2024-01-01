package org.codingape9.cashmileageshop.command.shop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.codingape9.cashmileageshop.service.ShopService;
import org.codingape9.cashmileageshop.state.ShopState;
import org.codingape9.cashmileageshop.util.PlayerUtil;
import org.codingape9.cashmileageshop.util.validator.ShopCommandValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShopTabCompleter implements TabCompleter {
    private final ShopService shopService;

    private static final List<String> CREATE_SHOP_EXTRA_COMMAND = List.of("<이름> <줄 수>");
    private static final List<String> DISPLAY_ITEM_EXTRA_COMMAND = List.of("<슬롯번호> <가격> <최대구매가능개수(개인)> <최대구매가능개수(전역)>");
    private static final List<String> EMPTY_AUTOCOMPLETE = List.of();
    private static final String CREATE_SHOP = "생성";
    private static final String DELETE_SHOP = "삭제";
    private static final String DISPLAY_ITEM = "진열";
    private static final String DELETE_ITEM_IN_SHOP = "아이템삭제";
    private static final String DISPLAY_SHOP_LIST = "목록";
    private static final String OPEN_SHOP = "오픈";
    private static final String CLOSE_SHOP = "닫기";
    private static final List<String> FIRST_AUTOCOMPLETE = List.of(DELETE_SHOP, CREATE_SHOP, DISPLAY_ITEM,
            DELETE_ITEM_IN_SHOP, DISPLAY_SHOP_LIST, OPEN_SHOP, CLOSE_SHOP);
    private static final int NO_SUB_COMMAND = 1;

    public ShopTabCompleter(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] subCommand
    ) {
        if (!PlayerUtil.hasAdminPrivileges(sender)) {
            return EMPTY_AUTOCOMPLETE;
        }

        if (ShopCommandValidator.hasValidSubCommandLength(subCommand, NO_SUB_COMMAND)) {
            return FIRST_AUTOCOMPLETE;
        }

        return getAutoCompleteSubCommand(subCommand);
    }

    private List<String> getAutoCompleteSubCommand(String[] subCommand) {
        String firstSubCommand = subCommand[0];
        if (firstSubCommand.equals(CREATE_SHOP)) {
            return CREATE_SHOP_EXTRA_COMMAND;
        }
        if (firstSubCommand.equals(DELETE_SHOP)) {
            return shopService.getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);
        }
        if (firstSubCommand.equals(DISPLAY_ITEM)) {
            return getDisplayItemExtraCommand(subCommand);
        }
        if (firstSubCommand.equals(DELETE_ITEM_IN_SHOP)) {
            return getDeleteItemExtraCommand(subCommand);
        }
        if (firstSubCommand.equals(DISPLAY_SHOP_LIST)) {
            return shopService.getShopInfoList();
        }
        if (firstSubCommand.equals(OPEN_SHOP)) {
            return shopService.getShopNameList(ShopState.UNOPEN_SHOP_STATE_LIST);
        }
        if (firstSubCommand.equals(CLOSE_SHOP)) {
            return shopService.getShopNameList(ShopState.OPEN_SHOP_STATE_LIST);
        }

        return EMPTY_AUTOCOMPLETE;
    }

    private List<String> getDeleteItemExtraCommand(String[] subCommand) {
        int subCommandCount = subCommand.length;
        String inputShopName = subCommand[1];
        List<String> shopNameList = shopService.getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);

        if (subCommandCount > 2 && shopNameList.contains(inputShopName)) {
            return shopService.getShopItemSlotNumberList(inputShopName).stream().map(String::valueOf).toList();
        }
        return shopNameList;
    }

    private List<String> getDisplayItemExtraCommand(String[] subCommand) {
        int subCommandCount = subCommand.length;
        String inputShopName = subCommand[1];
        List<String> shopNameList = shopService.getShopNameList(ShopState.UNDELETED_SHOP_STATE_LIST);

        if (subCommandCount > 2 && shopNameList.contains(inputShopName)) {
            List<String> itemNameList = shopService.getItemNameList();
            String inputItemName = subCommand[2];

            if (subCommandCount > 3 && itemNameList.contains(inputItemName)) {
                return DISPLAY_ITEM_EXTRA_COMMAND;
            }
            return itemNameList;
        }
        return shopNameList;
    }
}
