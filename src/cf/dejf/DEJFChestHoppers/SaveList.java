package cf.dejf.DEJFChestHoppers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.config.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveList {

    public static void save(String configName) {
        Configuration config = ModConfig.getPluginConfig(configName + ".config");
        Map<String, Map<String, Object>> listList = new HashMap<>();
        for(int i = 0; i < DEJFChestHoppers.hopperList.size(); i++) {
            Block block = DEJFChestHoppers.hopperList.get(i);
            Map<String, Object> locationStringList = new HashMap<>();
            Location location = block.getLocation();
            locationStringList.put("x", location.getX());
            locationStringList.put("y", location.getY());
            locationStringList.put("z", location.getZ());
            locationStringList.put("world", block.getWorld().getName());
            listList.put(""+i, locationStringList);
        }
        config.setProperty("hopperList", listList);
        config.save();
    }

    public static void load(String configName) {
        Configuration config = ModConfig.getPluginConfig(configName + ".config");
        if(config.getNode("hopperList") == null) { return; }
        List<String> strings = config.getKeys("hopperList");
        for(String s : strings) {
            String worldName = config.getNode("hopperList").getNode(s).getString("world", "world");
            double x = config.getNode("hopperList").getNode(s).getDouble("x", 0);
            double y = config.getNode("hopperList").getNode(s).getDouble("y", 0);
            double z = config.getNode("hopperList").getNode(s).getDouble("z", 0);
            World world = Bukkit.getServer().getWorld(worldName);
            Location loc = new Location(world, x, y, z);
            DEJFChestHoppers.hopperList.add(loc.getBlock());
        }


        List<String> list = new ArrayList<>();
        // Imagine we added a bunch of stuff to the list
        list.forEach(System.out::println);

    }

}
