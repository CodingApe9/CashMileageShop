package org.codingape9.cashmileageshop.view;

import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.util.ShopMessageConstants;

public class PlayerMessageSender {
    public static String PLAYER_ERROR_MESSAGE_HEADER;
    public static String PLAYER_SUCCESS_MESSAGE_HEADER;

    public static void sendErrorMessage(Player player, String message) {
        player.sendMessage(PLAYER_ERROR_MESSAGE_HEADER + message);
    }

    public static void sendSuccessMessage(Player player, String message) {
        player.sendMessage(PLAYER_SUCCESS_MESSAGE_HEADER + message);
    }

    public static void sendSuccessMessage(Player player, ShopMessageConstants message) {
        player.sendMessage(message.toString());
    }

    public static void sendErrorMessage(Player player, ShopMessageConstants message) {
        player.sendMessage(message.toString());
    }
}
