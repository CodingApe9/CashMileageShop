package org.codingape9.cashmileageshop.command;

import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;

import java.util.UUID;

public class CashCommand extends MoneyCommand {
    private static final int NOT_FOUND = -1;
    private static final String SELF_CASH_FORMAT = "캐시: §a%s§f원";
    private static final String PLAYER_CASH_FORMAT = "§e%s§f님의 캐시: §a%d§f원";


    @Override
    int getPlayerMoney(UUID playerUUID) {
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
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
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
        return userRepository.updateUserCash(playerUUID, balance);
    }

    @Override
    int addPlayerMoney(UUID playerUUID, int balance) {
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
        return userRepository.updateUserAddCash(playerUUID, balance);
    }

    @Override
    int subPlayerMoney(UUID playerUUID, int balance) {
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
        return userRepository.updateUserSubCash(playerUUID, balance);
    }
}
