package com.cdejong.nomorephantoms.hooks;

import com.cdejong.nomorephantoms.NoMorePhantoms;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class LuckPermsHook extends PluginHook {
    private LuckPerms api;

    public LuckPermsHook(@NotNull NoMorePhantoms plugin) {
        super("LuckPerms", plugin);
    }

    @Override
    public boolean onHook() {
        RegisteredServiceProvider<LuckPerms> rsp = plugin.getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (rsp == null) {
            plugin.getLogger().warning("Failed to get LuckPermsApi rsp.");
            return false;
        }

        api = rsp.getProvider();
        return true;
    }

    public boolean isNoPhantomSpawnsEnabled(Player player) {
        boolean result = false;

        QueryOptions options = api.getContextManager().getQueryOptions(player);
        User user = api.getUserManager().getUser(player.getUniqueId());

        if (user != null) {
            String metaValue = user.getCachedData().getMetaData(options).getMetaValue("no-phantom-spawns");
            if (metaValue != null) {
                result = Boolean.parseBoolean(metaValue);
            }
        }

        return result;
    }

    public void setUserPhantomState(Player player, boolean state) {
        User user = api.getUserManager().getUser(player.getUniqueId());

        if (user != null) {
            MetaNode oldNode = MetaNode.builder("no-phantom-spawns", Boolean.toString(isNoPhantomSpawnsEnabled(player))).build();
            MetaNode newNode = MetaNode.builder("no-phantom-spawns", Boolean.toString(state)).build();

            DataMutateResult oldResult = user.data().remove(oldNode);
            DataMutateResult newResult = user.data().add(newNode);
            api.getUserManager().saveUser(user);
        }
    }

}
