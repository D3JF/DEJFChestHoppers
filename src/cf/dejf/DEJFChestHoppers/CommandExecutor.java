package cf.dejf.DEJFChestHoppers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("DEJFChestHoppers.hoppers") || commandSender.isOp()) {
            for (Block chestBlock : DEJFChestHoppers.hopperList) {
                Location bl = chestBlock.getLocation();
                commandSender.sendMessage("There is a hopper at " + bl.getBlockX() + " " + bl.getBlockY() + " " + bl.getBlockZ());
            }
            return true;
        } else {
            return false;
        }
    }
}
