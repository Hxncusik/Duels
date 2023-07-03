package kz.hxncus.mc.duels;

import kz.hxncus.mc.duels.kits.BowKit;
import kz.hxncus.mc.duels.kits.DiamondKit;
import kz.hxncus.mc.duels.kits.IronKit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DuelsQueue {
    private final Queue<Player> queue;
    private final Map<Player, DuelsArenas> game;
    private final String queueName;
    private final int teamSize;

    public DuelsQueue(String queueName, int teamSize) {
        this.queue = new LinkedList<>();
        this.game = new HashMap<>();
        this.queueName = queueName;
        this.teamSize = teamSize;
    }
    public void addToQueue(Player player) {
        PlayerChecks checks = Duels.getPlayerChecks().get(player);
        checks.setQueue(this);
        queue.add(player);
        player.sendMessage("You have been added to the queue.");
        checkQueue();
    }
    public boolean isPlayerInQueue(Player player) {
        return queue.contains(player);
    }
    public boolean isPlayerInGame(Player player) {
        return game.containsKey(player);
    }
    public Map<Player, DuelsArenas> getGame(){
        return game;
    }
    public void removeFromQueue(Player player) {
        queue.remove(player);
        PlayerChecks checks = Duels.getPlayerChecks().get(player);
        checks.setQueue(null);
        player.sendMessage("You have been removed from the queue.");
        Inventory inventory = player.getInventory();
        inventory.clear();

        ItemStack slot0 = new ItemStack(Material.IRON_SWORD);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName("Дуэли");
        slot0.setItemMeta(slot0meta);
        inventory.setItem(0, slot0);

        player.updateInventory();
    }
    public void removeFromGame(Player player) {
        game.remove(player);

        player.teleport(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
        player.setHealth(20);
        player.setFoodLevel(20);

        Inventory inventory = player.getInventory();
        inventory.clear();
        ItemStack slot0 = new ItemStack(Material.IRON_SWORD);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName("Дуэли");
        slot0.setItemMeta(slot0meta);
        inventory.setItem(0, slot0);
        player.updateInventory();
        player.setMaximumNoDamageTicks(20);
        AttributeInstance attack_speed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if(attack_speed != null) {
            attack_speed.setBaseValue(attack_speed.getDefaultValue());
        }

        player.sendMessage("You have been leaved from the game.");
    }
    private void checkQueue() {
        if(queue.size() >= (teamSize * 2)) {
            Player player1 = queue.poll();
            Player player2 = queue.poll();
            // start the duel between player1 and player2
            startGame(player1, player2);
        }
    }
    public void startGame(Player player1, Player player2){
        DuelsArenas arena = Duels.getInstance().getFreeArena();
        if (arena != null) {
            arena.setAvailable(false);
            doKit(arena, player1);
            player1.teleport(arena.getSpawnPoint1());
            doKit(arena, player2);
            player2.teleport(arena.getSpawnPoint2());
        } else {
            removeFromQueue(player1);
            removeFromQueue(player2);
            player1.sendMessage("Все арены забиты игроками, подождите несколько минут");
            player2.sendMessage("Все арены забиты игроками, подождите несколько минут");
        }
            Bukkit.getScheduler().runTaskLater(
                    Duels.getInstance(),
                    () -> {
                        removeFromGame(player1);
                        removeFromGame(player2);
                    },
                    18000L
            );
    }
    private void doKit(DuelsArenas arena, Player player){
        if (player != null) {
            player.updateInventory();
            game.put(player, arena);
            PlayerChecks playerChecks = Duels.getPlayerChecks().get(player);
            playerChecks.setQueue(this);
            if(queueName.contains("8")){
                AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
                if(attribute != null){
                    attribute.setBaseValue(1000);
                }
            }
            if (queueName.contains("iron")) {
                new IronKit().getKit(player);
            } else if (queueName.contains("diamond")) {
                new DiamondKit().getKit(player);
            } else if (queueName.contains("bow")){
                new BowKit().getKit(player);
            }
        }
    }
}
