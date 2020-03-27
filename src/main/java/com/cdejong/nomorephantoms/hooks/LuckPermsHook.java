package com.cdejong.nomorephantoms.hooks;

import com.cdejong.nomorephantoms.NoMorePhantoms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class LuckPermsHook extends PluginHook {
    private LuckPermsApi api;

    public LuckPermsHook(@NotNull NoMorePhantoms plugin) {
        super("LuckPerms", plugin);
    }

    @Override
    public boolean onHook() {
        RegisteredServiceProvider<LuckPermsApi> rsp = plugin.getServer().getServicesManager().getRegistration(LuckPermsApi.class);
        if (rsp == null) {
            plugin.getLogger().warning("Failed to get LuckPermsApi rsp.");
            return false;
        }

        api = rsp.getProvider();
        return true;
    }

    public boolean getUserPhantomState(Player player) {
        boolean result = false;
        User user = api.getUser(player.getUniqueId());

        if (user != null) {
            Contexts ctx = api.getContextsForPlayer(player);
            String metaValue = user.getCachedData().getMetaData(ctx).getMeta().getOrDefault("phantom-spawns", "false");

            try {
                result = Boolean.valueOf(metaValue);
            } catch (Exception ignored) {}
        }

        return result;
    }

    public void setUserPhantomState(Player player, boolean state) {
        User user = api.getUser(player.getUniqueId());
        if (user != null) {
            Node oldNode = api.getNodeFactory().makeMetaNode("phantom-spawns",
                    String.valueOf(getUserPhantomState(player))).build();
            Node newNode = api.getNodeFactory().makeMetaNode("phantom-spawns", Boolean.toString(state)).build();

            user.unsetPermission(oldNode);
            user.setPermission(newNode);
            api.getUserManager().saveUser(user);
        }
    }

}
