package org.codingape9.cashmileageshop.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.codingape9.cashmileageshop.CashMileageShop;
import org.codingape9.cashmileageshop.gui.ShopGui;
import org.codingape9.cashmileageshop.util.ConfigLoader;
import org.codingape9.cashmileageshop.view.PlayerMessageSender;
import org.codingape9.cashmileageshop.view.ServerConsole;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class PropertyManager {
    private static PropertyManager propertyManager;

    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final String NO_SECTION_EXCEPTION_MESSAGE = "%s 파일에 %s 섹션을 작성해주세요.";

    private static final String CUSTOM_ITEM_INFO_SECTION_NAME = "custom_item_info";
    private static final Map<String, String> CUSTOM_ITEM_INFO_SECTION_MAP = Map.of("price", "§f가격: §e%price% §f캐시",
            "server_limited", "§f서버 한정: §e%server_remain%/%server_purchases_limited%",
            "user_limited", "§f개인 한정: §e%user_remain%/%user_purchases_limited%");

    private static final String SERVER_CUSTOM_MESSAGE_SECTION_NAME = "server_custom_message";
    private static final Map<String, String> SERVER_CUSTOM_MESSAGE_SECTION_MAP = Map.of("ServerErrorMessageHeader",
            "§4",
            "ServerSuccessMessageHeader", "§2");

    private static final String PLAYER_CUSTOM_MESSAGE_SECTION_NAME = "user_custom_message";
    private static final Map<String, String> PLAYER_CUSTOM_MESSAGE_SECTION_MAP = Map.of("sendFailMessageHeader",
            "§c◇§f",
            "sendSuccessMessageHeader", "§a◇§f");

    private PropertyManager() {
    }

    public static PropertyManager getInstance() {
        if (propertyManager == null) {
            propertyManager = new PropertyManager();
        }
        return propertyManager;
    }


    public void loadProperties() {
        loadProperty(CUSTOM_ITEM_INFO_SECTION_NAME, CUSTOM_ITEM_INFO_SECTION_MAP, this::assignCustomItemInfo);
        loadProperty(SERVER_CUSTOM_MESSAGE_SECTION_NAME, SERVER_CUSTOM_MESSAGE_SECTION_MAP,
                this::assignServerCustomMessage);
        loadProperty(PLAYER_CUSTOM_MESSAGE_SECTION_NAME, PLAYER_CUSTOM_MESSAGE_SECTION_MAP,
                this::assignPlayerCustomMessage);
    }

    private void loadProperty(String sectionName, Map<String, String> defaultValues,
                              Consumer<ConfigurationSection> assignFunction) {
        hasProperty(sectionName, defaultValues);
        File configFile = new File(CashMileageShop.PLUGIN_FOLDER, CONFIG_FILE_NAME);
        YamlConfiguration config = ConfigLoader.loadConfig(configFile);
        ConfigurationSection section = config.getConfigurationSection(sectionName);
        assignFunction.accept(section);
    }

    private void assignCustomItemInfo(ConfigurationSection section) {
        ShopGui.PRICE = section.getString("price");
        ShopGui.SERVER_LIMITED = section.getString("server_limited");
        ShopGui.USER_LIMITED = section.getString("user_limited");
    }

    private void assignServerCustomMessage(ConfigurationSection section) {
        ServerConsole.SERVER_ERROR_MESSAGE_HEADER = section.getString("ServerErrorMessageHeader");
        ServerConsole.SERVER_SUCCESS_MESSAGE_HEADER = section.getString("ServerSuccessMessageHeader");
    }

    private void assignPlayerCustomMessage(ConfigurationSection section) {
        PlayerMessageSender.PLAYER_ERROR_MESSAGE_HEADER = section.getString("sendFailMessageHeader");
        PlayerMessageSender.PLAYER_SUCCESS_MESSAGE_HEADER = section.getString("sendSuccessMessageHeader");
    }

    private void hasProperty(String propertyName, Map<String, String> defaultProperties) {
        File configFile = new File(CashMileageShop.PLUGIN_FOLDER, CONFIG_FILE_NAME);
        YamlConfiguration config = loadConfiguration(configFile);

        if (!existsProperty(config, propertyName)) {
            addDefaultProperties(config, configFile, propertyName, defaultProperties);
        }
    }

    private YamlConfiguration loadConfiguration(File configFile) {
        return ConfigLoader.loadConfig(configFile);
    }

    private boolean existsProperty(YamlConfiguration config, String propertyName) {
        return config.getConfigurationSection(propertyName) != null;
    }

    private void addDefaultProperties(YamlConfiguration config, File configFile, String propertyName,
                                      Map<String, String> defaultProperties) {
        ConfigurationSection section = config.createSection(propertyName);
        defaultProperties.forEach(section::set);
        saveConfiguration(config, configFile);
        throw new RuntimeException(NO_SECTION_EXCEPTION_MESSAGE.formatted(CONFIG_FILE_NAME, propertyName));
    }

    private void saveConfiguration(YamlConfiguration config, File configFile) {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
