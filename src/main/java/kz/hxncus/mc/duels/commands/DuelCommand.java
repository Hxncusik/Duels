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
    public void execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
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
        if (duelsQueue != null && (!duelsQueue.isPlayerInGame(player) || !duelsQueue.isPlayerInQueue(player))) {
            player.sendMessage("§7[§aDuel§7] §cЗапрещено использовать эту команду во время игры или поиска игры.");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "invite":
                handleInviteCommand(player, args);
                break;
            case "accept":
                handleAcceptCommand(player, args, playerChecks);
                break;
            case "deny":
                handleDenyCommand(player, args, playerChecks);
                break;
            case "menu":
                handleMenuCommand(player);
                break;
            case "reload":
                handleReloadCommand(player);
                break;
            default:
                player.sendMessage("§7[§aDuel§7] §cНеверная команда.");
                break;
        }
    }

    private void handleInviteCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§7[§aDuel§7] §cНапишите никнейм игрока, которому хотите отправить запрос.");
            return;
        }
        String targetName = args[1];
        if (targetName.equals(player.getName())) {
            player.sendMessage("§7[§aDuel§7] §cНельзя отправить запрос самому себе.");
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
            return;
        }
        Inventory inventory = DuelsInventory.getInventory("1.9+ Запрос игроку " + targetName);
        player.openInventory(inventory);
    }

    private void handleAcceptCommand(Player player, String[] args, PlayerChecks playerChecks) {
        if (args.length < 2) {
            player.sendMessage("§7[§aDuel§7] §cНапишите никнейм игрока, от которого пришёл запрос.");
            return;
        }
        String inviter = args[1];
        Player invite = Bukkit.getPlayer(inviter);
        if (invite == null) {
            player.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
            return;
        }
        if (!playerChecks.containsInvites(inviter)) {
            player.sendMessage("§7[§aDuel§7] §cЭтот игрок не отправлял вам запрос.");
            return;
        }
        playerChecks.getKeyInvites(inviter).startGame(invite, player);
        playerChecks.removeInvites(inviter);
    }

    private void handleDenyCommand(Player player, String[] args, PlayerChecks playerChecks) {
        if (args.length < 2) {
            player.sendMessage("§7[§aDuel§7] §cНапишите никнейм игрока, от которого пришёл запрос.");
            return;
        }
        String inviter = args[1];
        if (Bukkit.getPlayer(inviter) == null) {
            player.sendMessage("§7[§aDuel§7] §cИгрок оффлайн");
            return;
        }
        if (!playerChecks.containsInvites(inviter)) {
            player.sendMessage("§7[§aDuel§7] §cЭтот игрок не отправлял вам запрос.");
            return;
        }
        playerChecks.removeInvites(inviter);
    }

    private void handleMenuCommand(Player player) {
        player.openInventory(DuelsInventory.getInventory("Дуэли 1.9+ пвп"));
    }

    private void handleReloadCommand(Player player) {
        if (!player.hasPermission("duel.reload")) {
            player.sendMessage("§7[§aDuel§7] §cНедостаточно прав");
            return;
        }
        long startMillis = System.currentTimeMillis();
        Duels.getInstance().reloadConfig();
        player.sendMessage("§7[§aDuel§7] §fКонфиг перезагружен за " + (System.currentTimeMillis() - startMillis) + "ms.");
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
