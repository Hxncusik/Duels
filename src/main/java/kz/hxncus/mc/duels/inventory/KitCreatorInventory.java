package kz.hxncus.mc.duels.inventory;

import kz.hxncus.mc.duels.methods.ItemStacker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitCreatorInventory {
    public static Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§cСоздать кит");
        inventory.setItem(22, createBedrockItem());
        inventory.setItem(20, createNameTagItem());
        inventory.setItem(24, createSaveKitItem());
        return inventory;
    }

    private static ItemStack createBedrockItem() {
        return ItemStacker.setDisplayName(new ItemStack(Material.BEDROCK), "§eКреатив");
    }

    private static ItemStack createNameTagItem() {
        return ItemStacker.setDisplayName(new ItemStack(Material.NAME_TAG), "§eНазвание кита");
    }

    private static ItemStack createSaveKitItem() {
        return ItemStacker.setDisplayName(new ItemStack(Material.LIME_WOOL), "§aСохранить кит");
    }
}
