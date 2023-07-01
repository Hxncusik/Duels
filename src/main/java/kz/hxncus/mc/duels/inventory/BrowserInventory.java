package kz.hxncus.mc.duels.inventory;

import kz.hxncus.mc.duels.Duels;
import kz.hxncus.mc.duels.methods.ItemStacker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BrowserInventory {
    public static Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "§eБраузер китов");
        for (String kit : Duels.getKitsKeys(false)) {
            System.out.println(kit);
        }
        inventory.setItem(45, ItemStacker.setDisplayName(new ItemStack(Material.IRON_PICKAXE), "§cСоздать кит"));

        return inventory;
    }
}
