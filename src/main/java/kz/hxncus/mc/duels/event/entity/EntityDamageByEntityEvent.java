package kz.hxncus.mc.duels.event.entity;

import kz.hxncus.mc.duels.inventory.DuelsInventory;
import kz.hxncus.mc.duels.inventory.KitCreatorInventory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityEvent implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player victim = (Player) entity;
            if(damager instanceof Arrow) {
                ProjectileSource entityShooter = ((Arrow) damager).getShooter();
                if(entityShooter instanceof Player) {
                    Player shooter = (Player) entityShooter;
                    double victimHp = victim.getHealth() - event.getFinalDamage();
                    shooter.sendMessage("§7[§aDuel§7] §fВы попали в §e" + victim.getName() + " §fу него осталось §c" + String.format("%.2f", victimHp) + "❤");
                }
            } else if (damager instanceof Player) {
                Player player = (Player) event.getDamager();
                AttributeInstance attack_speed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
                if(attack_speed != null && attack_speed.getValue() >= 999) {
                    if(event.getFinalDamage() >= 0.5) {
                        victim.setVelocity(player.getLocation().getDirection().multiply(1.05).setY(0.3));
                        victim.setMaximumNoDamageTicks(15);
                    }
                }
            }
        }
        String customName = entity.getCustomName();
        if (customName == null) {
            return;
        }
        if (damager instanceof Player) {
            if (customName.equals("§cСоздать кит")) {
                Player player = (Player) event.getDamager();
                KitCreatorInventory.getInventory(player);
            } else if (customName.contains("Дуэли")) {
                DuelsInventory.getInventory("Дуэли 1.9+ пвп");
            }
        }
        event.setCancelled(true);
    }
}
