package kz.hxncus.mc.duels;

import org.bukkit.Location;

public class DuelsArenas {
    private String name;
    private Location spawnPoint1;
    private Location spawnPoint2;
    private boolean isAvailable;

    public DuelsArenas(String name, Location spawnPoint1, Location spawnPoint2, boolean isAvailable) {
        this.name = name;
        this.spawnPoint1 = spawnPoint1;
        this.spawnPoint2 = spawnPoint2;
        this.isAvailable = isAvailable;
    }

    public String getName() { return name; }

    public Location getSpawnPoint1() {
        return spawnPoint1;
    }

    public Location getSpawnPoint2() {
        return spawnPoint2;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
