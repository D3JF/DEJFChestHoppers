package cf.dejf.DEJFChestHoppers.listeners;

import cf.dejf.DEJFChestHoppers.DEJFChestHoppers;
import cf.dejf.DEJFChestHoppers.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class BlockBreakListener extends BlockListener {

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Block blockBelow = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() - 1, event.getBlock().getZ());
        Block blockAbove = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() + 1, event.getBlock().getZ());

        //TODO: Improve the check to consult with our hopper list first.

        if (block.getType() == Material.CHEST && blockBelow.getType() == Material.IRON_BLOCK) {
            player.sendMessage(ChatColor.YELLOW + "Hopper destroyed!");
            removeBlock(block.getLocation());
            ConfigurationManager.save("hoppers");
        } else if (block.getType() == Material.IRON_BLOCK && blockAbove.getType() == Material.CHEST) {
            player.sendMessage(ChatColor.YELLOW + "Hopper destroyed!");
            removeBlock(blockAbove.getLocation());
            ConfigurationManager.save("hoppers");
        }
    }

    public void removeBlock(Location location) {
        for (int i = 0; i < DEJFChestHoppers.hopperList.size(); i++) {
            Location location2 = DEJFChestHoppers.hopperList.get(i);
            if (location.getWorld() == location2.getWorld() && location.getBlockX() == location2.getBlockX() && location.getBlockY() == location2.getBlockY() && location.getBlockZ() == location2.getBlockZ()) {
                DEJFChestHoppers.hopperList.remove(i);
                ConfigurationManager.save("hoppers");
            }
        }
    }

}
