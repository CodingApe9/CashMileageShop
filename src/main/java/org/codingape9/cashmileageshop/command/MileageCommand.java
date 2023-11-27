package org.codingape9.cashmileageshop.command;

import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;

import java.util.UUID;

public class MileageCommand extends MoneyCommand {
    private static final int NOT_FOUND = -1;
    private static final String COLOR_GREEN = "§a";
    private static final String COLOR_YELLOW = "§e";
    private static final String COLOR_WHITE = "§f";
    private static final String SELF_MILEAGE_FORMAT = "마일리지: " + COLOR_GREEN + "%s" + COLOR_WHITE + "원";
    private static final String PLAYER_MILEAGE_FORMAT =
            COLOR_YELLOW + "%s" + COLOR_WHITE + "님의 마일리지: " + COLOR_GREEN + "%d" + COLOR_WHITE + "원";

    private final UserRepository userRepository;

    public MileageCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    int getPlayerMoney(UUID playerUUID) {
        UserDto userDto = userRepository.selectUser(playerUUID);
        if (userDto == null) {
            return NOT_FOUND;
        }
        return userDto.mileage();
    }

    @Override
    String getMoneyConfirmMessage(int playerMoney) {
        return SELF_MILEAGE_FORMAT.formatted(playerMoney);
    }

    @Override
    String getMoneyConfirmMessage(String playerNickName, int playerMoney) {
        return PLAYER_MILEAGE_FORMAT.formatted(playerNickName, playerMoney);
    }

    @Override
    int setPlayerMoney(UUID playerUUID, int balance) {
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
        return userRepository.updateUserMileage(playerUUID, balance);
    }

    @Override
    int addPlayerMoney(UUID playerUUID, int balance) {
        return userRepository.updateUserAddMileage(playerUUID, balance);
    }

    @Override
    int subPlayerMoney(UUID playerUUID, int balance) {
        return userRepository.updateUserSubMileage(playerUUID, balance);
    }
}
