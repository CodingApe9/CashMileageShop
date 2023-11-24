package org.codingape9.cashmileageshop;

import org.bukkit.plugin.java.JavaPlugin;
import org.codingape9.cashmileageshop.manager.PropertyManager;
import org.codingape9.cashmileageshop.view.ServerConsole;

import java.io.File;

public final class CashMileageShop extends JavaPlugin {
    public static File pluginFolder;
    private static final String PLUGIN_ON = "플러그인이 활성화되었습니다.";
    private static final String PLUGIN_OFF = "플러그인이 비활성화되었습니다";

    @Override
    public void onEnable() {
        createPluginFolder();

        PropertyManager propertyManager = PropertyManager.getInstance();
        propertyManager.getCustomItemInfoProperty();
        propertyManager.getServerCustomMessageProperty();
        propertyManager.getUserCustomMessageProperty();

        ServerConsole.sendSuccessMessage(PLUGIN_ON);
    }

    @Override
    public void onDisable() {
        ServerConsole.sendSuccessMessage(PLUGIN_OFF);
    }

    public void createPluginFolder() {
        if (pluginFolder == null) {
            File folder = getDataFolder();
            if (!folder.exists()) {
                folder.mkdir();
            }
            pluginFolder = folder;
        }
    }
}
