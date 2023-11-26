package org.codingape9.cashmileageshop.command;

import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;

import java.util.UUID;

public class CashCommand extends MoneyCommand {
    private static final int NOT_FOUND = -1;
    private static final String COLOR_GREEN = "§a";
    private static final String COLOR_YELLOW = "§e";
    private static final String COLOR_WHITE = "§f";
    private static final String SELF_CASH_FORMAT = "캐시: " + COLOR_GREEN + "%s" + COLOR_WHITE + "원";
    private static final String PLAYER_CASH_FORMAT =
            COLOR_YELLOW + "%s" + COLOR_WHITE + "님의 캐시: " + COLOR_GREEN + "%d" + COLOR_WHITE + "원";
    private final UserRepository userRepository;

    public CashCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    int getPlayerMoney(UUID playerUUID) {
        UserDto userDto = userRepository.selectUser(playerUUID);
        if (userDto == null) {
            return NOT_FOUND;
        }
        return userDto.cash();
    }

    @Override
    String getMoneyConfirmMessage(int playerMoney) {
        return SELF_CASH_FORMAT.formatted(playerMoney);
    }

    @Override
    String getMoneyConfirmMessage(String playerNickName, int playerMoney) {
        return PLAYER_CASH_FORMAT.formatted(playerNickName, playerMoney);
    }

    @Override
    int setPlayerMoney(UUID playerUUID, int balance) {
        return userRepository.updateUserCash(playerUUID, balance);
    }

    @Override
    int addPlayerMoney(UUID playerUUID, int balance) {
        return userRepository.updateUserAddCash(playerUUID, balance);
    }

    @Override
    int subPlayerMoney(UUID playerUUID, int balance) {
        return userRepository.updateUserSubCash(playerUUID, balance);
    }
}
