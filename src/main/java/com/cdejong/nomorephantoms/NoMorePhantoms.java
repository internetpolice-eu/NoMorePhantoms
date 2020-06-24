package com.cdejong.nomorephantoms;

import com.cdejong.nomorephantoms.commands.DisablePhantomSpawnsCommand;
import com.cdejong.nomorephantoms.commands.NoMorePhantomsCommand;
import com.cdejong.nomorephantoms.events.PhantomSpawnEvents;
import com.cdejong.nomorephantoms.hooks.HookManager;
import com.cdejong.nomorephantoms.hooks.LuckPermsHook;
import com.cdejong.nomorephantoms.hooks.PluginHook;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class NoMorePhantoms extends JavaPlugin {
    private HookManager hookManager;

    private ConfigManager mainConfig = new ConfigManager(this, "config.yml");
    private ConfigManager phantomStatesConfig = new ConfigManager(this, "phantom-spawn-states.yml");


    @Override
    public void onEnable() {
        hookManager = new HookManager(this);
        hookManager.loadPluginHook(new LuckPermsHook(this));

        getServer().getPluginManager().registerEvents(new PhantomSpawnEvents(this), this);

        getCommand("phantom").setExecutor(new DisablePhantomSpawnsCommand(this));
        getCommand("nomorephantom").setExecutor(new NoMorePhantomsCommand(this));
    }

    public void togglePhantomSpawnState(Player player, CommandSender sender) {
        if (isNoPhantomSpawnsEnabled(player)) {
            setPhantomSpawnState(player, sender, false);
            return;
        }
        setPhantomSpawnState(player, sender, true);
    }

    public void setPhantomSpawnState(Player player, CommandSender sender, boolean isNoPhantomSpawns) {
        // LuckyPerms
        Optional<PluginHook> hook = getHookManager().getPluginHook("LuckPerms");
        if (storeInLuckyPerms() && hook.isPresent()) {

            LuckPermsHook lpHook = (LuckPermsHook) hook.get();

            lpHook.setUserPhantomState(player, isNoPhantomSpawns);
        }

        // yaml (default)
        List<String> noPhantomSpawnsFor = phantomStatesConfig.getConfig().getStringList("no-phantom-spawns-enabled-for");
        if (!noPhantomSpawnsFor.contains(player.getUniqueId().toString()) && isNoPhantomSpawns) {
            noPhantomSpawnsFor.add(player.getUniqueId().toString());
            phantomStatesConfig.getConfig().set("no-phantom-spawns-enabled-for", noPhantomSpawnsFor);

        } else if (noPhantomSpawnsFor.contains(player.getUniqueId().toString()) && !isNoPhantomSpawns) {
            noPhantomSpawnsFor.remove(player.getUniqueId().toString());
            phantomStatesConfig.getConfig().set("no-phantom-spawns-enabled-for", noPhantomSpawnsFor);
        }

        phantomStatesConfig.saveConfig();
        sendState(player, sender);
    }

    public boolean isNoPhantomSpawnsEnabled(Player player) {
        // LuckyPerms
        Optional<PluginHook> hook = getHookManager().getPluginHook("LuckPerms");
        if (storeInLuckyPerms() && hook.isPresent()) {
            LuckPermsHook lpHook = (LuckPermsHook) hook.get();

            return lpHook.isNoPhantomSpawnsEnabled(player);
        }

        // yaml (default)
        List<String> noPhantomSpawnsFor = phantomStatesConfig.getConfig().getStringList("no-phantom-spawns-enabled-for");
        return noPhantomSpawnsFor.contains(player.getUniqueId().toString());
    }

    private void sendState(Player player, CommandSender sender) {
        String state = isNoPhantomSpawnsEnabled(player) ? "enabled" : "disabled";

        if (player == sender) {
            sender.sendMessage(String.format("§6No phantom spawns §c%s§6.", state));
            return;
        }
        sender.sendMessage(String.format("§6No phantom spawns §c%s §6for §c%s§6.", state, player.getDisplayName()));
        player.sendMessage(String.format("§6No phantom spawns §c%s§6.", state));
    }

    @NotNull
    public HookManager getHookManager() {
        return hookManager;
    }

    public boolean storeInLuckyPerms() {
        String storeIn = mainConfig.getConfig().getString("store-in");

        if (storeIn == null) {
            return false;
        }

        if (storeIn.equalsIgnoreCase("luckyperms")) {
            return true;
        }

        return false;
    }

    public ConfigManager getMainConfig() {
        return mainConfig;
    }

    public ConfigManager getPhantomStatesConfig() {
        return phantomStatesConfig;
    }
}
