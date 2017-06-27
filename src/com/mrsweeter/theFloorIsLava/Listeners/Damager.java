package com.mrsweeter.theFloorIsLava.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.mrsweeter.theFloorIsLava.Party;

public class Damager implements	Listener	{
	
	@EventHandler
	public void onPlayerMagmaDamage(EntityDamageByBlockEvent event)	{
		
		Entity victim = event.getEntity();
		Block b = event.getDamager();
		
		Party party = Party.players.get(victim.getUniqueId());
		if (party != null)	{
			if (b != null && b.getType() == Material.MAGMA)	{
				event.setDamage(10);
			}
		}
	}
	
	@EventHandler
	public void onPlayerHitPlayer(EntityDamageByEntityEvent event)	{
		
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)	{
			
			Player victim = (Player) event.getEntity();
			Player murder = (Player) event.getDamager();
			
			Party pVictim = Party.players.get(victim.getUniqueId());
			Party pMurder = Party.players.get(murder.getUniqueId());
			
			if(pVictim != null && pMurder != null){
				
				event.setCancelled(true);
				InteractManager.eject(victim, murder);
			}
		}
	}
}
