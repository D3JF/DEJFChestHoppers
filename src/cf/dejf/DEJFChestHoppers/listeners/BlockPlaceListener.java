package cf.dejf.DEJFChestHoppers.listeners;

import cf.dejf.DEJFChestHoppers.DEJFChestHoppers;
import cf.dejf.DEJFChestHoppers.util.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener extends BlockListener {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();
        Block block = event.getBlock();
        Block blockBelow = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() - 1, event.getBlock().getZ());
        Block blockAbove = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() + 1, event.getBlock().getZ());

        if(block.getType() == Material.CHEST && blockBelow.getType() == Material.IRON_BLOCK) {
            player.sendMessage(ChatColor.YELLOW + "Hopper created!");
            DEJFChestHoppers.hopperList.add(block.getLocation());
            ConfigurationManager.save("hoppers");
            return;
        }
        
        if(block.getType() == Material.IRON_BLOCK && blockAbove.getType() == Material.CHEST) {
            player.sendMessage(ChatColor.YELLOW + "Hopper created!");
            DEJFChestHoppers.hopperList.add(blockAbove.getLocation());
            ConfigurationManager.save("hoppers");
        }

    }

}
