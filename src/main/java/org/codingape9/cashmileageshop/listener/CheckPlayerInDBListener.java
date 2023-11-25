package org.codingape9.cashmileageshop.listener;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;
import org.codingape9.cashmileageshop.util.PlayerUtil;
import org.codingape9.cashmileageshop.view.ServerConsole;

public class CheckPlayerInDBListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!PlayerUtil.isPlayerInDB(player)) {
            UserRepository userRepository = new UserRepository(CashMileageShop.MYBATIS_MANAGER);
            UserDto userDto = new UserDto(player.getUniqueId().toString());
            int insertUserCount = userRepository.insertUser(userDto);
            if (insertUserCount == 0) {
                ServerConsole.sendErrorMessage(player.getUniqueId().toString() + ": 플레이어가 DB에 추가되지 않았습니다.");
            }
        }
    }
}
