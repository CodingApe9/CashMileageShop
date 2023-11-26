package org.codingape9.cashmileageshop.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;

import java.util.UUID;

public class PlayerUtil {
    private static final UserDto NOT_FOUND_USER = null;

    public static boolean isPlayerInDB(Player player, UserRepository userRepository) {
        UserDto user = userRepository.selectUser(player.getUniqueId());
        return user != NOT_FOUND_USER;
    }

    public static UUID getOnlineOrOfflinePlayerUUID(String playerName) {
        UUID playerUUID = getOnlinePlayerUUID(playerName);
        if (playerUUID != null) {
            return playerUUID;
        }
        return getOfflinePlayerUUID(playerName);
    }

    private static UUID getOnlinePlayerUUID(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return player.getUniqueId();
        }
        return null;
    }

    private static UUID getOfflinePlayerUUID(String playerName) {
        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }
}
