package kz.hxncus.mc.duels.commands;

import com.google.common.collect.Lists;
import kz.hxncus.mc.duels.Duels;
import kz.hxncus.mc.duels.DuelsQueue;
import kz.hxncus.mc.duels.PlayerChecks;
import kz.hxncus.mc.duels.inventory.DuelsInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DuelCommand extends AbstractCommand{
    private final String duelPrefix = "§7[§aDuel§7] ";
    public DuelCommand() { super("duel"); }
    @Override
    public void execute(CommandSender sender, String label, String[] args){
        if(!(sender instanceof Player)) {
            sender.sendMessage(duelPrefix + "§cВы должны быть игроком чтобы использовать эту команду");
            return;
        }
        if (args.length == 0) {
            sendDefaultHelpMessage(sender);
            return;
        }
        Player player = (Player) sender;
        PlayerChecks playerChecks = Duels.getPlayerChecks().get(player);
        if (checkDuelsQueue(player, playerChecks)) return;
        switch (args[0].toLowerCase()) {
            case "invite":
                invite(args, player);
                break;
            case "accept":
                if (needToWriteName(args.length, player, duelPrefix + "§cНапишите никнейм игрока от которого пришёл запрос.")) return;
                if (isPlayerOffline(args[1], player)) return;
                if (playerChecks.containsInvites(args[1])) {
                    player.sendMessage(duelPrefix + "§cЭтот игрок не отправлял вам запрос.");
                    return;
                }
                playerChecks.getKeyInvites(args[1]).startGame(Bukkit.getPlayer(args[1]), player);
                playerChecks.removeInvites(args[1]);
                break;
            case "deny":
                if (needToWriteName(args.length, player, duelPrefix + "§cНапишите никнейм игрока от которого пришёл запрос.")) return;
                if (isPlayerOffline(args[1], player)) return;
                if (playerChecks.containsInvites(args[1])) {
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
                Duels.getInstance().reloadConfig();
                player.sendMessage(duelPrefix + "§fКонфиг успешно перезагружен.");
                break;
            default:
                sendDefaultHelpMessage(sender);
        }
    }
    private void sendDefaultHelpMessage(CommandSender sender) {
        sender.sendMessage(duelPrefix + "§7/duel invite <Никнейм> §8| §eОтправить запрос на дуэль игроку\n" +
                duelPrefix + "§7/duel accept <Никнейм> §8| §eПринять запрос на дуэль от игрока\n" +
                duelPrefix + "§7/duel deny <Никнейм> §8| §eОтклонить запрос на дуэль от игрока\n" +
                duelPrefix + "§7/duel menu §8| §eОткрыть меню дуэлей\n" +
                duelPrefix + "§7/duel reload §8| §eПерезагрузить конфигурацию");
    }
    private void invite(String[] args, Player player) {
        if (needToWriteName(args.length, player, duelPrefix + "§cНапишите никнейм игрока которому хотите отправить запрос.")) return;
        if (isPlayerOffline(args[1], player)) return;
        if (args[1].equals(player.getName())) {
            player.sendMessage(duelPrefix + "§cНельзя отправить запрос самому себе.");
            return;
        }
        player.openInventory(DuelsInventory.getInventory("1.9+ Запрос игроку " + args[1]));
    }
    private boolean checkDuelsQueue(Player player, PlayerChecks playerChecks) {
        DuelsQueue duelsQueue = playerChecks.getQueue();
        if (duelsQueue != null && !duelsQueue.isPlayerInGame(player)) {
            player.sendMessage(duelPrefix + "§cЗапрещено использовать эту команду во время игры.");
            return true;
        }
        if (duelsQueue != null && !duelsQueue.isPlayerInQueue(player)) {
            player.sendMessage(duelPrefix + "§cЗапрещено использовать эту команду во время поиска игры.");
            return true;
        }
        return false;
    }

    private boolean isPlayerOffline(String name, Player player) {
        if (Bukkit.getPlayer(name) == null) {
            player.sendMessage(duelPrefix);
            return true;
        }
        return false;
    }
    private boolean needToWriteName(int length, Player player, String text) {
        if (length < 2) {
            player.sendMessage(text);
            return true;
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("accept", "deny", "invite", "menu", "reload");
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
