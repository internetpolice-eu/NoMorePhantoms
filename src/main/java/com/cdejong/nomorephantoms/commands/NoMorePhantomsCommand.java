package com.cdejong.nomorephantoms.commands;

import com.cdejong.nomorephantoms.NoMorePhantoms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class NoMorePhantomsCommand implements CommandExecutor {
    private NoMorePhantoms plugin;

    public NoMorePhantomsCommand(NoMorePhantoms plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("nomorephantoms.reload")) {
                plugin.getPhantomStatesConfig().reloadConfig();
                plugin.getMainConfig().reloadConfig();
                sender.sendMessage("§6NoMorePhantoms reloaded §c" + plugin.getDescription().getVersion() + "§6.");
                return true;
            }
        }

        sender.sendMessage("§6NoMorePhantoms §c" + plugin.getDescription().getVersion() + "§6 by §cSheepert_§6.");
        return true;
    }
}
