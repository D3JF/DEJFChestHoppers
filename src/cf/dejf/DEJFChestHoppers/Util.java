package cf.dejf.DEJFChestHoppers;

import org.bukkit.Location;

public class Util {

    public static boolean isChunkLoaded(Location location) {
        return location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }
}
