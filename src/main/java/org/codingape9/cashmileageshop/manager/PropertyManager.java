package org.codingape9.cashmileageshop.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.gui.ShopGui;
import org.codingape9.cashmileageshop.view.PlayerMessage;
import org.codingape9.cashmileageshop.view.ServerConsole;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PropertyManager {
    private static PropertyManager propertyManager;

    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final String NO_SECTION_EXCEPTION_MESSAGE = "%s 파일에 %s 섹션을 작성해주세요.";

    private static final String CUSTOM_ITEM_INFO_SECTION_NAME = "custom_item_info";
    private static final Map<String, String> CUSTOM_ITEM_INFO_SECTION_NAME_MAP = Map.of("price", "§f가격: §e%price% §f캐시",
            "server_limited", "§f서버 한정: §e%server_remain%/%server_purchases_limited%",
            "user_limited", "§f개인 한정: §e%user_remain%/%user_purchases_limited%");

    private static final String SERVER_CUSTOM_MESSAGE_SECTION_NAME = "server_custom_message";
    private static final Map<String, String> SERVER_CUSTOM_MESSAGE_SECTION_MAP = Map.of("ServerErrorMessageHeader", "§4",
            "ServerSuccessMessageHeader", "§2");

    private static final String PLAYER_CUSTOM_MESSAGE_SECTION_NAME = "user_custom_message";
    private static final Map<String, String> PLAYER_CUSTOM_MESSAGE_SECTION_MAP = Map.of("sendFailMessageHeader", "§c◇§f",
            "sendSuccessMessageHeader", "§a◇§f");

    private PropertyManager() {
    }

    public static PropertyManager getInstance() {
        if (propertyManager == null) {
            propertyManager = new PropertyManager();
        }
        return propertyManager;
    }

    public void getCustomItemInfoProperty() {
        hasProperty(CUSTOM_ITEM_INFO_SECTION_NAME, CUSTOM_ITEM_INFO_SECTION_NAME_MAP);

        File configFile = new File(CashMileageShop.pluginFolder, CONFIG_FILE_NAME);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection customItemInfoConfig = config.getConfigurationSection(CUSTOM_ITEM_INFO_SECTION_NAME);

        ShopGui.PRICE = customItemInfoConfig.getString("price");
        ShopGui.SERVER_LIMITED = customItemInfoConfig.getString("server_limited");
        ShopGui.USER_LIMITED = customItemInfoConfig.getString("user_limited");
    }

    public void getServerCustomMessageProperty() {
        hasProperty(SERVER_CUSTOM_MESSAGE_SECTION_NAME, SERVER_CUSTOM_MESSAGE_SECTION_MAP);

        File configFile = new File(CashMileageShop.pluginFolder, CONFIG_FILE_NAME);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection customItemInfoConfig = config.getConfigurationSection(SERVER_CUSTOM_MESSAGE_SECTION_NAME);

        ServerConsole.SERVER_ERROR_MESSAGE_HEADER = customItemInfoConfig.getString("ServerErrorMessageHeader");
        ServerConsole.SERVER_SUCCESS_MESSAGE_HEADER = customItemInfoConfig.getString("ServerSuccessMessageHeader");
    }

    public void getUserCustomMessageProperty() {
        hasProperty(PLAYER_CUSTOM_MESSAGE_SECTION_NAME, PLAYER_CUSTOM_MESSAGE_SECTION_MAP);

        File configFile = new File(CashMileageShop.pluginFolder, CONFIG_FILE_NAME);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection customItemInfoConfig = config.getConfigurationSection(PLAYER_CUSTOM_MESSAGE_SECTION_NAME);

        PlayerMessage.PLAYER_ERROR_MESSAGE_HEADER = customItemInfoConfig.getString("sendFailMessageHeader");
        PlayerMessage.PLAYER_SUCCESS_MESSAGE_HEADER = customItemInfoConfig.getString("sendSuccessMessageHeader");
    }

    private void hasProperty(
            String propertyName,
            Map<String, String> defaultProperties
    ) {
        File configFile = new File(CashMileageShop.pluginFolder, CONFIG_FILE_NAME);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection customItemInfoConfig = config.getConfigurationSection(propertyName);

        if (customItemInfoConfig == null) {
            ConfigurationSection newCustomItemInfoSection = config.createSection(propertyName);
            defaultProperties.forEach(newCustomItemInfoSection::set);
            try {
                config.save(configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(NO_SECTION_EXCEPTION_MESSAGE.formatted(CONFIG_FILE_NAME, propertyName));
        }
    }
}
