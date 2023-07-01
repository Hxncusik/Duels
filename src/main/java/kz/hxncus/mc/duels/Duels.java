package kz.hxncus.mc.duels;

import kz.hxncus.mc.duels.commands.DuelCommand;
import kz.hxncus.mc.duels.commands.SpawnCommand;
import kz.hxncus.mc.duels.methods.ItemStacker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class Duels extends JavaPlugin {
    private static Duels instance;
    private static final Map<String, DuelsQueue> queues = new HashMap<>();
    private final List<DuelsArenas> arenas = new ArrayList<>();
    private static final Map<Player, PlayerChecks> playerChecks = new HashMap<>();
    private static File kitsFile;
    private static YamlConfiguration kits;
    @Override
    public void onEnable() {
        instance = this;
        new SpawnCommand();
        new DuelCommand();
        arenas.add(new DuelsArenas("cowboy", new Location(Bukkit.getWorld("arenas"), 132.5, 28, -85.5), new Location(Bukkit.getWorld("arenas"), 134.5, 28, -38.5), true));
        arenas.add(new DuelsArenas("dragon", new Location(Bukkit.getWorld("arenas"), 257.5, 27, -122.5), new Location(Bukkit.getWorld("arenas"), 257.5, 27, -0.5), true));
        kitsFile = new File(getDataFolder(), "kits.yml");
        kits = YamlConfiguration.loadConfiguration(kitsFile);
        getServer().getPluginManager().registerEvents(new EventListener(), instance);
        for(Player player : Bukkit.getOnlinePlayers()){
            Map<Player, PlayerChecks> playerChecks = Duels.getPlayerChecks();
            if(playerChecks.isEmpty() || playerChecks.get(player) == null){
                playerChecks.put(player, new PlayerChecks(null));
            }
            Inventory inventory = player.getInventory();
            inventory.clear();
            player.teleport(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
            player.setHealth(20);
            player.setFoodLevel(20);

            inventory.setItem(0, ItemStacker.setDisplayName(new ItemStack(Material.IRON_SWORD), "§fДуэли"));
            player.updateInventory();

            AttributeInstance attack_speed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            if(attack_speed != null) {
                attack_speed.setBaseValue(attack_speed.getDefaultValue());
            }
        }
    }

    @Override
    public void onDisable() {
        try {
            kits.save(kitsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public DuelsArenas getFreeArena() {
        DuelsArenas[] duelsArenas = new DuelsArenas[arenas.size()];
        int i = 0;
        for (DuelsArenas arena : arenas) {
            if (arena.isAvailable()) {
                duelsArenas[i] = arena;
                i++;
            }
        }
        if(i > 0) {
            return duelsArenas[new Random().nextInt(i)];
        } else {
            return null;
        }
    }
    public static Map<String, DuelsQueue> getQueues() { return queues; }
    public static Map<Player, PlayerChecks> getPlayerChecks(){
        return playerChecks;
    }
    public static void setKit(String key, Object value) { kits.set(key, value); }
    public static Object getKit(String key) {
        return kits.get(key);
    }
    public static Set<String> getKitsKeys(boolean deep) { return kits.getKeys(deep); }
    public static Map<String, Object> getValues(boolean deep) { return kits.getValues(deep); }
    public void reloadConfig() {
        kitsFile = new File(getDataFolder(), "kits.yml");
        kits = YamlConfiguration.loadConfiguration(kitsFile);
    }
    public static Duels getInstance() {
        return instance;
    }
}
