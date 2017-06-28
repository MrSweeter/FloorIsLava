package com.mrsweeter.theFloorIsLava.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrsweeter.theFloorIsLava.Party;

public class FilLeave implements CommandExecutor	{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (sender instanceof Player)	{
			Player p = (Player) sender;
			
			Party party = Party.players.get(p.getUniqueId());
			if (party != null)	{
				party.playerLeave(p);
			}
		}
		return true;
		
	}
}
