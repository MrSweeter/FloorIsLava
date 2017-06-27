package com.mrsweeter.theFloorIsLava;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrsweeter.theFloorIsLava.Commands.Commands;
import com.mrsweeter.theFloorIsLava.Listeners.Damager;
import com.mrsweeter.theFloorIsLava.Listeners.Death;
import com.mrsweeter.theFloorIsLava.Listeners.InteractManager;
import com.mrsweeter.theFloorIsLava.Listeners.JoinQuit;
import com.mrsweeter.theFloorIsLava.Listeners.StopCommandInParty;

import Utils.ConfigurationCollection;
import Utils.ConsoleColor;
import Utils.PluginConfiguration;

public class TheFloorIsLava extends JavaPlugin	{
	
	public static Logger log = Logger.getLogger("Minecraft - Focus");
	public static PluginManager pm = Bukkit.getPluginManager();
	public static TheFloorIsLava instance;
	
	public static HashMap<String, Party> party = new HashMap<>();
	private static ConfigurationCollection configs;
	
	public void onEnable()	{
		
		instance = this;
		
		configs = new ConfigurationCollection(this);
		configs.addExistingConfiguration("configuration");
		configs.addFileConfiguration("zones");
		
		reloadZone();
		
		pm.registerEvents(new Damager(), this);
		pm.registerEvents(new JoinQuit(), this);
		pm.registerEvents(new InteractManager(), this);
		pm.registerEvents(new Death(), this);
		pm.registerEvents(new StopCommandInParty(), this);
		
		getCommand("flleave").setExecutor(new Commands());
		getCommand("flreload").setExecutor(new Commands());
		
		log.info(ConsoleColor.GREEN + "=============== " + ConsoleColor.YELLOW + "FloorIsLava enable" + ConsoleColor.GREEN + " ===============" + ConsoleColor.RESET);
	}
	
	private void reloadZone() {
		PluginConfiguration zones = configs.getConfigByName("zones");
		ConfigurationSection zone;
		for (String name : zones.getKeys(false))	{			
			zone = zones.getConfigurationSection(name);
			party.put(name, Party.createParty(zone));
			if (party.get(name) == null)	{
				party.remove(name);
				log.info(ConsoleColor.YELLOW + "Zone " + name + " non-chargee, configuration vide" + ConsoleColor.RESET);
			}
			else {log.info(ConsoleColor.YELLOW + "Zone " + name + " chargee" + ConsoleColor.RESET);}
		}
	}

	public void onDisable()	{
		
		CreateNPCGui.clear();
		for (Party p : party.values())	{
			if (p !=null)	{
				p.stop();
			}
		}
		
		log.info(ConsoleColor.GREEN + "=============== " + ConsoleColor.YELLOW + "FloorIsLava enable" + ConsoleColor.GREEN + " ===============" + ConsoleColor.RESET);
	}
	
	public ConfigurationCollection getConfigurations()	{
		return configs;
	}
}
