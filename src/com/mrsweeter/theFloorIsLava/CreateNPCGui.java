package com.mrsweeter.theFloorIsLava;

import java.util.Arrays;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import com.mrsweeter.theFloorIsLava.Listeners.InteractManager;
import com.mrsweeter.theFloorIsLava.ObjectLoader.ItemsLoader;

public class CreateNPCGui {
	
	private static LinkedList<Villager> NPC_LIST = new LinkedList<>();
	
	public static void clear()	{
		for (Villager v : NPC_LIST)	{
			v.remove();
		}
		NPC_LIST.clear();
	}
	
	private Villager npc;
	
	public CreateNPCGui(Location location)	{
		this.spawnNPC(location);
		NPC_LIST.add(npc);
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

	private void spawnNPC(Location location) {
		
		npc = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		npc.setAdult();
		npc.setCustomNameVisible(true);
		npc.setProfession(Profession.NITWIT);
		npc.setSilent(true);
		npc.setInvulnerable(true);
		npc.setCollidable(false);
		npc.setAgeLock(true);
		npc.setAI(false);
		npc.setGravity(false);
		
	}
	
	public void createGUI(Party party)	{
		npc.setCustomName("§6" + party.getName());
		
		new ItemsLoader("JP", Material.MAGMA, "§9Rejoindre la partie", Arrays.asList("§fCliquez ici pour rejoindre la partie !"), 1, (byte) 0);
		new ItemsLoader("SRP", Material.LAVA_BUCKET, "§9Prêt ?", Arrays.asList("§fCliquez ici pour signaler", "§fque vous être prêt !"), 1, (byte) 0);
		
		Inventory inventory = Bukkit.createInventory(null, 9, "§6" + party.getName());
		inventory.setItem(3, ItemsLoader.ITEMS.get("JP"));
		inventory.setItem(5, ItemsLoader.ITEMS.get("SRP"));
		
		InteractManager.NPC_MENU.put(npc.getUniqueId(), inventory);
	}
}
