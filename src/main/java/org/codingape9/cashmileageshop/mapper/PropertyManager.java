package org.codingape9.cashmileageshop.mapper;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PropertyManager {
    private static PropertyManager propertyManager;

    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final String CONFIGURATION_SECTION_NAME = "custom_item_info";
    private static final Map<String, String> CONFIGURATION_SECTION_MAP = Map.of("price", "§f가격: §e%price% §f캐시",
            "server_limited", "§f서버 한정: §e%server_remain%/%server_purchases_limited%",
            "user_limited", "§f개인 한정: §e%user_remain%/%user_purchases_limited%");

    private File pluginFolder;

    private PropertyManager() {
    }

    public static PropertyManager getInstance() {
        if (propertyManager == null) {
            propertyManager = new PropertyManager();
        }
        return propertyManager;
    }

    public void getCustomItemInfoProperty() {
        hasProperty(CONFIGURATION_SECTION_NAME, CONFIGURATION_SECTION_MAP);
        CONFIGURATION_SECTION_MAP.keySet().forEach( key -> {
            //아이템에 커스텀 메시지
        });
    }

    private void hasProperty(
            String propertyName,
            Map<String, String> defaultProperties
    ) {
        File configFile = new File(pluginFolder, CONFIG_FILE_NAME);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection customItemInfo = config.getConfigurationSection(CONFIGURATION_SECTION_NAME);

        if (customItemInfo == null) {
            ConfigurationSection newCustomItemInfoSection = config.createSection(propertyName);
            defaultProperties.forEach(newCustomItemInfoSection::set);
            try {
                config.save(configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createPluginFolder() {
        if (pluginFolder == null) {
            File folder = Bukkit.getPluginsFolder();
            if (!folder.exists()) {
                folder.mkdir();
            }
            pluginFolder = folder;
        }
    }
}
