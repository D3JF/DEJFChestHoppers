package cf.dejf.DEJFChestHoppers.util;

import cf.dejf.DEJFChestHoppers.DEJFChestHoppers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static boolean isChunkLoaded(Location location) {
        return location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }


    public static boolean hasFreeSpace(Inventory inputInventory, ItemStack inputItemStack) {
        int freeSpace = 0;
        for (ItemStack j : inputInventory.getContents()) {
            if (j == null) {
                freeSpace += inputItemStack.getType().getMaxStackSize();
            } else if (j.getType() == inputItemStack.getType()) {
                freeSpace += j.getType().getMaxStackSize() - j.getAmount();
            }
        }
        return inputItemStack.getAmount() <= freeSpace;
    }

    public static List<Location> getNearbyHoppers(Location startingLocation, int radius) {
        Location location;
        List<Location> nearbyHoppers = new ArrayList<>();
        for(int x = startingLocation.getBlockX() - radius; x <= startingLocation.getBlockX() + radius; x++) {
            for(int y = startingLocation.getBlockY() - radius; y <= startingLocation.getBlockY() + radius; y++) {
                for(int z = startingLocation.getBlockZ() - radius; z <= startingLocation.getBlockZ() + radius; z++) {
                    location = new Location(startingLocation.getWorld(), x, y, z);
                    if(!(location.getBlock().getType() == Material.CHEST))
                        continue;
                    if(DEJFChestHoppers.hopperList.contains(location)) {
                        System.out.println("hopper found lol");
                        nearbyHoppers.add(location);
                    }
                }
            }
        }
        return nearbyHoppers;
    }
}
