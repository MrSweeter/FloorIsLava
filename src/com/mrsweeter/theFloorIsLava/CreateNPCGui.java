package com.mrsweeter.theFloorIsLava;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import com.mrsweeter.theFloorIsLava.Listeners.InteractManager;
import com.mrsweeter.theFloorIsLava.ObjectLoader.ItemsLoader;

import Utils.ConsoleColor;
import Utils.Messages;

public class CreateNPCGui {
	
	private static LinkedList<Villager> NPC_LIST = new LinkedList<>();
	
	public static void clear()	{
		for (Villager v : NPC_LIST)	{
			v.remove();
		}
		NPC_LIST.clear();
	}
	
	private Villager npc;
	
	public CreateNPCGui(Location location, String name)	{
		if (this.spawnNPC(location, name))	{
			NPC_LIST.add(npc);
		}
	}
	
	public Location getLoc()	{
		return this.npc.getLocation();
	}
	
	public void moveNPC(Location location)	{
		this.npc.teleport(location);
	}
	
	public void killNPC()	{
		NPC_LIST.remove(this.npc);
		this.npc.remove();
	}
	
	public UUID getUniqueIdNPC()	{
		return npc.getUniqueId();
	}

	private boolean spawnNPC(Location location, String name) {
		
		if (location == null)	{TheFloorIsLava.log.info(ConsoleColor.YELLOW + "No location found for party: " + name + ConsoleColor.RESET); return false;}
		
		npc = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		npc.teleport(location);
		npc.setAdult();
		npc.setCustomNameVisible(true);
		npc.setProfession(Profession.LIBRARIAN);
		npc.setSilent(true);
		npc.setInvulnerable(true);
		npc.setCollidable(false);
		npc.setAgeLock(true);
		npc.setAI(false);
		npc.setGravity(false);
		return true;
		
	}
	
	public void updateGUI(Inventory inventory, Party party)	{
		
		String ready = Messages.getMessage("PR").replace("{VALUE}", party.getPlayersReady() + "/" + party.getPlayers().size());
		String ig = Messages.getMessage("PP").replace("{VALUE}", party.getPlayers().size() + "");
		
		new ItemsLoader("JP", Material.MAGMA, Messages.getMessage("JP"), Arrays.asList(Messages.getMessage("CHJ"), "", ig), 1, (byte) 0);
		new ItemsLoader("SRP", Material.LAVA_BUCKET, Messages.getMessage("R"), Arrays.asList(Messages.getMessage("CHSR"), "", ready), 1, (byte) 0);
		
		inventory.setItem(3, ItemsLoader.ITEMS.get("JP"));
		inventory.setItem(5, ItemsLoader.ITEMS.get("SRP"));
		
		InteractManager.NPC_MENU.put(npc.getUniqueId(), inventory);
		
	}

	public void createGUI(Party party)	{
		
		npc.setCustomName(party.getName());
		
		Inventory inventory = Bukkit.createInventory(null, 9, party.getName());
		
		updateGUI(inventory, party);
	}
}
