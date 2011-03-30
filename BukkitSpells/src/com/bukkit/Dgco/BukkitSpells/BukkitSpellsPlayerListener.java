package com.bukkit.Dgco.BukkitSpells;

import java.io.*;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockEvent;
//import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.material.MaterialData;
import org.bukkit.*;





/**
 * Handle events for all Player related events
 * @author Dgco
 */
public class BukkitSpellsPlayerListener extends PlayerListener 
{
    private final BukkitSpells plugin;
    protected HashMap<String,Long> LastBlinkCast;

    public BukkitSpellsPlayerListener(BukkitSpells instance) 
    {
        plugin = instance;
        LastBlinkCast = new HashMap<String,Long>();
    }
    
    public boolean onPlayerCommand(CommandSender  sender,  
  		  Command  command,  
		  String  label,  
		  String[]  args) 
    {
 	  
 	   
 		if (command.getName().toString().equalsIgnoreCase("cast")) 
 		{
 			
 			if(args.length >0 && args[0].equals("blink"))
 			{
 				if(castBlink(sender,command,args))
 				{
 					//spell worked if here
 				}
 			}
 			return true;
 		}
 		
 		return false;
    }
 	
    
    public void onPlayerInteract(PlayerInteractEvent event)
    {
    	event.getAction();
		if (event.getAction().equals(Action.LEFT_CLICK_AIR))
    	{
    		event.getPlayer().sendMessage("Left Clicked in Air :D");
    	}
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			event.getPlayer().sendMessage("Right Clicked Block :D");
		}
    	return;
    }
    public void onPlayerAnimation(PlayerAnimationEvent event)
    {
    	event.getAnimationType();
		if(event.getAnimationType().equals(PlayerAnimationType.ARM_SWING))
    	{
			event.getPlayer().sendMessage("arm swung");
    	}
		
		
    }
    
    
    
    
   // @SuppressWarnings("unused")
	public boolean castBlink(CommandSender sender,Command command,String[] args)
    {
    	Player player = (Player) sender;
    	player.chat("You have blinked :D");
    	
    	if(canCastSpell(player,"Blink"))
    	{
    		//old magic spells code
    		Block target = player.getTargetBlock(null, 80); //second int is max range
    		BlockFace face = target.getFace(player.getLocation().getBlock());
    		
    		if (target == null) {
    			player.sendMessage("Too Far To Blink");
    			return false;
    		} else if (80 > 0 && getDistance(player,target) > 80) {
    			player.sendMessage("Too Far To Blink b");
    			return false;
    		} else if (60 > 0 && isOnCooldown(player,"Blink",60)) {
    			player.sendMessage("Spell Is On Cooldown");
    			return false;
    		}  else if (player.getWorld().getBlockTypeIdAt(target.getX(),target.getY()+1,target.getZ()) == 0 && player.getWorld().getBlockTypeIdAt(target.getX(),target.getY()+2,target.getZ()) == 0) {
    			// teleport to top of target block if possible
    			if (!removeRegents(player,getSpellCost("Blink"))) {
        			player.sendMessage("No Regents");
        			return false;
        		}
    			player.sendMessage("You Cast Blink!");
    		//	sendMessageToPlayersInRange(player,STR_CAST_OTHERS.replace("[caster]",player.getName()));
    			player.teleport(new Location(player.getWorld(), target.getX()+.5, (double)target.getY()+1, target.getZ()+.5 ,player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch()  ));
    			//if (COOLDOWN > 0) {
    			//	startCooldown(player);
    			//}
    			return true;
    		} else if (target.getTypeId() == 0 && player.getWorld().getBlockTypeIdAt(face.getModX(),face.getModY()+1,face.getModZ()) == 0) {
    			// otherwise teleport to face of target block
    			if (!removeRegents(player,getSpellCost("Blink"))) {
        			player.sendMessage("No Regents");
        			return false;
        		}
    			player.sendMessage("You cast blink");
    			//sendMessageToPlayersInRange(player,STR_CAST_OTHERS.replace("[caster]",player.getName()));
    			player.teleport(new Location(player.getWorld(),face.getModX()+.5,(double)face.getModY(),face.getModZ()+.5,player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch()));
    			//if (COOLDOWN > 0) {
    			//	startCooldown(player);
    			//}
    			return true;
    		} else {
    			// no place to stand
    			player.sendMessage("No place to stand :/");
    			return false;
    		}
	
    		
    		
    	} 
    	return false;
    }
    
    
    
    private boolean isOnCooldown(Player player, String spellname, int cooldowntime) {
		return false;
	}
    
    private boolean removeRegents(Player player, int[] regents)
    {
    	
    	Inventory inv = player.getInventory();
    	ItemStack[] items = inv.getContents();
    	int counter=0;
    	int holdi =-1;
    	int holdj =-1;
    	
    	
    	for(int i =0; i < items.length;i++)
    	{
    		if(items[i].getTypeId() == regents[0] && items[i].getAmount() >= regents[1])
    		{
    			holdi = i;
    			counter++;
    			break;
    		}
    	}
    	if(counter == 0 && regents[1] == 0)
    	{
    		counter++;
    	}
    	for(int j =0; j< items.length;j++)
    	{
    		if(items[j].getTypeId() == regents[2] && items[j].getAmount() >= regents[3])
    		{
    			holdj = j;
    			counter++;
    			break;
    		}
    	}
    	if(counter == 1 && regents[3] == 0 && holdj == -1)
    	{
    		counter++;
    	}
    	if(counter >=2)
    	{
    		if(holdi != -1) items[holdi].setAmount(items[holdi].getAmount() - regents[1]);
    		if(holdj != -1) items[holdj].setAmount(items[holdj].getAmount() - regents[3]);
    		return true;
    	}
    	
    	
    	
		return false;
    }

 
    public boolean canCastSpell(Player player, String spellname)
    {
    	return true;
    }    
    public int[] getSpellCost(String spellname)
    {
    	int[] spellcost = {0,0,0,0};
    	spellcost[0]=331;
    	spellcost[1]=0;
    	spellcost[2]=3;
    	spellcost[3]=1;
    	
    	return spellcost;
    }  
    public void addSpell(Player player, String spellname)
    {
    	
    }
    public void removeSpell(Player player, String spellname)
    {
    	
    }
    public int getSpellCooldown(String spellname)
    {
    	return 30;
    }
    public HashMap<Location,String> getSpellLocations()
    {
    	Location temploc = new Location(plugin.getServer().getWorld("world"), 100, 68, 90);
    	HashMap<Location,String> map = new HashMap<Location,String>();
    	map.put(temploc, "Blink");
    	return map;
    	
    }
    public void setPlayerMark(Player player,Location loc)
    {
    	
    }
    public Location getPLayerMark(Player player)
    {
    	return new Location(plugin.getServer().getWorld("world"),101,68,88);
    }
    
	public double getDistance(Player pl, Block tg) {
		Location player = pl.getLocation();
		Location target = tg.getLocation();
		return Math.sqrt(Math.pow(player.getX()-target.getX(),2) + Math.pow(player.getY()-target.getY(),2) + Math.pow(player.getZ()-target.getZ(),2));
	}

}

