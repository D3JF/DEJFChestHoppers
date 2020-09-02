package cf.dejf.DEJFChestHoppers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        if (inputItemStack.getAmount() <= freeSpace) {
            return true;
        } else {
            return false;
        }
    }
}
