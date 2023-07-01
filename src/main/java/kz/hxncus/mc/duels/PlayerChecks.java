package kz.hxncus.mc.duels;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerChecks {
    private DuelsQueue queue;
    private final Map<Player, DuelsQueue> invites;
    public PlayerChecks(DuelsQueue queue) {
        this.invites = new HashMap<>();
        this.queue = queue;
    }
    public DuelsQueue getQueue() { return queue; }
    public void setQueue(DuelsQueue queue) { this.queue = queue; }
    public void addInvites(Player player, DuelsQueue queue){
        invites.put(player, queue);
    }
    public void removeInvites(Player player){
        invites.remove(player);
    }
    public boolean containsInvites(Player player) { return invites.containsKey(player); }
    public DuelsQueue getKeyInvites(Player player) { return invites.get(player); }
    public Set<Player> getKeySetInvites() { return invites.keySet(); }
}
