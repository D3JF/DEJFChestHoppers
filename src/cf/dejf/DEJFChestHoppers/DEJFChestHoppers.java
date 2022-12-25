package cf.dejf.DEJFChestHoppers;

import cf.dejf.DEJFChestHoppers.listeners.BlockBreakListener;
import cf.dejf.DEJFChestHoppers.listeners.BlockPlaceListener;
import cf.dejf.DEJFChestHoppers.listeners.PistonListener;
import cf.dejf.DEJFChestHoppers.util.CommandExecutor;
import cf.dejf.DEJFChestHoppers.util.ConfigurationManager;
import cf.dejf.DEJFChestHoppers.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.StorageMinecart;
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

import static cf.dejf.DEJFChestHoppers.util.Util.isChunkLoaded;


public class DEJFChestHoppers extends JavaPlugin {
    // Basic Plugin Info
    private DEJFChestHoppers plugin;
    public Logger log;
    public String pluginName;
    private PluginDescriptionFile pdf;
    // Plugin Specific
    public static List<Location> hopperList = new ArrayList<>();
    public static DEJFChestHoppers instance;
    public static long refreshRate;
    public static DEJFChestHoppers getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();
        pdf = this.getDescription();
        pluginName = pdf.getName();
        log.info("[" + pluginName + "] Loading plugin version " + pdf.getVersion());


        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, new BlockPlaceListener(),
                Event.Priority.Low, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, new BlockBreakListener(),
                Event.Priority.Low, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PISTON_RETRACT, new PistonListener(),
                Event.Priority.Low, this);
        this.getCommand("hoppers").setExecutor(new CommandExecutor());

        instance = this;
        ConfigurationManager.load("hoppers");

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            //Iterate over entities as there are more likely to be more entities than hoppers
            //Scan Worlds
            for(World world : Bukkit.getServer().getWorlds()) {
                //Scan all entities in world
                for(Entity entity : world.getEntities()) {

                    if(entity instanceof StorageMinecart) {

                        Inventory minecartInventory = ((StorageMinecart) entity).getInventory();
                        ItemStack[] minecartInventoryContents = minecartInventory.getContents();

                        //Scan all hopper lists
                        for(int i = 0; i < hopperList.size(); i++) {
                            Location hopperLocation = hopperList.get(i).clone();
                            if(hopperLocation.getWorld() != entity.getWorld())
                                continue; //Don't check entities against hoppers in a different world
                            Block chestBlock = hopperLocation.getBlock();
                            if(hopperLocation.getY() == 0)
                                continue; //A hopper chest can't exist on layer 0 as no block can go below it
                            Block ironBlock = hopperLocation.getBlock().getRelative(0, -1, 0);
                            if(ironBlock.getType() != Material.IRON_BLOCK || chestBlock.getType() != Material.CHEST) {
                                log.info("[" + pluginName + "] Removing hopper at " + chestBlock.getX() + " " +
                                        chestBlock.getY() + " " + chestBlock.getZ() +
                                        " as it isn't a chest, or doesn't have an iron block below it.");
                                hopperList.remove(hopperLocation);
                                ConfigurationManager.save("hoppers");
                                continue;
                            }
                            Chest chest = (Chest) chestBlock.getState();
                            Inventory chestInventory = chest.getInventory();
                            ItemStack[] chestInventoryContents = chestInventory.getContents();

                            if(hopperLocation.distance(entity.getLocation()) <= 2) {
                                for(ItemStack itemStack : minecartInventoryContents) {
                                    if(itemStack != null) {
                                        if(Util.hasFreeSpace(chestInventory, itemStack)) {
                                            chestInventory.addItem(itemStack);
                                            minecartInventory.removeItem(itemStack);
                                        }
                                    }
                                }
                            }

                        }
                        return;

                    } else if(entity instanceof Item) {

                        //Scan all hopper lists
                        for(int i = 0; i < hopperList.size(); i++) {
                            Location hopperLocation = hopperList.get(i).clone();
                            if(hopperLocation.getWorld() != entity.getWorld())
                                continue; //Don't check entities against hoppers in a different world
                            if(!isChunkLoaded(hopperLocation)) continue; //If chunk isn't loaded, skip
                            Block chestBlock = hopperLocation.getBlock();
                            if(hopperLocation.getY() == 0)
                                continue; //A hopper chest can't exist on layer 0 as no block can go below it
                            Block ironBlock = hopperLocation.getBlock().getRelative(0, -1, 0);
                            if(ironBlock.getType() != Material.IRON_BLOCK || chestBlock.getType() != Material.CHEST) {
                                log.info("[" + pluginName + "] Removing hopper at " + chestBlock.getX() + " " +
                                        chestBlock.getY() + " " + chestBlock.getZ() +
                                        " as it isn't a chest, or doesn't have an iron block below it.");
                                hopperList.remove(hopperLocation);
                                ConfigurationManager.save("hoppers");
                                continue;
                            }
                            Chest chest = (Chest) chestBlock.getState();
                            Inventory chestInventory = chest.getInventory();
                            if(entity.getLocation().distance(chestBlock.getLocation().add(0, 1, 0)) < 1) {
                                //Item Info
                                Item itemToBeSucked = (Item) entity;
                                ItemStack naughtyStack = itemToBeSucked.getItemStack();
                                //Add item to chest
                                if(Util.hasFreeSpace(chestInventory, naughtyStack)) {
                                    chestInventory.addItem(naughtyStack);
                                    entity.remove();
                                }
                            }
                        }
                    }
                }
            }
        }, 20L, refreshRate);

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
