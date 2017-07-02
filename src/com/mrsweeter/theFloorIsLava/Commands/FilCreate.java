package com.mrsweeter.theFloorIsLava.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrsweeter.theFloorIsLava.CreateEditorSession;

public class FilCreate implements CommandExecutor	{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(args.length == 2){
				CreateEditorSession.newCreateEditorSession(p, args[0], args[1]);
				return true;
			}
		}
		return false;
	}
}
