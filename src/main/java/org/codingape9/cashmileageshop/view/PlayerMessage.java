package org.codingape9.cashmileageshop.view;

import org.bukkit.entity.Player;

public class PlayerMessage {
    public static String PLAYER_ERROR_MESSAGE_HEADER;
    public static String PLAYER_SUCCESS_MESSAGE_HEADER;

    public static void sendErrorMessage(Player player, String message) {
        player.sendMessage(PLAYER_ERROR_MESSAGE_HEADER + message);
    }

    public static void sendSuccessMessage(Player player, String message) {
        player.sendMessage(PLAYER_SUCCESS_MESSAGE_HEADER + message);
    }
}
