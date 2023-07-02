package kz.hxncus.mc.duels.methods;

import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class ItemStacker {
    public ItemStacker() {}
    public static ItemStack setDisplayName(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack clearAttributes(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Multimap<Attribute, AttributeModifier> attribute = itemMeta.getAttributeModifiers();
        if (attribute != null && !attribute.isEmpty()) {
            attribute.forEach(itemMeta::removeAttributeModifier);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack clearItemFlags(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getItemFlags().forEach(itemMeta::removeItemFlags);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack maxEnchantments(ItemStack itemStack) {
        Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
        enchantments.keySet().stream().filter(enchantment -> enchantment.getMaxLevel() + 2 < enchantments.get(enchantment)).forEach(enchantment -> enchantments.replace(enchantment, enchantment.getMaxLevel() + 2));
        itemStack.addEnchantments(enchantments);
        return itemStack;
    }
}
