package com.cdejong.nomorephantoms;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
                file.createNewFile();

                InputStream defaultConfigStream = plugin.getResource(ymlName);

                if (defaultConfigStream != null) {
                    Files.copy(defaultConfigStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

            } catch (Exception e) {
                //ignore
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
            plugin.getLogger().warning("§cCould not save §6" + this.fileName + "§c!");
        }
    }

    public void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }





}
