package kz.hxncus.mc.duels.event.player;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDropItemEvent implements Listener {
    @EventHandler
    public void onPlayerDropItem(org.bukkit.event.player.PlayerDropItemEvent event){
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world == Bukkit.getWorld("world")){
            event.setCancelled(true);
        }
    }
}
