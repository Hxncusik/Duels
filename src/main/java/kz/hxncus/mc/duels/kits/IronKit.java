package kz.hxncus.mc.duels.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class IronKit {
    public void getKit(Player player){
        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(40, new ItemStack(Material.GOLDEN_APPLE, 8));
        inventory.setItem(39, new ItemStack(Material.IRON_HELMET));
        inventory.setItem(38, new ItemStack(Material.IRON_CHESTPLATE));
        inventory.setItem(37, new ItemStack(Material.IRON_LEGGINGS));
        inventory.setItem(36, new ItemStack(Material.IRON_BOOTS));
        inventory.setItem(9, new ItemStack(Material.ARROW, 8));
        inventory.setItem(2, new ItemStack(Material.COOKED_BEEF, 16));
        inventory.setItem(1, new ItemStack(Material.BOW));
        inventory.setItem(0, new ItemStack(Material.IRON_SWORD));
        player.updateInventory();
    }
}
