package kz.hxncus.mc.duels.event.inventory;

import kz.hxncus.mc.duels.EventListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEvent implements Listener {
    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (!player.getWorld().getName().equals("world")) {
            return;
        }
        event.setCancelled(true);
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        String inventoryName = event.getView().getTitle();
        String itemName = item.getItemMeta().getDisplayName();
        int slot = event.getSlot();
        Material material = item.getType();
        if ((slot == 19 && material == Material.DIAMOND_CHESTPLATE) || (slot == 28 && material == Material.IRON_CHESTPLATE) || (slot == 20 && material == Material.BOW)) {
            EventListener.handleKitItem(player, player.getInventory(), inventoryName, item);
        } else if ((slot == 4 && material == Material.DIAMOND_SWORD) || (slot == 5 && material == Material.IRON_CHESTPLATE)) {
            EventListener.handleDuelItem(player, inventoryName, item.getItemMeta().getDisplayName());
        } else if (itemName.contains("§eБраузер китов")) {
            EventListener.handleKitBrowserItem(player);
        } else if (inventoryName.contains("§cСоздать кит")) {
            EventListener.handleCreateKitItem(player, event.getCurrentItem());
        } else if (inventoryName.contains("§eБраузер китов")) {
            if (itemName.contains("§cСоздать кит")) {
                EventListener.handleKitCreatorInventory(player);
            } else {
                EventListener.handleBrowserKit(player, item.getItemMeta().getDisplayName().substring(2));
            }
        }
    }
}
