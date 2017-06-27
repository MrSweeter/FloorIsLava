package com.mrsweeter.theFloorIsLava.Listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.mrsweeter.theFloorIsLava.Party;

public class InteractManager implements Listener	{
	
	public static HashMap<UUID, Inventory> NPC_MENU = new HashMap<>();
	
	@EventHandler
	public void onClickManager(PlayerInteractEntityEvent event)	{
		
		Entity victimE = event.getRightClicked();
		
		if(victimE instanceof Player){
			
			Player victim = (Player) victimE;
			Player murder = event.getPlayer();
			
			Party pVictim = Party.players.get(victim.getUniqueId());
			Party pMurder = Party.players.get(murder.getUniqueId());
			
			if(pVictim != null && pMurder != null){
				eject(victim, murder);
			}
			
		} else if (victimE instanceof Villager)	{
			
			Inventory inventory = NPC_MENU.get(victimE.getUniqueId());
			if (inventory != null)	{
				event.setCancelled(true);
				Player p = event.getPlayer();
				
				if (Party.players.get(p.getUniqueId()) == null || !Party.players.get(p.getUniqueId()).isStarted())	{
					p.openInventory(inventory);
				} else {
					event.getPlayer().sendMessage("This game is already running");
					event.setCancelled(true);
				}
			}
		}	
	}
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent event)	{
		
		Entity victimE = event.getEntity();
		
		if (victimE instanceof Villager){
			
			Inventory inventory = NPC_MENU.get(victimE.getUniqueId());
			if (inventory != null)	{
				event.setCancelled(true);
			}
		}	
	}
	
	@EventHandler
	public void onClickGui(InventoryClickEvent event)	{
		
		ItemStack item = event.getCurrentItem();
		
		if (item != null && item.getType() != Material.AIR && event.getClickedInventory() != null)	{
			String nameGUI = event.getInventory().getName().replace("§6", "");
			Party party = Party.PARTY_LIST.get(nameGUI);
			if (party != null)	{
				
				if (item.getType() == Material.MAGMA)	{
					
					switch (party.addPlayer((Player) event.getWhoClicked()))	{
					case 1: event.getWhoClicked().sendMessage("Vous accéder à une partie"); break;
					case 6: event.getWhoClicked().sendMessage("Partie en cours"); break;
					case 2: event.getWhoClicked().sendMessage("Vous êtes déjà dans une partie"); break;
					}
					event.setCancelled(true);
					
				} else if (item.getType() == Material.LAVA_BUCKET)	{
					
					party.setReady((Player) event.getWhoClicked());
					event.setCancelled(true);
				}
			}
		}
	}
	
	public static void eject(Player victim, Player p)	{
		Vector direction = p.getLocation().toVector().subtract(victim.getLocation().toVector());
		direction.multiply(-0.2);
		direction.add(new Vector(0, 0.7, 0));
		victim.setVelocity(direction);
	}
}
