package com.mrsweeter.theFloorIsLava.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (sender.hasPermission(command.getPermission()))	{
			commandLabel = command.getLabel();
			switch (commandLabel)	{
			case "flleave":
				return ExecuteCommand.leaveParty(sender);
			case "flreload":
				return ExecuteCommand.reload(sender);
			}
		} else {
			sender.sendMessage("");
		}
		return true;
		
	}
}
