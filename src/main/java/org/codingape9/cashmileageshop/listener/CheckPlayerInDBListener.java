package org.codingape9.cashmileageshop.listener;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.codingape9.cashmileageshop.dto.UserDto;
import org.codingape9.cashmileageshop.repository.UserRepository;
import org.codingape9.cashmileageshop.util.PlayerUtil;
import org.codingape9.cashmileageshop.view.ServerConsole;

public class CheckPlayerInDBListener implements Listener {
    private static final String FAIL_INSERT_PLAYER = "플레이어 데이터를 DB에 저장하는데 실패했습니다. 플레이어 UUID: ";
    private final UserRepository userRepository;

    public CheckPlayerInDBListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!PlayerUtil.isPlayerInDB(player, userRepository)) {
            UserDto userDto = new UserDto(player.getUniqueId().toString());
            int insertUserCount = userRepository.insertUser(userDto);
            if (insertUserCount == 0) {
                ServerConsole.sendErrorMessage(FAIL_INSERT_PLAYER + player.getUniqueId());
            }
        }
    }
}

