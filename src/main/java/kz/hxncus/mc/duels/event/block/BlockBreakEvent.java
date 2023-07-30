package kz.hxncus.mc.duels.event.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockBreakEvent implements Listener {
    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event){
        event.setCancelled(true);
    }
}
