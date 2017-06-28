package com.mrsweeter.theFloorIsLava;

import org.bukkit.scheduler.BukkitRunnable;

public class McTimerTask extends BukkitRunnable {
	
	String timer = "§e";
	int limit = 0;
	int time;
	Party party;
	
	public McTimerTask(int time, Party p) {
		for (int i = 0; i < time; i++)	{timer += "◼";}
		this.time = time;
		this.party = p;
	}
	
	@Override
	public void run() {
		
		if (limit == time)	{this.cancel();}
		if (timer.length() == 2)	{this.cancel();}
		
		party.sendUfo(timer, false, true, null);
		
		timer = timer.substring(0, timer.length()-1);
		limit++;
		
	}

}
