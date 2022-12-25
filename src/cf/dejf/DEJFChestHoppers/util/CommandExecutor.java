package cf.dejf.DEJFChestHoppers.util;

import cf.dejf.DEJFChestHoppers.DEJFChestHoppers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender.hasPermission("DEJFChestHoppers.hoppers") || sender.isOp()) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("This command cannot be used in console.");
                return true;
            }

            int radius = 10;
            if(args.length > 0) {
                try {
                    if(Integer.parseInt(args[0]) > 100) {
                        sender.sendMessage("");
                        radius = 100;
                    }
                    radius = Integer.parseInt(args[0]);
                } catch(NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Error: Invalid radius. " +
                            "(Expected integer, got \"" + args[0] + "\")");
                }
            }

            sender.sendMessage("Searching for hoppers within a radius of " + radius + " blocks...");
            for(Location location : Util.getNearbyHoppers(((Player) sender).getLocation(), radius)) {
                sender.sendMessage("There is a hopper at "
                        + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() +
                        " (Distance: " + Math.round(location.distance(((Player) sender).getLocation())) + ")");
            }

            return true;
        } else {
            return false;
        }
    }
}
