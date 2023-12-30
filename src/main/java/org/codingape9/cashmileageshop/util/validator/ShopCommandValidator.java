package org.codingape9.cashmileageshop.util.validator;

import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.view.PlayerMessageSender;

public class ShopCommandValidator {
    private static final String INVALID_COMMAND = "잘못된 명령어입니다.";

    public static boolean hasAdminPrivileges(Player player) {
        return player.isOp();
    }

    public static boolean hasValidSubCommandLength(String[] subCommand, int expectedLength) {
        return subCommand.length == expectedLength;
    }

    public static void sendInvalidCommandMessage(Player player) {
        PlayerMessageSender.sendErrorMessage(player, INVALID_COMMAND);
    }
}