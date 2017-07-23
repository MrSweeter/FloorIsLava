package com.mrsweeter.theFloorIsLava;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrsweeter.theFloorIsLava.Listeners.Damager;
import com.mrsweeter.theFloorIsLava.Listeners.Death;
import com.mrsweeter.theFloorIsLava.Listeners.InteractManager;
import com.mrsweeter.theFloorIsLava.Listeners.JoinQuit;
import com.mrsweeter.theFloorIsLava.Listeners.StopCommandInParty;
import com.mrsweeter.theFloorIsLava.ObjectLoader.CommandsLoader;
import com.mrsweeter.theFloorIsLava.ObjectLoader.ConfigurationLoader;

import Utils.ConfigurationCollection;
import Utils.ConsoleColor;
import Utils.Messages;
import Utils.PluginConfiguration;

public class TheFloorIsLava extends JavaPlugin	{
	
	public static Logger log = Logger.getLogger("Minecraft - TheFloorIsLava");
	public static TheFloorIsLava instance;
//	public static MultiverseCore mv;

	private static ConfigurationCollection configs;
	
	public void onEnable()	{
		
		instance = this;
//		mv = (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		
		configs = ConfigurationLoader.createFile(this);
		
		loadListener();
		
		CommandsLoader.registerCommand(this);
		
		for (String key : configs.getConfigByName("lang").getKeys(false))	{
			Messages.loadMessages(configs.getConfigByName("lang").getConfigurationSection(key));
		}
		
		ConfigurationLoader.loadConfiguration(configs.getConfigByName("configuration"));
		
		reloadZone();
		
		log.info(ConsoleColor.GREEN + "=============== " + ConsoleColor.YELLOW + "FloorIsLava enable" + ConsoleColor.GREEN + " ===============" + ConsoleColor.RESET);
	}
	
	private void reloadZone() {
		PluginConfiguration zones = configs.getConfigByName("zones");
		ConfigurationSection zone;
		for (String name : zones.getKeys(false))	{			
			zone = zones.getConfigurationSection(name);
			Party.createParty(zone);
			if (Party.PARTY_LIST.get(name) == null)	{
				Party.PARTY_LIST.remove(name);
				log.info(ConsoleColor.YELLOW + Messages.getMessage("ECS").replace("{NAME}", name) + ConsoleColor.RESET);
			}
			else {log.info(ConsoleColor.YELLOW + Messages.getMessage("LZC").replace("{NAME}", name) + ConsoleColor.RESET);}
		}
	}

	public void onDisable()	{
		
		CreateNPCGui.clear();
		for (Party p : Party.PARTY_LIST.values())	{
			if (p !=null)	{
				p.stop();
			}
		}
		CreateEditorSession.clearAll();
		
		log.info(ConsoleColor.GREEN + "=============== " + ConsoleColor.YELLOW + "FloorIsLava disable" + ConsoleColor.GREEN + " ===============" + ConsoleColor.RESET);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		if(sender.isOp() || sender.hasPermission(command.getPermission())){
			return command.execute(sender, label, args);
		}
		return false;
	}
	
	private void loadListener(){
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new Damager(), this);
		pm.registerEvents(new JoinQuit(), this);
		pm.registerEvents(new InteractManager(), this);
		pm.registerEvents(new Death(), this);
		pm.registerEvents(new StopCommandInParty(), this);
	}
	
	public ConfigurationCollection getConfigurations()	{
		return configs;
	}
}
