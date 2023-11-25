package org.codingape9.cashmileageshop.util;

import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;

public class PlayerUtil {
    private static final UserDto NOT_FOUND_USER = null;

    public static boolean isPlayerInDB(Player player) {
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);

        UserDto user = userRepository.selectUser(player.getUniqueId());
        if (user == NOT_FOUND_USER) {
            return false;
        }
        return true;
    }
}
