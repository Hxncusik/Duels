package kz.hxncus.mc.duels.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BowKit {
    public void getKit(Player player){
        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(40, new ItemStack(Material.GOLDEN_APPLE, 4));
        inventory.setItem(39, new ItemStack(Material.IRON_HELMET));
        inventory.setItem(38, new ItemStack(Material.IRON_CHESTPLATE));
        inventory.setItem(37, new ItemStack(Material.IRON_LEGGINGS));
        inventory.setItem(36, new ItemStack(Material.IRON_BOOTS));
        inventory.setItem(9, new ItemStack(Material.ARROW, 1));
        inventory.setItem(2, new ItemStack(Material.COOKED_BEEF, 8));
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        inventory.setItem(0, bow);
        player.updateInventory();
    }
}
