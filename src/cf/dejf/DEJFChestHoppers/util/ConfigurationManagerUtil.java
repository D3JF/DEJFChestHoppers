package cf.dejf.DEJFChestHoppers.util;

import cf.dejf.DEJFChestHoppers.DEJFChestHoppers;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManagerUtil {
    private static Map<String, Configuration> configDict = new HashMap<>();

    public static Configuration getPluginConfig(String config) {
        if (configDict.containsKey(config))
            return configDict.get(config);
        File file = new File(DEJFChestHoppers.getInstance().getDataFolder(), config);
        Configuration c = new Configuration(file);
        c.load();
        configDict.put(config, c);
        return c;
    }
    /*
    public static Configuration getPlayerConfig(String playerName) {
        return getPluginConfig(playerName+".config");
    }
     */

    public static Object get(Configuration c, String object) {
        return c.getProperty(object);
    }

    public static void addDefault(Configuration c, String object, Object value) {
        if (c.getProperty(object) == null)
            addTo(c, object, value);
    }

    public static void addTo(Configuration c, String object, Object value) {
        c.setProperty(object, value);
        c.save();
    }

    public static boolean contains(Configuration c, String object) {
        Object s = c.getProperty(object);
        return s != null;
    }

    public static void remove(Configuration c, String object) {
        c.removeProperty(object);
        c.save();
    }
}
