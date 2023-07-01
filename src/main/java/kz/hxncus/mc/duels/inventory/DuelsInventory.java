package kz.hxncus.mc.duels.inventory;

import javafx.beans.property.Property;
import kz.hxncus.mc.duels.methods.ItemStacker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class DuelsInventory {
    public static Inventory getInventory(String name){
        Inventory inventory = Bukkit.createInventory(null, 54, name);

        ItemStack duel = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta duelMeta = duel.getItemMeta();
        duelMeta.setDisplayName("§fДуэли 1.9+ пвп");
        if (name.contains("1.9")) {
            duelMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
            duelMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        duel.setItemMeta(duelMeta);
        inventory.setItem(4, duel);

        ItemStack duel8 = new ItemStack(Material.IRON_SWORD);
        ItemMeta duel8Meta = duel8.getItemMeta();
        duel8Meta.setDisplayName("§fДуэли 1.8 пвп");
        if (name.contains("1.8")) {
            duel8Meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
            duel8Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        duel8.setItemMeta(duel8Meta);
        inventory.setItem(5, duel8);
        inventory.setItem(19, ItemStacker.setDisplayName(new ItemStack(Material.DIAMOND_CHESTPLATE), "§fАлмазник"));
        inventory.setItem(28, ItemStacker.setDisplayName(new ItemStack(Material.IRON_CHESTPLATE), "§fЖелезник"));
        inventory.setItem(20, ItemStacker.setDisplayName(new ItemStack(Material.BOW), "§fЛучник"));

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwner("cake");
        headMeta.setDisplayName("§eБраузер китов");
        head.setItemMeta(headMeta);
        inventory.setItem(53, head);

        return inventory;
    }
}
