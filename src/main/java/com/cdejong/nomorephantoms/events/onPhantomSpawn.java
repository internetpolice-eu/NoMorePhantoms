package com.cdejong.nomorephantoms.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class onPhantomSpawn implements Listener {

    private Plugin plugin;

    public onPhantomSpawn(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPhantomSpawn(EntitySpawnEvent event) {
        if ( event.getEntityType() == EntityType.PHANTOM && event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            Collection<Player> nearbyPlayers = event.getLocation().getNearbyPlayers(200.0);
            for (Player player : nearbyPlayers) {
                if (player.hasPermission("nomorephantoms.nospawns")) {
                    plugin.getLogger().info("CANCELLED EVENT: EntitySpawnEvent nearby: " + player.getName());
                    event.setCancelled(true);
                }
            }
        }
    }
}
