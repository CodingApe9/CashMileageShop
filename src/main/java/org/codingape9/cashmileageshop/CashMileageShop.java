package org.codingape9.cashmileageshop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.codingape9.cashmileageshop.manager.PropertyManager;

public final class CashMileageShop extends JavaPlugin {

    @Override
    public void onEnable() {
        PropertyManager propertyManager = PropertyManager.getInstance();
        propertyManager.createPluginFolder();



        Bukkit.getConsoleSender().sendMessage("§4[CashMileageShop] 플러그인이 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
