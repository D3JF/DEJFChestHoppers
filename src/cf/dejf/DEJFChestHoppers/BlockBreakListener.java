package cf.dejf.DEJFChestHoppers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class BlockBreakListener extends BlockListener {

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Block blockBelow = block.getLocation().subtract(0, 1, 0).getBlock();
        Block blockAbove = block.getLocation().add(0, 1, 0).getBlock();

        //TODO: Improve the check to consult with our hopper list first.

        if(block.getType() == Material.CHEST && blockBelow.getType() == Material.IRON_BLOCK){
            player.sendMessage(ChatColor.YELLOW + "Hopper destroyed!");
            DEJFChestHoppers.hopperList.remove(block);
            SaveList.save("hoppers");
        } else if(block.getType() == Material.IRON_BLOCK && blockAbove.getType() == Material.CHEST) {
            player.sendMessage(ChatColor.YELLOW + "Hopper destroyed!");
            DEJFChestHoppers.hopperList.remove(blockAbove);
            SaveList.save("hoppers");
        }
    }

}
