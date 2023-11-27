package org.codingape9.cashmileageshop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.codingape9.cashmileageshop.util.PlayerUtil;
import org.codingape9.cashmileageshop.view.PlayerMessageSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class MoneyCommand implements CommandExecutor, TabCompleter {
    private static final List<String> PLAYER_NAME_AUTOCOMPLETE = null;
    private static final List<String> EMPTY_AUTOCOMPLETE = List.of();
    private static final List<String> FIRST_AUTOCOMPLETE = List.of("확인", "설정", "지급", "차감");
    private static final List<String> OTHER_COMMAND = List.of("설정", "지급", "차감");
    private static final int SET_MONEY = 0;
    private static final int ADD_MONEY = 1;
    private static final int SUB_MONEY = 2;
    private static final List<String> THIRD_AUTOCOMPLETE = List.of("<금액>");
    private static final int CHECK_MONEY = 0;
    private static final int MAX_SUB_COMMAND_COUNT = 3;
    private static final int NOT_FOUND = -1;
    private static final int UPDATE_FAIL = 0;
    private static final String FAIL_FOUND_PLAYER_ERROR_MESSAGE = "잠시 후 다시 시도해주세요.";
    private static final String BALANCE_MINUS_ERROR_MESSAGE = "0보다 작은 금액은 설정할 수 없습니다.";
    private static final String BALANCE_NOT_NUMBER_ERROR_MESSAGE = "금액은 1이상의 숫자로 입력해주세요.";
    private static final String FAIL_OP_COMMAND_EXECUTE = "관리자 명령어를 실행하는데 실패했습니다.";

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] subCommand
    ) {
        Player player = (Player) sender;
        if (!player.isOp()) {
            return PLAYER_NAME_AUTOCOMPLETE;
        }

        return getAutoCompleteSubCommand(subCommand);
    }

    private List<String> getAutoCompleteSubCommand(String[] subCommand) {
        int subCommandCount = subCommand.length;
        String firstSubCommand = subCommand[0];
        if (subCommandCount > MAX_SUB_COMMAND_COUNT ||
                subCommandCount == MAX_SUB_COMMAND_COUNT && firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(CHECK_MONEY))) {
            return EMPTY_AUTOCOMPLETE;
        }
        if (subCommandCount == 1) {
            return FIRST_AUTOCOMPLETE;
        }
        if (subCommandCount == MAX_SUB_COMMAND_COUNT && !firstSubCommand.equals(FIRST_AUTOCOMPLETE.get(CHECK_MONEY))) {
            return THIRD_AUTOCOMPLETE;
        }

        return PLAYER_NAME_AUTOCOMPLETE;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] subCommand
    ) {
        Player player = (Player) sender;

        if (isCheckSelfMoney(subCommand)) {
            return printSelfMoney(player);
        }

        if (!player.isOp()) {
            return false;
        }
        if (isCheckOtherPlayerMoney(subCommand)) {
            return printOtherPlayerMoney(subCommand, player);
        }
        if (isOtherCommand(subCommand)) {
            return executeMoneyOpCommand(subCommand, player);
        }

        PlayerMessageSender.sendErrorMessage(player, FAIL_OP_COMMAND_EXECUTE);
        return false;
    }

    private boolean executeMoneyOpCommand(@NotNull String[] subCommand, Player player) {
        String playerNickName = subCommand[1];
        UUID playerUUID = PlayerUtil.getOnlineOrOfflinePlayerUUID(playerNickName);
        int balance = getValidateBalance(player, subCommand[2]);
        if (balance == NOT_FOUND) {
            return false;
        }

        String coreCommand = subCommand[0];
        if (coreCommand.equals(OTHER_COMMAND.get(SET_MONEY))) {
            if (setPlayerMoney(playerUUID, balance) == UPDATE_FAIL) {
                PlayerMessageSender.sendErrorMessage(player, FAIL_OP_COMMAND_EXECUTE);
                return false;
            }
        }
        if (coreCommand.equals(OTHER_COMMAND.get(ADD_MONEY))) {
            if (addPlayerMoney(playerUUID, balance) == UPDATE_FAIL) {
                PlayerMessageSender.sendErrorMessage(player, FAIL_OP_COMMAND_EXECUTE);
                return false;
            }
        }
        if (coreCommand.equals(OTHER_COMMAND.get(SUB_MONEY))) {
            if (subPlayerMoney(playerUUID, balance) == UPDATE_FAIL) {
                PlayerMessageSender.sendErrorMessage(player, FAIL_OP_COMMAND_EXECUTE);
                return false;
            }
        }
        printOtherPlayerMoney(subCommand, player);
        return true;
    }

    private int getValidateBalance(Player player, String balance) {
        int validateBalance;
        try {
            validateBalance = Integer.parseInt(balance);
        } catch (NumberFormatException e) {
            PlayerMessageSender.sendErrorMessage(player, BALANCE_NOT_NUMBER_ERROR_MESSAGE);
            return NOT_FOUND;
        }
        if (validateBalance < 0) {
            PlayerMessageSender.sendErrorMessage(player, BALANCE_MINUS_ERROR_MESSAGE);
            return NOT_FOUND;
        }
        return validateBalance;
    }

    private boolean printOtherPlayerMoney(@NotNull String[] subCommand, Player player) {
        String playerNickName = subCommand[1];
        UUID playerUUID = PlayerUtil.getOnlineOrOfflinePlayerUUID(playerNickName);
        int playerMoney = getPlayerMoney(playerUUID);
        if (playerMoney == NOT_FOUND) {
            PlayerMessageSender.sendErrorMessage(player, FAIL_FOUND_PLAYER_ERROR_MESSAGE);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(player, getMoneyConfirmMessage(playerNickName, playerMoney));
        return true;
    }

    private boolean printSelfMoney(Player player) {
        int playerMoney = getPlayerMoney(player.getUniqueId());
        if (playerMoney == NOT_FOUND) {
            PlayerMessageSender.sendErrorMessage(player, FAIL_FOUND_PLAYER_ERROR_MESSAGE);
            return false;
        }
        PlayerMessageSender.sendSuccessMessage(player, getMoneyConfirmMessage(playerMoney));
        return true;
    }

    private boolean isCheckSelfMoney(String[] subCommand) {
        return subCommand.length == 0;
    }

    private boolean isCheckOtherPlayerMoney(String[] subCommand) {
        return subCommand.length == 2 && subCommand[0].equals(FIRST_AUTOCOMPLETE.get(CHECK_MONEY));
    }

    private boolean isOtherCommand(String[] subCommand) {
        return subCommand.length == 3 && OTHER_COMMAND.contains(subCommand[0]);
    }

    abstract int getPlayerMoney(UUID playerUUID);

    abstract String getMoneyConfirmMessage(int playerMoney);

    abstract String getMoneyConfirmMessage(String playerNickName, int playerMoney);

    abstract int setPlayerMoney(UUID playerUUID, int balance);

    abstract int addPlayerMoney(UUID playerUUID, int balance);

    abstract int subPlayerMoney(UUID playerUUID, int balance);
}
