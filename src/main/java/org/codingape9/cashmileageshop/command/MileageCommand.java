package org.codingape9.cashmileageshop.command;

import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;

import java.util.UUID;

public class MileageCommand extends MoneyCommand {
    private static final int NOT_FOUND = -1;
    private static final String SELF_MILEAGE_FORMAT = "마일리지: §a%s§f원";
    private static final String PLAYER_MILEAGE_FORMAT = "§e%s§f님의 마일리지: §a%d§f원";

    @Override
    int getPlayerMoney(UUID playerUUID) {
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
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
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
        return userRepository.updateUserAddMileage(playerUUID, balance);
    }

    @Override
    int subPlayerMoney(UUID playerUUID, int balance) {
        UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
        return userRepository.updateUserSubMileage(playerUUID, balance);
    }
}
