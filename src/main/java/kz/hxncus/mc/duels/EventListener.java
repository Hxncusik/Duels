package kz.hxncus.mc.duels;

import kz.hxncus.mc.duels.inventory.BrowserInventory;
import kz.hxncus.mc.duels.inventory.DuelsInventory;
import kz.hxncus.mc.duels.inventory.KitCreatorInventory;
import kz.hxncus.mc.duels.methods.ItemStacker;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Iterator;
import java.util.Map;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Map<Player, PlayerChecks> playerChecks = Duels.getPlayerChecks();
        if(playerChecks.isEmpty() || playerChecks.get(player) == null){
            playerChecks.put(player, new PlayerChecks(null));
        }
        player.teleport(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
        Inventory inventory = player.getInventory();
        inventory.clear();

        inventory.setItem(0, ItemStacker.setDisplayName(new ItemStack(Material.IRON_SWORD), "§fДуэли"));

        player.updateInventory();
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(event.getCause() == EntityDamageEvent.DamageCause.VOID && player.getLocation().getY() < 0) {
                player.teleportAsync(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
            }
            if (player.getWorld() == Bukkit.getWorld("world")) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            Player victim = (Player) entity;
            if(event.getDamager() instanceof Arrow) {
                Entity damager = event.getDamager();
                ProjectileSource entityShooter = ((Arrow) damager).getShooter();
                if(entityShooter instanceof Player) {
                    Player shooter = (Player) entityShooter;
                    double victimHp = victim.getHealth() - event.getFinalDamage();
                    shooter.sendMessage("§7[§aDuel§7] §fВы попали в §e" + victim.getName() + " §fу него осталось §c" + String.format("%.2f", victimHp) + "❤");
                }
            } else if(event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                AttributeInstance attack_speed = damager.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
                if(attack_speed != null && attack_speed.getValue() >= 999) {
                    if(event.getFinalDamage() >= 0.5) {
                        victim.setVelocity(damager.getLocation().getDirection().multiply(1.05).setY(0.3));
                        victim.setMaximumNoDamageTicks(15);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world == Bukkit.getWorld("world")){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        String inventoryName = event.getView().getTitle();

        if (item == null || item.getType() == Material.AIR) {
            ItemStack cursor = event.getCursor();
            if (cursor == null) {
                return;
            }
            if (cursor.getItemMeta().getDisplayName().contains("§fДуэли")) {
                event.setCursor(null);
            }
            player.updateInventory();
            return;
        }

        World world = player.getWorld();
        if (!world.getName().equals("world")) {
            return;
        }

        Inventory inventory = player.getInventory();

        if (item.getItemMeta().getDisplayName().contains("§fАлмазник") ||
                item.getItemMeta().getDisplayName().contains("§fЖелезник") ||
                item.getItemMeta().getDisplayName().contains("§fЛучник")) {
            handleKitItem(event, player, inventory, inventoryName, item);
        } else if (item.getItemMeta().getDisplayName().contains("§fДуэли 1.8 пвп") ||
                item.getItemMeta().getDisplayName().contains("§fДуэли 1.9+ пвп")) {
            handleDuelItem(event, player, inventory, inventoryName);
        } else if (item.getItemMeta().getDisplayName().contains("§eБраузер китов")) {
            handleKitBrowserItem(event, player);
        } else if (inventoryName.contains("§cСоздать кит")) {
            handleCreateKitItem(event, player);
        }

        player.updateInventory();
    }

    private void handleKitItem(InventoryClickEvent event, Player player, Inventory inventory, String inventoryName, ItemStack item) {
        event.setCancelled(true);
        Map<String, DuelsQueue> queues = Duels.getQueues();
        DuelsQueue duelsQueue = null;

        if (inventoryName.contains("Дуэли 1.9+ пвп")) {
            if (item.getItemMeta().getDisplayName().contains("§fАлмазник")) {
                duelsQueue = getOrCreateDuelsQueue(queues, "diamond9kit", 16, 1);
            } else if (item.getItemMeta().getDisplayName().contains("§fЖелезник")) {
                duelsQueue = getOrCreateDuelsQueue(queues, "iron9kit", 16, 1);
            } else if (item.getItemMeta().getDisplayName().contains("§fЛучник")) {
                duelsQueue = getOrCreateDuelsQueue(queues, "bow9kit", 16, 1);
            }
        } else if (inventoryName.contains("Дуэли 1.8 пвп")) {
            if (item.getItemMeta().getDisplayName().contains("§fАлмазник")) {
                duelsQueue = getOrCreateDuelsQueue(queues, "diamond8kit", 16, 1);
            } else if (item.getItemMeta().getDisplayName().contains("§fЖелезник")) {
                duelsQueue = getOrCreateDuelsQueue(queues, "iron8kit", 16, 1);
            } else if (item.getItemMeta().getDisplayName().contains("§fЛучник")) {
                duelsQueue = getOrCreateDuelsQueue(queues, "bow8kit", 16, 1);
            }
        }

        if (duelsQueue != null) {
            if (!duelsQueue.isPlayerInQueue(player)) {
                inventory.remove(player.getItemInHand());
                inventory.setItem(8, new ItemStack(Material.REDSTONE));
                duelsQueue.addToQueue(player);
            }
        }

        if (inventoryName.contains("1.9+ Запрос игроку") || inventoryName.contains("1.8 Запрос игроку")) {
            Player getter = Bukkit.getPlayer(inventoryName.split(" ")[3]);
            PlayerChecks playerChecks = Duels.getPlayerChecks().get(getter);

            if (inventoryName.contains("1.9+ Запрос игроку")) {
                if (item.getItemMeta().getDisplayName().contains("§fАлмазник")) {
                    duelsQueue = getOrCreateDuelsQueue(queues, "diamond9kit", 16, 1);
                    playerChecks.addInvites(player.getName(), duelsQueue);
                } else if (item.getItemMeta().getDisplayName().contains("§fЖелезник")) {
                    duelsQueue = getOrCreateDuelsQueue(queues, "iron9kit", 16, 1);
                    playerChecks.addInvites(player.getName(), duelsQueue);
                } else if (item.getItemMeta().getDisplayName().contains("§fЛучник")) {
                    duelsQueue = getOrCreateDuelsQueue(queues, "bow9kit", 16, 1);
                    playerChecks.addInvites(player.getName(), duelsQueue);
                }
            } else if (inventoryName.contains("1.8 Запрос игроку")) {
                if (item.getItemMeta().getDisplayName().contains("§fАлмазник")) {
                    duelsQueue = getOrCreateDuelsQueue(queues, "diamond8kit", 16, 1);
                    playerChecks.addInvites(player.getName(), duelsQueue);
                } else if (item.getItemMeta().getDisplayName().contains("§fЖелезник")) {
                    duelsQueue = getOrCreateDuelsQueue(queues, "iron8kit", 16, 1);
                    playerChecks.addInvites(player.getName(), duelsQueue);
                } else if (item.getItemMeta().getDisplayName().contains("§fЛучник")) {
                    duelsQueue = getOrCreateDuelsQueue(queues, "bow8kit", 16, 1);
                    playerChecks.addInvites(player.getName(), duelsQueue);
                }
            }
        }

        player.closeInventory();
    }

    private DuelsQueue getOrCreateDuelsQueue(Map<String, DuelsQueue> queues, String queueName, int maxSize, int minPlayers) {
        DuelsQueue duelsQueue = queues.get(queueName);
        if (duelsQueue == null) {
            duelsQueue = new DuelsQueue(queueName, maxSize, minPlayers);
            queues.put(queueName, duelsQueue);
        }
        return duelsQueue;
    }

    private void handleDuelItem(InventoryClickEvent event, Player player, Inventory inventory, String inventoryName) {
        event.setCancelled(true);
        if (inventoryName.contains("Дуэли 1.8 пвп")) {
            if (inventoryName.contains("1.9+")) {
                Inventory duelInventory = DuelsInventory.getInventory(inventoryName.replace("1.9+", "1.8"));
                player.openInventory(duelInventory);
            }
        } else if (inventoryName.contains("Дуэли 1.9+ пвп")) {
            if (inventoryName.contains("1.8")) {
                Inventory duelInventory = DuelsInventory.getInventory(inventoryName.replace("1.8", "1.9+"));
                player.openInventory(duelInventory);
            }
        }
    }

    private void handleKitBrowserItem(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        if (event.getCurrentItem().getType() == Material.IRON_PICKAXE) {
            player.openInventory(KitCreatorInventory.getInventory());
        }
    }

    private void handleCreateKitItem(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        if (item.getType() == Material.BEDROCK) {
            player.getInventory().clear();
            player.teleport(new Location(Bukkit.getWorld("world"), 1396.5, 156, 1707.5));
            player.setGameMode(GameMode.CREATIVE);
        } else if (item.getType() == Material.NAME_TAG) {
            // Handle NAME_TAG item
        }
    }

    private boolean isNullOrEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    private boolean isWorldNameEquals(Player player, String worldName) {
        World world = player.getWorld();
        return world.getName().equals(worldName);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        ItemStack item = event.getItem();
        if (item != null) {
            if(player.getWorld().getName().equals("world")) {
                if(openInventory.getType().equals(InventoryType.CRAFTING)) {
                    if (item.getType().equals(Material.IRON_SWORD)) {
                        Inventory duelInv = DuelsInventory.getInventory("Дуэли 1.9+ пвп");
                        player.openInventory(duelInv);
                    } else if (item.getType().equals(Material.REDSTONE)) {
                        PlayerChecks playerChecks = Duels.getPlayerChecks().get(player);
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
                    }
                }
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        event.setCancelled(true);
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        event.setCancelled(true);
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        PlayerChecks checks = Duels.getPlayerChecks().get(player);
        DuelsQueue duelsQueue = checks.getQueue();
        if(duelsQueue.isPlayerInGame(player)){
            event.setRespawnLocation(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
            duelsQueue.removeFromGame(player);
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        PlayerChecks checks = Duels.getPlayerChecks().get(player);
        DuelsQueue duelsQueue = checks.getQueue();
        if(duelsQueue.isPlayerInGame(player)){
            Map<Player, DuelsArenas> game = duelsQueue.getGame();
            Iterator<Player> iterator = game.keySet().iterator();
            while(iterator.hasNext()) {
                Player key = iterator.next();
                if(key != player && game.get(key) == game.get(player)){
                    iterator.remove();
                    key.sendMessage("§7[§aDuel§7] §e" + key.getName() + " §fубил игрока §e" + player.getName() + " §fу него осталось §c" + String.format("%.2f", key.getHealth()) + "❤");
                    player.sendMessage("§7[§aDuel§7] §e" + key.getName() + " §fубил игрока §e" + player.getName() + " §fу него осталось §c" + String.format("%.2f", key.getHealth()) + "❤");
                    duelsQueue.removeFromGame(key);
                    game.get(player).setAvailable(true);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PlayerChecks playerChecks = Duels.getPlayerChecks().get(player);
        DuelsQueue duelsQueue = playerChecks.getQueue();
        if(duelsQueue != null) {
            if (duelsQueue.isPlayerInQueue(player)) {
                duelsQueue.removeFromQueue(player);
            } else if (duelsQueue.isPlayerInGame(player)) {
                Map<Player, DuelsArenas> game = duelsQueue.getGame();
                Iterator<Player> iterator = game.keySet().iterator();
                duelsQueue.removeFromGame(player);
                while (iterator.hasNext()) {
                    Player key = iterator.next();
                    if (key != player && game.get(key) == game.get(player)) {
                        iterator.remove();
                        duelsQueue.removeFromGame(key);
                        game.get(player).setAvailable(true);
                    }
                }
            }
        }
    }
}
