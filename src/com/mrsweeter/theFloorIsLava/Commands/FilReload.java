package com.mrsweeter.theFloorIsLava.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

import com.mrsweeter.theFloorIsLava.TheFloorIsLava;

public class FilReload implements CommandExecutor	{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
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
