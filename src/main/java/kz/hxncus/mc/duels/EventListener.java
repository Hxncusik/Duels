package kz.hxncus.mc.duels;

import kz.hxncus.mc.duels.inventory.BrowserInventory;
import kz.hxncus.mc.duels.inventory.DuelsInventory;
import kz.hxncus.mc.duels.inventory.KitCreatorInventory;
import kz.hxncus.mc.duels.kit.KitManager;
import kz.hxncus.mc.duels.kit.PlayerManager;
import kz.hxncus.mc.duels.methods.ItemStacker;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EventListener implements Listener {
    public static PlayerManager setupPlayerManager(String playerName) {
        Map<String, PlayerManager> playerManagerMap = Duels.getPlayerManagerMap();
        PlayerManager playerManager = playerManagerMap.get(playerName);
        if (playerManager == null) {
            playerManager = new PlayerManager(playerName, "null", false);
            playerManagerMap.put(playerName, playerManager);
        }
        return playerManager;
    }
    public static void handleBrowserKit(Player player, String kitName) {
        player.closeInventory();
        Inventory inventory = player.getInventory();
        DuelsQueue duelsQueue = getOrCreateDuelsQueue(Duels.getQueues(), kitName, 1);
        if (!duelsQueue.isPlayerInQueue(player)) {
            inventory.remove(player.getItemInHand());
            inventory.setItem(8, new ItemStack(Material.REDSTONE));
            duelsQueue.addToQueue(player);
        }
    }
    public static void handleKitItem(Player player, Inventory inventory, String inventoryName, ItemStack item) {
        Map<String, DuelsQueue> queues = Duels.getQueues();
        DuelsQueue duelsQueue;
        String queueName = "null";
        if (item.getItemMeta().getDisplayName().contains("§fАлмазник")) {
            queueName = "diamond9kit";
            if (inventoryName.contains("Дуэли 1.8 пвп") || inventoryName.contains("1.8 Запрос игроку")) {
                queueName = "diamond8kit";
            }
        } else if (item.getItemMeta().getDisplayName().contains("§fЖелезник")) {
            queueName = "iron9kit";
            if (inventoryName.contains("Дуэли 1.8 пвп") || inventoryName.contains("1.8 Запрос игроку")) {
                queueName = "iron8kit";
            }
        } else if (item.getItemMeta().getDisplayName().contains("§fЛучник")) {
            queueName = "bow9kit";
            if (inventoryName.contains("Дуэли 1.8 пвп") || inventoryName.contains("1.8 Запрос игроку")) {
                queueName = "bow8kit";
            }
        }
        if (queueName.equals("null")) {
            player.closeInventory();
            return;
        }
        duelsQueue = getOrCreateDuelsQueue(queues, queueName, 1);
        if (!duelsQueue.isPlayerInQueue(player)) {
            inventory.remove(player.getItemInHand());
            inventory.setItem(8, new ItemStack(Material.REDSTONE));
            duelsQueue.addToQueue(player);
        }
        String[] splittedName = inventoryName.split(" ");
        if (splittedName.length > 3) {
            PlayerChecks playerChecks = PlayerChecks.getPlayerChecks(splittedName[3]);
            playerChecks.addInvites(player.getName(), duelsQueue);
        }
        player.closeInventory();
    }
    public static DuelsQueue getOrCreateDuelsQueue(Map<String, DuelsQueue> queues, String queueName, int minPlayers) {
        DuelsQueue duelsQueue = queues.get(queueName);
        if (duelsQueue == null) {
            duelsQueue = new DuelsQueue(queueName, minPlayers);
            queues.put(queueName, duelsQueue);
        }
        return duelsQueue;
    }
    public static void handleDuelItem(Player player, String inventoryName, String itemName) {
        if (itemName.contains("Дуэли 1.8 пвп")) {
            if (inventoryName.contains("1.9+")) {
                Inventory duelInventory = DuelsInventory.getInventory(inventoryName.replace("1.9+", "1.8"));
                player.openInventory(duelInventory);
            }
        } else if (itemName.contains("Дуэли 1.9+ пвп")) {
            if (inventoryName.contains("1.8")) {
                Inventory duelInventory = DuelsInventory.getInventory(inventoryName.replace("1.8", "1.9+"));
                player.openInventory(duelInventory);
            }
        }
    }
    public static void handleKitBrowserItem(Player player) {
        player.openInventory(BrowserInventory.getInventory());
    }
    public static void handleKitCreatorInventory(Player player) {
        player.openInventory(KitCreatorInventory.getInventory(player));
    }
    public static void handleCreateKitItem(Player player, ItemStack item) {
        if (item == null) {
            return;
        }
        Inventory inventory = player.getInventory();
        String playerName = player.getName();
        if (item.getType() == Material.BEDROCK) {
            inventory.clear();
            player.teleport(new Location(Bukkit.getWorld("world"), 1396.5, 156, 1707.5));
            player.setGameMode(GameMode.CREATIVE);
        } else if (item.getType() == Material.NAME_TAG) {
            player.closeInventory();
            PlayerManager playerManager = setupPlayerManager(player.getName());
            playerManager.setNaming(true);
        } else if (item.getType() == Material.LIME_WOOL) {
            KitManager.saveKit(player, setupPlayerManager(playerName).getKitName(), inventory);
        }
    }
    public static void clear(Player player) {
        player.teleport(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(0, ItemStacker.setDisplayName(new ItemStack(Material.IRON_SWORD), "§fДуэли"));
        player.updateInventory();
    }
}
