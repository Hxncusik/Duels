package kz.hxncus.mc.duels.kit;

import kz.hxncus.mc.duels.Duels;

import java.util.Map;

public class PlayerManager {
    private final Map<String, PlayerManager> playerManagerMap = Duels.getPlayerManagerMap();
    private final String playerName;
    private String kitName;
    private boolean isNaming;
    public PlayerManager(String playerName, String name, boolean isNaming) {
        this.playerName = playerName;
        this.kitName = name;
        this.isNaming = isNaming;
    }
    public String getKitName() {
        return kitName;
    }
    public void setKitName(String name) {
        this.kitName = name;
        playerManagerMap.put(playerName, this);
    }
    public boolean isNaming() {
        return isNaming;
    }
    public void setNaming(boolean naming) {
        isNaming = naming;
        playerManagerMap.put(playerName, this);
    }
}
