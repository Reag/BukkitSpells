package com.bukkit.Dgco.BukkitSpells;

import org.bukkit.block.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;

/**
 * BukkitSpells block listener
 * @author Dgco
 */
public class BukkitSpellsBlockListener extends BlockListener {
    private final BukkitSpells plugin;
    public String stuff ="";

    public BukkitSpellsBlockListener(final BukkitSpells plugin) {
        this.plugin = plugin;
    }
    
   
    
}
