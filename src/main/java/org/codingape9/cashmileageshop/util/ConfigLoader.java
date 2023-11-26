package org.codingape9.cashmileageshop.util;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigLoader {

    public static YamlConfiguration loadConfig(File configFile) {
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
