package cf.dejf.DEJFChestHoppers;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("DEJFChestHoppers.hoppers") || commandSender.isOp()) {
            for (Location location : DEJFChestHoppers.hopperList) {
                commandSender.sendMessage("There is a hopper at " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
            }
            return true;
        } else {
            return false;
        }
    }
}
