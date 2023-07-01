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
    private final String duelPrefix = "§7[§aDuel§7] ";
    public DuelCommand() { super("duel"); }
    @Override
    public void execute(CommandSender sender, String label, String[] args){
        if(!(sender instanceof Player)) {
            sender.sendMessage("§7[§aDuel§7] §cВы должны быть игроком чтобы использовать эту команду");
            return;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§7[§aDuel§7] §7/duel invite <Никнейм> §8| §eОтправить запрос на дуэль игроку\n" +
                    "§7[§aDuel§7] §7/duel accept <Никнейм> §8| §eПринять запрос на дуэль от игрока\n" +
                    "§7[§aDuel§7] §7/duel deny <Никнейм> §8| §eОтклонить запрос на дуэль от игрока\n" +
                    "§7[§aDuel§7] §7/duel menu §8| §eОткрыть меню дуэлей\n" +
                    "§7[§aDuel§7] §7/duel reload §8| §eПерезагрузить конфигурацию");
            return;
        }
        PlayerChecks playerChecks = Duels.getPlayerChecks().get(player);
        DuelsQueue duelsQueue = playerChecks.getQueue();
        if (duelsQueue != null) {
            if (!duelsQueue.isPlayerInGame(player)) {
                player.sendMessage(duelPrefix + "§cЗапрещено использовать эту команду во время игры.");
                return;
            }
            if (!duelsQueue.isPlayerInQueue(player)) {
                player.sendMessage(duelPrefix + "§cЗапрещено использовать эту команду во время поиска игры.");
                return;
            }
        }
        switch (args[0].toLowerCase()) {
            case "invite":
                if (lengthLessThan2(args, player, duelPrefix + "§cНапишите никнейм игрока которому хотите отправить запрос.")) return;
                if (isPlayerOffline(args[1], player)) return;
                String targetName = args[1];
                if (targetName.equals(player.getName())) {
                    player.sendMessage(duelPrefix + "§cНельзя отправить запрос самому себе.");
                    return;
                }
                Inventory inventory = DuelsInventory.getInventory("1.9+ Запрос игроку " + targetName);
                player.openInventory(inventory);
                break;
            case "accept":
                if (lengthLessThan2(args, player, duelPrefix + "§cНапишите никнейм игрока от которого пришёл запрос.")) return;
                Player invite = Bukkit.getPlayer(args[1]);
                if (isPlayerOffline(args[1], player)) return;
                if(!playerChecks.containsInvites(args[1])) {
                    player.sendMessage(duelPrefix + "§cЭтот игрок не отправлял вам запрос.");
                    return;
                }
                playerChecks.getKeyInvites(args[1]).startGame(invite, player);
                playerChecks.removeInvites(args[1]);
                break;
            case "deny":
                if (lengthLessThan2(args, player, duelPrefix + "§cНапишите никнейм игрока от которого пришёл запрос.")) return;
                if (isPlayerOffline(args[1], player)) return;
                if(!playerChecks.containsInvites(args[1])) {
                    player.sendMessage(duelPrefix + "§cЭтот игрок не отправлял вам запрос.");
                    return;
                }
                playerChecks.removeInvites(args[1]);
                break;
            case "menu":
                player.openInventory(DuelsInventory.getInventory("Дуэли 1.9+ пвп"));
                break;
            case "reload":
                if (!player.hasPermission("duel.reload")) {
                    player.sendMessage(duelPrefix + "§cНедостаточно прав");
                }
                long startMillis = System.currentTimeMillis();
                Duels.getInstance().reloadConfig();
                player.sendMessage(duelPrefix + "§fКонфиг перезагружен за " + (System.currentTimeMillis() - startMillis) + "ms.");
                break;
        }
    }
    private boolean isPlayerOffline(String name, Player player) {
        if (Bukkit.getPlayer(name) == null) {
            player.sendMessage(duelPrefix);
            return true;
        }
        return false;
    }
    private boolean lengthLessThan2(String[] args, Player player, String text) {
        if (args.length < 2) {
            player.sendMessage(text);
            return true;
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("accept", "deny", "invite", "menu", "reload");
        else if(args.length == 2) {
            List<String> players = new ArrayList<>();
            if(args[0].equalsIgnoreCase("invite")){
                Bukkit.getOnlinePlayers().stream().filter(player -> player != sender).forEach(player -> players.add(player.getName()));
            }else if(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny")){
                players.addAll(Duels.getPlayerChecks().get((Player) sender).getKeySetInvites());
            }
            return players;
        }
        return Lists.newArrayList();
    }
}
