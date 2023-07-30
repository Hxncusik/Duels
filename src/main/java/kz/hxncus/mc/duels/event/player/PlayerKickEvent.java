package kz.hxncus.mc.duels.event.player;

import kz.hxncus.mc.duels.Duels;
import kz.hxncus.mc.duels.DuelsArenas;
import kz.hxncus.mc.duels.DuelsQueue;
import kz.hxncus.mc.duels.PlayerChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;
import java.util.Map;

public class PlayerKickEvent implements Listener {
    @EventHandler
    public void onPlayerKick(org.bukkit.event.player.PlayerKickEvent event) {
        Player player = event.getPlayer();
        PlayerChecks checks = PlayerChecks.getPlayerChecks(player.getName());
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
}
