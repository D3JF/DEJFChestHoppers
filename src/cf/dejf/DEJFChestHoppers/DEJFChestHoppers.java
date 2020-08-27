package cf.dejf.DEJFChestHoppers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;


public class DEJFChestHoppers extends JavaPlugin {

    public static List<Block> hopperList = new ArrayList<>();
    public static DEJFChestHoppers instance;

    public static DEJFChestHoppers getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        System.out.println("DEJFChestHoppers 1.0 unloaded successfully!");
    }

    @Override
    public void onEnable() {

        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, new BlockPlaceListener(),
                Event.Priority.Low, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, new BlockBreakListener(),
                Event.Priority.Low, this);
        this.getCommand("hoppers").setExecutor(new CommandExecutor());

        instance = this;
        SaveList.load("hoppers");

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for(Block chestBlock : hopperList) {
                    if(chestBlock.getType() == Material.CHEST) {
                        Chest chest = (Chest) chestBlock.getState();
                        Inventory chestInventory = chest.getInventory();
                        List<Entity> entities = chestBlock.getWorld().getEntities();

                        for (Entity entity : entities) {
                            if (entity instanceof Item && entity.getLocation().distance(chestBlock.getLocation().add(0.5, 1, 0.5)) < 0.5) {
                                Item itemToBeSucked = (Item) entity;
                                ItemStack naughtyStack = itemToBeSucked.getItemStack();

                                int freeSpace = 0;

                                for (ItemStack i : chestInventory.getContents()) {
                                    if (i == null) {
                                        freeSpace+=naughtyStack.getType().getMaxStackSize();
                                    } else if (i.getType() == naughtyStack.getType()) {
                                        freeSpace+=i.getType().getMaxStackSize() - i.getAmount();
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
            }
        }, 20L, 20L);

        System.out.println("DEJFChestHoppers 1.0 loaded successfully!");
    }
}
