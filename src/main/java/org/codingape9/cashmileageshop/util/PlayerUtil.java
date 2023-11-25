package org.codingape9.cashmileageshop.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;

import java.util.UUID;

public class PlayerUtil {
    private static final UserDto NOT_FOUND_USER = null;

    public static boolean isPlayerInDB(Player player) {
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);

        UserDto user = userRepository.selectUser(player.getUniqueId());
        return user != NOT_FOUND_USER;
    }

    public static UUID getPlayerUUID(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return player.getUniqueId();
        }
        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }
}
