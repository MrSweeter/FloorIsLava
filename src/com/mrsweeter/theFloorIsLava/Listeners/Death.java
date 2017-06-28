package com.mrsweeter.theFloorIsLava.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.mrsweeter.theFloorIsLava.Party;

public class Death implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)	{
		
		Player p = event.getEntity();
		Party party = Party.players.get(p.getUniqueId());
		
		if(party != null){
			event.setDeathMessage(null);
			party.playerDeath(p);
		}
	}
}