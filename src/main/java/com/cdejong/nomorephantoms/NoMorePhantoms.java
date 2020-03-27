package com.cdejong.nomorephantoms;

import com.cdejong.nomorephantoms.commands.DisablePhantomSpawnsCommand;
import com.cdejong.nomorephantoms.events.PhantomSpawnEvents;
import com.cdejong.nomorephantoms.hooks.HookManager;
import com.cdejong.nomorephantoms.hooks.LuckPermsHook;
import com.cdejong.nomorephantoms.hooks.PluginHook;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NoMorePhantoms extends JavaPlugin {
    private HookManager hookManager;

    @Override
    public void onEnable() {
        hookManager = new HookManager(this);
        hookManager.loadPluginHook(new LuckPermsHook(this));
        getServer().getPluginManager().registerEvents(new PhantomSpawnEvents(this), this);

        getCommand("phantom").setExecutor(new DisablePhantomSpawnsCommand(this));
    }

    public void togglePhantomSpawnState(Player player, CommandSender sender) {
        Optional<PluginHook> hook = getHookManager().getPluginHook("LuckPerms");
        if (hook.isPresent()) {

            LuckPermsHook lpHook = (LuckPermsHook) hook.get();

            if (lpHook.getUserPhantomState(player)) { //state: true
                lpHook.setUserPhantomState(player, false);
                sendState(player, sender);
                return;
            }
            //state: false
            lpHook.setUserPhantomState(player, true);
            sendState(player, sender);
        }
    }

    public void setPhantomSpawnState(Player player, CommandSender sender, boolean state) {
        Optional<PluginHook> hook = getHookManager().getPluginHook("LuckPerms");
        if (hook.isPresent()) {

            LuckPermsHook lpHook = (LuckPermsHook) hook.get();

            lpHook.setUserPhantomState(player, state);
            sendState(player, sender);
        }
    }

    public boolean getPhantomSpawnState(Player player) {
        boolean state = false;
        Optional<PluginHook> hook = getHookManager().getPluginHook("LuckPerms");
        if (hook.isPresent()) {

            LuckPermsHook lpHook = (LuckPermsHook) hook.get();

            state = lpHook.getUserPhantomState(player);

        }
        return state;
    }

    private void sendState(Player player, CommandSender sender) {
        String state = getPhantomSpawnState(player) ? "enabled" : "disabled";

        if (player == sender) {
            sender.sendMessage(String.format("§6Phantom spawns §c%s§6.", state));
            return;
        }
        sender.sendMessage(String.format("§6Phantom spawns §c%s §6for §c%s§6.", state, player.getDisplayName()));
        player.sendMessage(String.format("§6Phantom spawns §c%s§6.", state));
    }

    @NotNull
    public HookManager getHookManager() {
        return hookManager;
    }
}
