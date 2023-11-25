package org.codingape9.cashmileageshop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.repository.ItemRepository;
import org.codingape9.cashmileageshop.util.ItemUtil;
import org.codingape9.cashmileageshop.view.PlayerMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemRegisterCommand implements CommandExecutor, TabCompleter {
    private static final String NO_ITEM_NAME = "아이템 이름을 입력해주세요.";
    private static final String NO_ITEM_IN_HAND = "손에 아이템을 들고 명령어를 사용해주세요.";
    private static final String FAIL_REGISTER_ITEM = "아이템 등록 실패: DB 연결 오류 혹은 이미 등록된 아이템 이름입니다.)";
    private static final String SUCCESS_REGISTER_ITEM = "아이템 등록 성공";
    private static final List<String> EMPTY_AUTOCOMPLETE = List.of();
    private static final List<String> SUB_COMMAND_AUTOCOMPLETE = List.of("아이템 이름");
    private static final int MAX_SUB_COMMAND_COUNT = 1;

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] subCommand
    ) {
        Player player = (Player) sender;

        if (!hasPermission(player)) {
            return EMPTY_AUTOCOMPLETE;
        }

        int subCommandCount = subCommand.length;
        if (subCommandCount > MAX_SUB_COMMAND_COUNT) {
            return EMPTY_AUTOCOMPLETE;
        }
        return SUB_COMMAND_AUTOCOMPLETE;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] subCommand
    ) {
        Player player = (Player) sender;

        if (!validateCondition(player, subCommand)) {
            return false;
        }

        ItemStack holdingItem = player.getInventory().getItemInMainHand();
        String itemName = subCommand[0];

        boolean isItemRegistered = registerItem(holdingItem, itemName);
        if (!isItemRegistered) {
            PlayerMessage.sendErrorMessage(player, FAIL_REGISTER_ITEM);
            return false;
        }
        PlayerMessage.sendSuccessMessage(player, SUCCESS_REGISTER_ITEM);
        return true;
    }

    private boolean registerItem(ItemStack itemStack, String itemName) {
        String itemInfo = ItemUtil.serialize(itemStack);
        ItemDto itemDto = new ItemDto(itemInfo, itemName);

        ItemRepository itemRepository = new ItemRepository(CashMileageShop.MYBATIS_MANAGER);
        int insertItemCount = itemRepository.insertItem(itemDto);
        return insertItemCount == 1;
    }

    private boolean validateCondition(Player player, @NotNull String[] subCommand) {
        if (!hasPermission(player)) {
            return false;
        }
        if (!checkArgs(subCommand)) {
            PlayerMessage.sendErrorMessage(player, NO_ITEM_NAME);
            return false;
        }
        if (!isHoldingItem(player)) {
            PlayerMessage.sendErrorMessage(player, NO_ITEM_IN_HAND);
            return false;
        }
        return true;
    }

    private boolean isHoldingItem(Player player) {
        return !player.getInventory().getItemInMainHand().getType().isAir();
    }

    private boolean hasPermission(Player player) {
        return player.isOp();
    }

    private boolean checkArgs(String[] subCommand) {
        return subCommand.length == 1;
    }
}
