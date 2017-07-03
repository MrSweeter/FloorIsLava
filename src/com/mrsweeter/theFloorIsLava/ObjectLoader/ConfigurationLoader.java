package com.mrsweeter.theFloorIsLava.ObjectLoader;

import org.bukkit.GameMode;

import com.mrsweeter.theFloorIsLava.Party;
import com.mrsweeter.theFloorIsLava.TheFloorIsLava;

import Utils.ConfigurationCollection;
import Utils.PluginConfiguration;

public class ConfigurationLoader {
	
	public static ConfigurationCollection createFile(TheFloorIsLava pl)	{
		
		ConfigurationCollection configs = new ConfigurationCollection(pl);
		configs.addExistingConfiguration("configuration");
		configs.addExistingConfiguration("lang");
		configs.addFileConfiguration("zones");
		return configs;
		
	}
	
	public static void loadConfiguration(PluginConfiguration config)	{
		
		Party.during = GameMode.valueOf(config.getString("party.gamemode-ig").toUpperCase());
		Party.after = GameMode.valueOf(config.getString("party.gamemode-after").toUpperCase());
		Party.coutdownStart = config.getInt("party.countdown-start");
		Party.countdownStep = config.getInt("party.countdown-step");
		Party.lavaTime = config.getInt("party.lava-time");
		
	}
}
