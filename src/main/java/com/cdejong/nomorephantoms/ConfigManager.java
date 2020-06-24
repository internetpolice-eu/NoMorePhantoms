package com.cdejong.nomorephantoms;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private FileConfiguration fileConfig;
    private File file;
    private String fileName;
    private NoMorePhantoms plugin;

    public ConfigManager(NoMorePhantoms plugin, String ymlName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), ymlName);
        this.fileName = ymlName;

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                plugin.saveResource(ymlName, false);

            } catch (Exception e) {
                plugin.getLogger().warning("Could not create" + this.fileName + "!");
                e.printStackTrace();
            }
        }

        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return fileConfig;
    }

    public void saveConfig() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save " + this.fileName + "!");
        }
    }

    public void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }





}
