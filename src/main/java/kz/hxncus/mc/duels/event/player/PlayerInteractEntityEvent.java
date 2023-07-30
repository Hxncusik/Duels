package kz.hxncus.mc.duels.event.player;

import kz.hxncus.mc.duels.inventory.DuelsInventory;
import kz.hxncus.mc.duels.inventory.KitCreatorInventory;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerInteractEntityEvent implements Listener {
    @EventHandler
    public void onPlayerInteractAtEntity(org.bukkit.event.player.PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        String name = entity.getCustomName();
        if (name == null) {
            return;
        }
        if (name.equals("§cСоздать кит")) {
            KitCreatorInventory.getInventory(event.getPlayer());
        } else if (name.contains("Дуэли")) {
            DuelsInventory.getInventory("Дуэли 1.9+ пвп");
        }
    }
}
