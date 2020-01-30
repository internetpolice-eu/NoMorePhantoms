package com.cdejong.nomorephantoms;

import events.onPhantomSpawn;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoMorePhantoms extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new onPhantomSpawn(this), this);
        Bukkit.getLogger().info("Starting...");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
