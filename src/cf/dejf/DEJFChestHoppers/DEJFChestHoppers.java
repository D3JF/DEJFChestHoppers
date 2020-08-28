package cf.dejf.DEJFChestHoppers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cf.dejf.DEJFChestHoppers.Util.isChunkLoaded;


public class DEJFChestHoppers extends JavaPlugin {
    // Basic Plugin Info
    private DEJFChestHoppers plugin;
    private Logger log;
    private String pluginName;
    private PluginDescriptionFile pdf;
    // Plugin Specific
    public static List<Location> hopperList = new ArrayList<>();
    public static DEJFChestHoppers instance;

    public static DEJFChestHoppers getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();
        pdf = this.getDescription();
        pluginName = pdf.getName();
        log.info("[" + pluginName + "] Is Loading, Version: " + pdf.getVersion());


        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, new BlockPlaceListener(),
                Event.Priority.Low, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, new BlockBreakListener(),
                Event.Priority.Low, this);
        this.getCommand("hoppers").setExecutor(new CommandExecutor());

        instance = this;
        SaveList.load("hoppers");

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            //Iterate over entities as there are more likely to be more entities then hoppers
            //Scan Worlds
            for (World world : Bukkit.getServer().getWorlds()) {
                //Scan all entities in world
                for (Entity entity : world.getEntities()) {
                    if (!(entity instanceof Item)) continue; //Make sure entity is item
                    //Scan all hopper lists
                    for (int i = 0; i < hopperList.size(); i++) {
                        Location hopperLocation = hopperList.get(i).clone();
                        if (hopperLocation.getWorld() != entity.getWorld())
                            continue; //Don't check entities against hoppers in a different world
                        if (!isChunkLoaded(hopperLocation)) continue; //If chunk isn't loaded, skip
                        Block chestBlock = hopperLocation.getBlock();
                        if (hopperLocation.getY() == 0)
                            continue; //A hopper chest can't exist on layer 0 as no block can go below it
                        Block ironBlock = hopperLocation.getBlock().getRelative(0, -1, 0);
                        if (ironBlock.getType() != Material.IRON_BLOCK || chestBlock.getType() != Material.CHEST) {
                            log.info("[" + pluginName + "] Removing hopper at " + chestBlock.getX() + " " + chestBlock.getY() + " " + chestBlock.getZ() + " as it isn't a chest, or doesn't have an iron block below it.");
                            hopperList.remove(hopperLocation);
                            SaveList.save("hoppers");
                            continue;
                        }
                        Chest chest = (Chest) chestBlock.getState();
                        Inventory chestInventory = chest.getInventory();
                        if (entity.getLocation().distance(chestBlock.getLocation().add(0.5, 1, 0.5)) < 0.5) {
                            //Item Info
                            Item itemToBeSucked = (Item) entity;
                            ItemStack naughtyStack = itemToBeSucked.getItemStack();
                            //Add item to chest
                            int freeSpace = 0;
                            for (ItemStack j : chestInventory.getContents()) {
                                if (j == null) {
                                    freeSpace += naughtyStack.getType().getMaxStackSize();
                                } else if (j.getType() == naughtyStack.getType()) {
                                    freeSpace += j.getType().getMaxStackSize() - j.getAmount();
                                }
                            }
                            if (naughtyStack.getAmount() <= freeSpace) {
                                chestInventory.addItem(naughtyStack);
                                entity.remove();
                            }

                        }

                    }
                }

            }
        }, 20L, 60L);

        log.info("[" + pluginName + "] loaded successfully!");
    }

    @Override
    public void onDisable() {
        log.info("[" + pluginName + "] unloaded successfully!");
    }

    public void log(Level level, String message) {
        log.log(level, "[" + pluginName + "] " + message);
    }
}
