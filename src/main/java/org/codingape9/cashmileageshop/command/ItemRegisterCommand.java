package org.codingape9.cashmileageshop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.ItemDto;
import org.codingape9.cashmileageshop.repository.ItemRepository;
import org.codingape9.cashmileageshop.util.ItemUtil;
import org.codingape9.cashmileageshop.view.PlayerMessage;
import org.jetbrains.annotations.NotNull;

public class ItemRegisterCommand implements CommandExecutor {
    private static final String NO_ITEM_NAME = "아이템 이름을 입력해주세요.";
    private static final String NO_ITEM_IN_HAND = "손에 아이템을 들고 명령어를 사용해주세요.";
    private static final String FAIL_REGISTER_ITEM = "아이템 등록 실패: DB 연결 오류 혹은 이미 등록된 아이템 이름입니다.)";
    private static final String SUCCESS_REGISTER_ITEM = "아이템 등록 성공";

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        Player player = (Player) sender;

        if (!validateCondition(args, player)) {
            return false;
        }

        ItemStack holdingItem = player.getInventory().getItemInMainHand();
        String itemName = args[0];

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

    private boolean validateCondition(@NotNull String[] args, Player player) {
        if (!hasPermission(player)) {
            return false;
        }
        if (!checkArgs(args)) {
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

    private boolean checkArgs(String[] args) {
        return args.length == 1;
    }
}
