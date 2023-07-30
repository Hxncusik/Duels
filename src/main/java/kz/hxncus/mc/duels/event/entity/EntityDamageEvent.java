package kz.hxncus.mc.duels.event.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityDamageEvent implements Listener {
    @EventHandler
    public void onEntityDamage(org.bukkit.event.entity.EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.VOID && player.getLocation().getY() < 0) {
                player.teleportAsync(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
            }
            if (player.getWorld() == Bukkit.getWorld("world")) {
                event.setCancelled(true);
            }
        }
    }
}
