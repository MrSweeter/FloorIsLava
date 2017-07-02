package com.mrsweeter.theFloorIsLava.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.mrsweeter.theFloorIsLava.Party;

import Utils.Messages;

public class FilTeleport implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (sender instanceof Player && args.length == 1)	{
			Player p = (Player) sender;
			
			Party party = Party.PARTY_LIST.get(args[0]);
			if (party != null)	{
				p.teleport(party.getLobby(), TeleportCause.PLUGIN);
			} else {
				p.sendMessage(Messages.getMessage("NPN"));
			}
			return true;
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
