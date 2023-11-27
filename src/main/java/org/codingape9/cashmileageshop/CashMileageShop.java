package org.codingape9.cashmileageshop;

import org.bukkit.plugin.java.JavaPlugin;
import org.codingape9.cashmileageshop.command.CashCommand;
import org.codingape9.cashmileageshop.command.CashShopCommand;
import org.codingape9.cashmileageshop.command.ItemRegisterCommand;
import org.codingape9.cashmileageshop.command.MileageCommand;
import org.codingape9.cashmileageshop.listener.CheckPlayerInDBListener;
import org.codingape9.cashmileageshop.manager.MyBatisManager;
import org.codingape9.cashmileageshop.manager.PropertyManager;
import org.codingape9.cashmileageshop.repository.CashItemRepository;
import org.codingape9.cashmileageshop.repository.CashShopRepository;
import org.codingape9.cashmileageshop.repository.ItemRepository;
import org.codingape9.cashmileageshop.repository.UserRepository;
import org.codingape9.cashmileageshop.view.ServerConsole;

import java.io.File;

public final class CashMileageShop extends JavaPlugin {
    public static File PLUGIN_FOLDER;
    public static MyBatisManager MYBATIS_MANAGER;
    private static final String PLUGIN_ON = "플러그인이 활성화되었습니다.";
    private static final String PLUGIN_OFF = "플러그인이 비활성화되었습니다";
    private static final String PLUGIN_FOLDER_NOT_EXIST = "플러그인 폴더가 존재하지 않습니다.";
    private static final File NOT_EXIST_PLUGIN_FOLDER = null;

    @Override
    public void onEnable() {
        createPluginFolder();
        MYBATIS_MANAGER = new MyBatisManager();

        PropertyManager propertyManager = PropertyManager.getInstance();
        propertyManager.loadProperties();

        getCommand("아이템등록").setExecutor(new ItemRegisterCommand(new ItemRepository(MYBATIS_MANAGER)));
        getCommand("캐시").setExecutor(new CashCommand(new UserRepository(MYBATIS_MANAGER)));
        getCommand("캐시샵").setExecutor(new CashShopCommand(
                new ItemRepository(MYBATIS_MANAGER),
                new CashShopRepository(MYBATIS_MANAGER),
                new CashItemRepository(MYBATIS_MANAGER))
        );

        getCommand("마일리지").setExecutor(new MileageCommand(new UserRepository(MYBATIS_MANAGER)));

        getServer().getPluginManager().registerEvents(new CheckPlayerInDBListener(new UserRepository(MYBATIS_MANAGER)), this);

        ServerConsole.sendSuccessMessage(PLUGIN_ON);
    }

    @Override
    public void onDisable() {
        ServerConsole.sendSuccessMessage(PLUGIN_OFF);
    }

    public void createPluginFolder() {
        if (PLUGIN_FOLDER == NOT_EXIST_PLUGIN_FOLDER) {
            File folder = getDataFolder();
            if (!folder.exists()) {
                folder.mkdir();
                ServerConsole.sendErrorMessage(PLUGIN_FOLDER_NOT_EXIST);
            }
            PLUGIN_FOLDER = folder;
        }
    }
}
