package com.mrsweeter.theFloorIsLava.ObjectLoader;

import com.mrsweeter.theFloorIsLava.TheFloorIsLava;
import com.mrsweeter.theFloorIsLava.Commands.FilCreate;
import com.mrsweeter.theFloorIsLava.Commands.FilLeave;
import com.mrsweeter.theFloorIsLava.Commands.FilReload;
import com.mrsweeter.theFloorIsLava.Commands.FilRemove;
import com.mrsweeter.theFloorIsLava.Commands.FilTeleport;

public class CommandsLoader {
	
	public static void registerCommand(TheFloorIsLava pl)	{
		
		pl.getCommand("flleave").setExecutor(new FilLeave());
		pl.getCommand("flreload").setExecutor(new FilReload());
		pl.getCommand("flteleport").setExecutor(new FilTeleport());
		pl.getCommand("flcreate").setExecutor(new FilCreate());
		pl.getCommand("flremove").setExecutor(new FilRemove());
		
	}
}
