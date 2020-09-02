package cf.dejf.DEJFChestHoppers.listeners;

import cf.dejf.DEJFChestHoppers.DEJFChestHoppers;
import cf.dejf.DEJFChestHoppers.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener extends BlockListener {

    /*
    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
     */


    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();

        Block block = event.getBlock();
        Block blockBelow = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() - 1, event.getBlock().getZ());
        Block blockAbove = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() + 1, event.getBlock().getZ());

        if (block.getType() == Material.CHEST && blockBelow.getType() == Material.IRON_BLOCK) {
            player.sendMessage(ChatColor.YELLOW + "Hopper created!");
            DEJFChestHoppers.hopperList.add(block.getLocation());
            ConfigurationManager.save("hoppers");

        } else if (block.getType() == Material.IRON_BLOCK && blockAbove.getType() == Material.CHEST) {
            player.sendMessage(ChatColor.YELLOW + "Hopper created!");
            DEJFChestHoppers.hopperList.add(blockAbove.getLocation());
            ConfigurationManager.save("hoppers");
        }
    }


}
