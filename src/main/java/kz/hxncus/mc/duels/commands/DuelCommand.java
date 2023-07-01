package kz.hxncus.mc.duels.commands;

import com.google.common.collect.Lists;
import kz.hxncus.mc.duels.Duels;
import kz.hxncus.mc.duels.DuelsQueue;
import kz.hxncus.mc.duels.PlayerChecks;
import kz.hxncus.mc.duels.inventory.DuelsInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DuelCommand extends AbstractCommand{
    public DuelCommand() { super("duel"); }
    @Override
    public void execute(CommandSender sender, String label, String[] args){
        if (args.length == 0) {
            sender.sendMessage("§7[§aDuel§7] §7/duel invite <Никнейм> §8| §eОтправить запрос на дуэль определённому игроку\n" +
                    "§7[§aDuel§7] §7/duel accept <Никнейм> §8| §eПринять запрос на дуэль от определённого игрока\n" +
                    "§7[§aDuel§7] §7/duel deny <Никнейм> §8| §eОтклонить запрос на дуэль от определённого игрока");
            return;
        }
        if(sender instanceof Player){
            PlayerChecks playerChecks = Duels.getPlayerChecks().get(sender);
            DuelsQueue duelsQueue = playerChecks.getQueue();
            if(duelsQueue == null || !duelsQueue.isPlayerInGame((Player) sender) || !duelsQueue.isPlayerInQueue((Player) sender)) {
                if (args[0].equalsIgnoreCase("invite") && args.length > 1) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null && player != sender) {
                        Inventory inventory = DuelsInventory.getInventory("1.9+ Запрос игроку " + player.getName());
                        ((Player) sender).openInventory(inventory);
                    } else if (player == null) {
                        sender.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
                    } else {
                        sender.sendMessage("§7[§aDuel§7] §cНельзя отправить запрос самому себе.");
                    }
                } else if (args[0].equalsIgnoreCase("accept")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null) {
                        if(playerChecks.containsInvites(player)) {
                            DuelsQueue duelsQueue1 = playerChecks.getKeyInvites(player);
                            playerChecks.removeInvites(player);
                            duelsQueue1.startGame(player, (Player) sender);
                        }
                    } else {
                        sender.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
                    }
                } else if (args[0].equalsIgnoreCase("deny")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null) {
                        if(playerChecks.containsInvites(player)) {
                            playerChecks.removeInvites(player);
                        }
                    } else {
                        sender.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
                    }
                } else if (args[0].equalsIgnoreCase("menu")) {
                    ((Player) sender).openInventory(DuelsInventory.getInventory("Дуэли 1.9+ пвп"));
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("*")) {
                        long millis = System.currentTimeMillis();
                        Duels.getInstance().reloadConfig();
                        sender.sendMessage("§7[§aDuel§7] §fКонфиг перезагружен за " + (System.currentTimeMillis() - millis) + "ms.");
                    }
                }
            }
        }
    }
    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("accept", "deny", "invite", "menu", "reload");
        else if(args.length == 2) {
            List<String> players = new ArrayList<>();
            if(args[0].equalsIgnoreCase("invite")){
                for (Player player : Bukkit.getOnlinePlayers()){
                    if(player != sender) {
                        players.add(player.getName());
                    }
                }
            }else if(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny")){
                for (Player player : Duels.getPlayerChecks().get((Player) sender).getKeySetInvites()){
                    players.add(player.getName());
                }
            }
            return players;
        }
        return Lists.newArrayList();
    }
}
