package com.cdejong.nomorephantoms;

import com.cdejong.nomorephantoms.events.PhantomSpawnEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class NoMorePhantoms extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PhantomSpawnEvents(), this);
    }
}
