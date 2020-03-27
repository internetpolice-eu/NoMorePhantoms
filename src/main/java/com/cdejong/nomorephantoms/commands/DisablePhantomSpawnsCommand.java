package com.cdejong.nomorephantoms.commands;

import com.cdejong.nomorephantoms.NoMorePhantoms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;



public class DisablePhantomSpawnsCommand implements CommandExecutor {
    private NoMorePhantoms plugin;

    public DisablePhantomSpawnsCommand(NoMorePhantoms plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            if (!sender.hasPermission("nomorephantoms.toggle")) {
                sender.sendMessage("§4You do not have access to that command.");
                plugin.getLogger().info(String.format("§c%s §4was denied access to command.", sender.getName()));
                return true;
            }

            // /phantom <enable|disable|<username>>
            if (args.length == 1) {

                if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("enabled")) {
                    plugin.setPhantomSpawnState((Player) sender, sender, true);
                    return true;
                }

                if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("disabled")) {
                    plugin.setPhantomSpawnState((Player) sender, sender,false);
                    return true;
                }

                if (sender.hasPermission("nomorephantoms.toggle.others")) {
                    Player player = Bukkit.getPlayerExact(args[0]);
                    if(player == null) {
                        sender.sendMessage("§cError: §4Player not found.");
                        return true;
                    }

                    plugin.togglePhantomSpawnState(player, sender);
                    return true;
                }
            }

            // /phantom <username> <enable|disable>
            if (args.length >= 2 && sender.hasPermission("nomorephantoms.toggle.others")) {


                Player player = Bukkit.getPlayerExact(args[0]);
                if(player == null) {
                    sender.sendMessage("§cError: §4Player not found.");
                    return true;
                }

                if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("enabled")) {
                    plugin.setPhantomSpawnState(player, sender, true);
                    return true;
                }

                if (args[1].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("disabled")) {
                    plugin.setPhantomSpawnState(player, sender,false);
                    return true;
                }

                sender.sendMessage("§cError: §4null");
                return true;
            }

            plugin.togglePhantomSpawnState((Player) sender, sender);
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {

            sender.sendMessage(String.format("CONSOLE issued server command: %s", label));

            if (args.length < 1) {
                sender.sendMessage("§cError: §4Player not found.");
                return true;
            }

            Player player = Bukkit.getPlayerExact(args[0]);
            if(player == null) {
                sender.sendMessage("§cError: §4Player not found.");
                return true;
            }

            if (args.length >= 2) {

                if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("enabled")) {
                    plugin.setPhantomSpawnState(player, sender, true);
                    return true;
                }

                if (args[1].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("disabled")) {
                    plugin.setPhantomSpawnState(player, sender,false);
                    return true;
                }

                sender.sendMessage("§cError: §4null");
                return true;
            }

            plugin.togglePhantomSpawnState(player, sender);
            return true;

        }
        sender.sendMessage("§cError: §4null");
        return true;
    }
}
