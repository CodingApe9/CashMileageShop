package org.codingape9.cashmileageshop.view;

import org.bukkit.Bukkit;

public class ServerConsole {
    public static String SERVER_ERROR_MESSAGE_HEADER;
    public static String SERVER_SUCCESS_MESSAGE_HEADER;
    private static final String PLUGIN_NAME = "[CashMileageShop]: ";

    public static void sendErrorMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(SERVER_ERROR_MESSAGE_HEADER + PLUGIN_NAME + message);
    }

    public static void sendSuccessMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(SERVER_SUCCESS_MESSAGE_HEADER + PLUGIN_NAME + message);
    }
}
