package kz.hxncus.mc.duels.commands;

import kz.hxncus.mc.duels.Duels;
import kz.hxncus.mc.duels.DuelsArenas;
import kz.hxncus.mc.duels.DuelsQueue;
import kz.hxncus.mc.duels.PlayerChecks;
import kz.hxncus.mc.duels.methods.ItemStacker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;

public class SpawnCommand extends AbstractCommand{
    public SpawnCommand() { super("spawn"); }
    @Override
    public void execute(CommandSender sender, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            PlayerChecks playerChecks = Duels.getPlayerChecks().get(player);
            DuelsQueue duelsQueue = playerChecks.getQueue();
            Inventory inventory = player.getInventory();
            player.setGameMode(GameMode.SURVIVAL);
            inventory.clear();
            inventory.setItem(0, ItemStacker.setDisplayName(new ItemStack(Material.IRON_SWORD), "§fДуэли"));
            player.teleport(new Location(Bukkit.getWorld("world"), 1422.5, 159, 1729.5));
            if (duelsQueue != null && duelsQueue.isPlayerInGame(player)) {
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
