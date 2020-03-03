package com.cdejong.nomorephantoms.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Collection;

public class PhantomSpawnEvents implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPhantomSpawnEvent(EntitySpawnEvent event) {
        if (event.getEntityType() != EntityType.PHANTOM ||
                event.getEntity().getEntitySpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        Collection<Player> nearbyPlayers = event.getLocation().getNearbyPlayers(200.0);
        for (Player player : nearbyPlayers) {
            if (player.hasPermission("nomorephantoms.nospawns")) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
