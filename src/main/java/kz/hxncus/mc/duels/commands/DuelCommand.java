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
            Player player = (Player) sender;
            PlayerChecks playerChecks = Duels.getPlayerChecks().get(player);
            DuelsQueue duelsQueue = playerChecks.getQueue();
            if (duelsQueue != null && !duelsQueue.isPlayerInGame(player) && !duelsQueue.isPlayerInQueue(player)) {
                return;
            }
            if (args[0].equalsIgnoreCase("invite") && args.length > 1) {
                Player invited = Bukkit.getPlayer(args[1]);
                if (invited == null) {
                    player.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
                    return;
                }
                if (invited == player) {
                    player.sendMessage("§7[§aDuel§7] §cНельзя отправить запрос самому себе.");
                    return;
                }
                Inventory inventory = DuelsInventory.getInventory("1.9+ Запрос игроку " + invited.getName());
                player.openInventory(inventory);
            } else if (args[0].equalsIgnoreCase("accept")) {
                Player inviter = Bukkit.getPlayer(args[1]);
                if (inviter == null) {
                    player.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
                    return;
                }
                if(!playerChecks.containsInvites(inviter)) {
                    player.sendMessage("§7[§aDuel§7] §cЭтот игрок не отправлял вам запрос.");
                    return;
                }
                DuelsQueue duelsQueue1 = playerChecks.getKeyInvites(inviter);
                playerChecks.removeInvites(inviter);
                duelsQueue1.startGame(inviter, player);
            } else if (args[0].equalsIgnoreCase("deny")) {
                Player inviter = Bukkit.getPlayer(args[1]);
                if (inviter == null) {
                    player.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
                    return;
                }
                if(!playerChecks.containsInvites(inviter)) {
                    player.sendMessage("§7[§aDuel§7] §cЭтот игрок не отправлял вам запрос.");
                    return;
                }
                playerChecks.removeInvites(inviter);
            } else if (args[0].equalsIgnoreCase("menu")) {
                player.openInventory(DuelsInventory.getInventory("Дуэли 1.9+ пвп"));
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("*")) {
                    long millis = System.currentTimeMillis();
                    Duels.getInstance().reloadConfig();
                    player.sendMessage("§7[§aDuel§7] §fКонфиг перезагружен за " + (System.currentTimeMillis() - millis) + "ms.");
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
