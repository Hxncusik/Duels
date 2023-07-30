package kz.hxncus.mc.duels.event.player;

import kz.hxncus.mc.duels.Duels;
import kz.hxncus.mc.duels.DuelsQueue;
import kz.hxncus.mc.duels.PlayerChecks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerRespawnEvent implements Listener {
    @EventHandler
    public void onPlayerRespawn(org.bukkit.event.player.PlayerRespawnEvent event){
        Player player = event.getPlayer();
        PlayerChecks checks = PlayerChecks.getPlayerChecks(player.getName());
        DuelsQueue duelsQueue = checks.getQueue();
        if(duelsQueue.isPlayerInGame(player)){
            event.setRespawnLocation(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
            duelsQueue.removeFromGame(player);
        }
    }
}
