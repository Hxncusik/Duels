package kz.hxncus.mc.duels.event.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static kz.hxncus.mc.duels.EventListener.clear;

public class PlayerJoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event){
        clear(event.getPlayer());
    }
}
