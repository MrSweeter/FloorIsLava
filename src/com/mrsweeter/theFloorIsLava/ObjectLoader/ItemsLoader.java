package com.mrsweeter.theFloorIsLava.ObjectLoader;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemsLoader {
	
	public static HashMap<String, ItemStack> ITEMS = new HashMap<>();
	
	public void clearAll()	{
		ITEMS.clear();
	}
	
	public ItemsLoader(String id, Material m, String name, List<String> lores, int amount, byte data) {
		
		ItemStack item = new ItemStack(m, amount, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lores);
		item.setItemMeta(meta);
		
		ITEMS.put(id, item);
	}
}
