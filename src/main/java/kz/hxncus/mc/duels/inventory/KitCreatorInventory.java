package kz.hxncus.mc.duels.inventory;

import kz.hxncus.mc.duels.methods.ItemStacker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitCreatorInventory {
    public static Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§cСоздать кит");
        inventory.setItem(22, ItemStacker.setDisplayName(new ItemStack(Material.BEDROCK), "§eКреатив"));
        return inventory;
    }
}
