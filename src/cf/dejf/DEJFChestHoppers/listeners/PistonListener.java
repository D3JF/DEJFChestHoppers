package cf.dejf.DEJFChestHoppers.listeners;

import cf.dejf.DEJFChestHoppers.DEJFChestHoppers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener extends BlockListener {


    @Override
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {

        // This may slow down things if there's a lot of hoppers,
        // but no better implementation can be made,
        // as event.getRetractLocation never wants to point to the correct location.
        for(Location hopperLocation : DEJFChestHoppers.hopperList) {
            if(hopperLocation.add(0, -1, 0).getBlock().getType() != Material.IRON_BLOCK) {
                DEJFChestHoppers.getInstance().log.info("[" + DEJFChestHoppers.getInstance().pluginName +
                        "] Removing hopper at " + hopperLocation.getX() + " " + hopperLocation.getY() +
                        " " + hopperLocation.getZ() + " as a piston retracted its iron block.");
            }
        }

    }



    public void onBlockPistonExtend(BlockPistonExtendEvent event) {

        // This may slow down things if there's a lot of hoppers,
        // but no better implementation can be made,
        // as event.getRetractLocation never wants to point to the correct location.
        for(Location hopperLocation : DEJFChestHoppers.hopperList) {
            if(hopperLocation.add(0, -1, 0).getBlock().getType() != Material.IRON_BLOCK) {
                DEJFChestHoppers.getInstance().log.info("[" + DEJFChestHoppers.getInstance().pluginName +
                        "] Removing hopper at " + hopperLocation.getX() + " " + hopperLocation.getY() +
                        " " + hopperLocation.getZ() + " as a piston retracted its iron block.");
            }
        }

    }

}
