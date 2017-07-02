package com.mrsweeter.theFloorIsLava.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.mrsweeter.theFloorIsLava.Party;
import com.mrsweeter.theFloorIsLava.TheFloorIsLava;

import Utils.Messages;
import Utils.PluginConfiguration;

public class FilRemove implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(args.length == 1){
				PluginConfiguration zones = TheFloorIsLava.instance.getConfigurations().getConfigByName("zones");
				if (zones.contains(args[0]))	{
					
					Party party = Party.PARTY_LIST.get(args[0]);
					party.stop();
					party.getNPCManager().killNPC();
					
					Party.PARTY_LIST.remove(args[0]);
					
					zones.set(args[0], null);
					zones.save();
				} else {
					p.sendMessage(Messages.getMessage("NPN"));
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (sender instanceof Player && args.length == 1)	{
			List<String> result = new ArrayList<>();
			result.addAll(Party.PARTY_LIST.keySet());
			return result;
		}
		return null;
	}
}
