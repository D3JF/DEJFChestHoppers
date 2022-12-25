package cf.dejf.DEJFChestHoppers.listeners;

import cf.dejf.DEJFChestHoppers.DEJFChestHoppers;
import cf.dejf.DEJFChestHoppers.util.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakListener extends BlockListener {

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled())
            return;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Block blockBelow = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() - 1, event.getBlock().getZ());
        Block blockAbove = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() + 1, event.getBlock().getZ());

        if(block.getType() == Material.CHEST && blockBelow.getType() == Material.IRON_BLOCK) {
            if(!DEJFChestHoppers.hopperList.contains(block.getLocation()))
                return;
            player.sendMessage(ChatColor.YELLOW + "Hopper destroyed!");
            removeBlock(block.getLocation());
            ConfigurationManager.save("hoppers");
        } else if(block.getType() == Material.IRON_BLOCK && blockAbove.getType() == Material.CHEST) {
            if(!DEJFChestHoppers.hopperList.contains(blockAbove.getLocation()))
                return;
            player.sendMessage(ChatColor.YELLOW + "Hopper destroyed!");
            removeBlock(blockAbove.getLocation());
            ConfigurationManager.save("hoppers");
        }
    }

    public void removeBlock(Location location) {
        List<Location> foundHoppers = new ArrayList<>();
        for(int i = 0; i < DEJFChestHoppers.hopperList.size(); i++) {
            Location hopperLocation = DEJFChestHoppers.hopperList.get(i);
            if (location.getWorld() == hopperLocation.getWorld()
                    && location.getBlockX() == hopperLocation.getBlockX()
                    && location.getBlockY() == hopperLocation.getBlockY()
                    && location.getBlockZ() == hopperLocation.getBlockZ()) {
                foundHoppers.add(hopperLocation);
            }
        }
        DEJFChestHoppers.hopperList.removeAll(foundHoppers);
        ConfigurationManager.save("hoppers");
    }

}
