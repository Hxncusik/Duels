package kz.hxncus.mc.duels.event.player;

import kz.hxncus.mc.duels.inventory.KitCreatorInventory;
import kz.hxncus.mc.duels.kit.KitManager;
import kz.hxncus.mc.duels.kit.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static kz.hxncus.mc.duels.EventListener.setupPlayerManager;

public class AsyncPlayerChatEvent implements Listener {
    @EventHandler
    public void onAsyncPlayerChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        PlayerManager playerManager = setupPlayerManager(playerName);
        if (!playerManager.isNaming()) {
            return;
        }
        event.setCancelled(true);
        playerManager.setNaming(false);
        String kitName = event.getMessage();
        if (KitManager.isKitHasBadSymbols(kitName)) {
            player.sendMessage("§7[§aDuel§7] §fДанное название кита содержит недопустимые символы.");
            return;
        }
        if (kitName.length() > 64) {
            player.sendMessage("§7[§aDuel§7] §fДанное название кита слишком длинное.");
            return;
        }
        if (KitManager.isKitNameExist(kitName)) {
            player.sendMessage("§7[§aDuel§7] §fДанное название кита уже используется.");
            return;
        }
        playerManager.setKitName(kitName);
        player.sendMessage("§7[§aDuel§7] §fУстановлено новое имя кита " + kitName);
        KitCreatorInventory.getInventory(player);
    }
}
