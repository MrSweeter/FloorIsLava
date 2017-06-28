package com.mrsweeter.theFloorIsLava.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mrsweeter.theFloorIsLava.Party;

public class JoinQuit implements Listener	{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)	{
		
		Player p = event.getPlayer();
		Party.players.put(p.getUniqueId(), null);
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event)	{
		
		Player p = event.getPlayer();
		Party party = Party.players.remove(p.getUniqueId());
		
		if(party != null){
			party.playerLeave(p);
		}
	}
}