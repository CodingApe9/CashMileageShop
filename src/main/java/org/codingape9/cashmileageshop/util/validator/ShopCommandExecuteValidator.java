package org.codingape9.cashmileageshop.util.validator;

import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.util.ShopMessageConstants;
import org.codingape9.cashmileageshop.view.PlayerMessageSender;

import java.util.List;

import static org.codingape9.cashmileageshop.util.ShopMessageConstants.*;

public class ShopCommandExecuteValidator {
    public boolean validateCreateShopSubCommand(Player administer, String[] subCommand, List<String> exisitShopNameList) {
        if (!ShopCommandValidator.hasValidSubCommandLength(subCommand, 3)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        if (exisitShopNameList.contains(subCommand[1])) {
            PlayerMessageSender.sendErrorMessage(administer, EXISTING_SHOP + subCommand[1]);
            return false;
        }
        return true;
    }

    public boolean validateDeleteShopSubCommand(Player administer, String[] subCommand, List<String> undeletedShopNameList) {
        if (!ShopCommandValidator.hasValidSubCommandLength(subCommand, 2)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        if (!undeletedShopNameList.contains(subCommand[1])) {
            PlayerMessageSender.sendErrorMessage(administer, ShopMessageConstants.UNDEFINED_SHOP + subCommand[1]);
            return false;
        }
        return true;
    }

    public boolean validateDisplayItemOnShopSubCommand(Player administer, String[] subCommand, List<String> undeletedShopNameList, List<String> itemNameList) {
        if (!ShopCommandValidator.hasValidSubCommandLength(subCommand, 7)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        if (!undeletedShopNameList.contains(subCommand[1])) {
            PlayerMessageSender.sendErrorMessage(administer, ShopMessageConstants.UNDEFINED_SHOP + subCommand[1]);
            return false;
        }
        if (!itemNameList.contains(subCommand[2])) {
            PlayerMessageSender.sendErrorMessage(administer, UNEXISTING_ITEM + subCommand[2]);
            return false;
        }
        return true;
    }

    public boolean validateDeleteItemInShopSubCommand(Player administer, String[] subCommand, List<String> undeletedShopNameList, List<Integer> shopItemSlotNumList) {
        if (!ShopCommandValidator.hasValidSubCommandLength(subCommand, 3)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        if (!undeletedShopNameList.contains(subCommand[1])) {
            PlayerMessageSender.sendErrorMessage(administer, ShopMessageConstants.UNDEFINED_SHOP + subCommand[1]);
            return false;
        }
        int tmpItemSlotNumber = Integer.parseInt(subCommand[2]);
        if (!shopItemSlotNumList.contains(tmpItemSlotNumber)) {
            PlayerMessageSender.sendErrorMessage(administer, UNEXISTING_ITEM_IN_SHOP.formatted(subCommand[1], tmpItemSlotNumber));
            return false;
        }
        return true;
    }

    public boolean validatePrintShopInfoSubCommand(Player administer, String[] subCommand, String shopName, List<String> shopNameList) {
        if (!ShopCommandValidator.hasValidSubCommandLength(subCommand, 2)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        if (!shopNameList.contains(shopName)) {
            PlayerMessageSender.sendErrorMessage(administer, ShopMessageConstants.UNDEFINED_SHOP + shopName);
            return false;
        }
        return true;
    }

    public boolean validateOpenShopSubCommand(Player administer, String[] subCommand, List<String> closedShopNameList) {
        if (!ShopCommandValidator.hasValidSubCommandLength(subCommand, 2)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        if (!closedShopNameList.contains(subCommand[1])) {
            PlayerMessageSender.sendErrorMessage(administer, UNDEFINED_SHOP + subCommand[1]);
            return false;
        }
        return true;
    }

    public boolean validateCloseShopSubCommand(Player administer, String[] subCommand, List<String> openedShopNameList) {
        if (ShopCommandValidator.hasValidSubCommandLength(subCommand, 1)) {
            PlayerMessageSender.sendErrorMessage(administer, WRONG_COMMAND);
            return false;
        }
        if (!openedShopNameList.contains(subCommand[1])) {
            PlayerMessageSender.sendErrorMessage(administer, UNDEFINED_SHOP + subCommand[1]);
            return false;
        }
        return true;
    }
}
