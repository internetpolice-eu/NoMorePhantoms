package com.cdejong.nomorephantoms.events;

import com.cdejong.nomorephantoms.NoMorePhantoms;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Collection;

public class PhantomSpawnEvents implements Listener {

    private NoMorePhantoms plugin;
    private static final int DISTANCE_HORIZONTAL = 15;
    private static final int DISTANCE_VERTICAL = 200;

    public PhantomSpawnEvents(NoMorePhantoms plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPhantomSpawnEvent(CreatureSpawnEvent spawnEvent) {

        if (spawnEvent.getEntityType() != EntityType.PHANTOM
                || spawnEvent.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL ) {
            return;
        }

        if (plugin.getMainConfig().getConfig().getBoolean("stop-all-spawns")) {
            spawnEvent.setCancelled(true);
            return;
        }

        World eventWorld = spawnEvent.getLocation().getWorld();
        if (eventWorld == null) {
            return;
        }

        if (plugin.getMainConfig().getConfig().getStringList("stop-world-spawns").contains(eventWorld.getName())) {
            spawnEvent.setCancelled(true);
            return;
        }

        Collection<Entity> nearbyEntities = eventWorld.getNearbyEntities(spawnEvent.getLocation(), DISTANCE_HORIZONTAL, DISTANCE_VERTICAL, DISTANCE_HORIZONTAL, (e) -> e.getType() == EntityType.PLAYER);

        nearbyEntities.stream()
                .filter(entity -> plugin.isNoPhantomSpawnsEnabled((Player) entity))
                .findFirst()
                .ifPresent(entity -> {
                    spawnEvent.setCancelled(true);
                });
    }
}
