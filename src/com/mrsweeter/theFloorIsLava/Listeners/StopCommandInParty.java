package com.mrsweeter.theFloorIsLava.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.mrsweeter.theFloorIsLava.Party;

public class StopCommandInParty implements Listener	{
	
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent event)	{
		
		Party p = Party.players.get(event.getPlayer().getUniqueId());
		
		if(p != null && !event.getMessage().matches("/flleave") && !event.getPlayer().isOp())	{
			event.setCancelled(true);
			event.getPlayer().sendMessage("§4Vous devez quitter la partie pour faire cette commande. /flleave");
		}
	}
}