package com.mrsweeter.theFloorIsLava.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

import com.mrsweeter.theFloorIsLava.Party;
import com.mrsweeter.theFloorIsLava.TheFloorIsLava;

public class ExecuteCommand {

	public static boolean leaveParty(CommandSender sender) {
		if (sender instanceof Player)	{
			Player p = (Player) sender;
			
			Party party = Party.players.get(p.getUniqueId());
			if (party != null)	{
				party.playerLeave(p);
			}
		}
		return true;
	}

	public static boolean reload(CommandSender sender) {
		
		TheFloorIsLava pl = TheFloorIsLava.instance;
		
		PluginManager pm = pl.getServer().getPluginManager();
		pm.disablePlugin(pl);
		try{
			pm.enablePlugin(pl);
		}catch(UnknownDependencyException e){
			e.printStackTrace();
		}
		return true;
	}
}
