package kz.hxncus.mc.duels.event.player;

import kz.hxncus.mc.duels.Duels;
import kz.hxncus.mc.duels.DuelsQueue;
import kz.hxncus.mc.duels.PlayerChecks;
import kz.hxncus.mc.duels.inventory.DuelsInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteractEvent implements Listener {
    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event){
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        if (!player.getWorld().getName().equals("world")) {
            return;
        }
        if(!openInventory.getType().equals(InventoryType.CRAFTING)) {
            return;
        }
        if (item.getType().equals(Material.IRON_SWORD)) {
            Inventory duelInv = DuelsInventory.getInventory("Дуэли 1.9+ пвп");
            player.openInventory(duelInv);
        } else if (item.getType().equals(Material.REDSTONE)) {
            PlayerChecks playerChecks = PlayerChecks.getPlayerChecks(player.getName());
            DuelsQueue duelsQueue = playerChecks.getQueue();
            if (duelsQueue.isPlayerInQueue(player)) {
                duelsQueue.removeFromQueue(player);
                inventory.remove(item);
                ItemStack slot0 = new ItemStack(Material.IRON_SWORD);
                ItemMeta slot0meta = slot0.getItemMeta();
                slot0meta.setDisplayName("Дуэли");
                slot0.setItemMeta(slot0meta);
                inventory.setItem(0, slot0);
            }
        } else if (item.getType().toString().contains("POTION")) {
            event.setCancelled(true);
        } else if (item.getType().toString().contains("BUCKET")) {
            event.setCancelled(true);
        } else if (item.getType().toString().contains("SPAWN_EGG")) {
            event.setCancelled(true);
        }
    }
}
