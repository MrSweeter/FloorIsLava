package com.mrsweeter.theFloorIsLava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import Utils.Messages;
import Utils.PluginConfiguration;

public class CreateEditorSession implements Listener	{
	
private static HashMap<UUID, CreateEditorSession> SESSIONS = new HashMap<UUID, CreateEditorSession>();
	
	public static void clearAll(){
		for(CreateEditorSession session : SESSIONS.values()){
			session.clear();
		}
	}
	
	public static void newCreateEditorSession(Player p, String name, String display){
		SESSIONS.put(p.getUniqueId(), new CreateEditorSession(p, name, display));
	}
	
	private Player p;
	private ItemStack[] saveInv;
	private String partyName;
	private String displayName;
	private Location spawn;
	private Location lobby;
	private Location manager;
	private Location corner1Loc;
	private Location corner2Loc;
	private Inventory floor;
	private ItemStack createSpawn;
	private ItemStack leaveEditor;
	private ItemStack createParty;
	private ItemStack createLobby;
	private ItemStack createNPC;
	private ItemStack createCorner1;
	private ItemStack createCorner2;
	private ItemStack createFloor;
	
	public CreateEditorSession(Player p, String name, String display){
		this.p = p;
		this.partyName = name;
		this.displayName = display.replace("&", "§").replace("_", " ");
		this.floor = Bukkit.createInventory(null, 54, Messages.getMessage("CF"));
		this.p.setGameMode(GameMode.CREATIVE);
		
		this.saveInventory();
		this.openEditInventory();
		
		TheFloorIsLava.instance.getServer().getPluginManager().registerEvents(this, TheFloorIsLava.instance);
		
	}
	
	private void saveInventory(){
		this.saveInv = this.p.getInventory().getContents();
		this.p.getInventory().clear();
	}
	
	private void loadInventory(){
		this.p.getInventory().clear();
		this.p.getInventory().setContents(this.saveInv);
	}
	
	private void openEditInventory(){
		PlayerInventory inv = this.p.getInventory();
		
		List<String> desc = new ArrayList<>();
		desc.add(Messages.getMessage("CSPL"));
		createSpawn = createItemStack(Material.BED, Messages.getMessage("CSP"), desc, 1);
		
		desc.clear();
		desc.add(Messages.getMessage("LEL"));
		leaveEditor = createItemStack(Material.BARRIER, Messages.getMessage("LE"), desc, 0);
		
		desc.clear();
		desc.add(Messages.getMessage("SPEL"));
		createParty = createItemStack(Material.BOOKSHELF, Messages.getMessage("SPE"), desc, 0);
		
		desc.clear();
		desc.add(Messages.getMessage("CLPL"));
		createLobby = createItemStack(Material.NETHER_STAR, Messages.getMessage("CLP"), desc, 0);
		
		desc.clear();
		desc.add(Messages.getMessage("CNPCPL"));
		createNPC = createItemStack(Material.EMERALD, Messages.getMessage("CNPCP"), desc, 0);
		
		desc.clear();
		desc.add(Messages.getMessage("CC1PL"));
		createCorner1 = createItemStack(Material.STRUCTURE_BLOCK, Messages.getMessage("CC1P"), desc, 0);
		
		desc.clear();
		desc.add(Messages.getMessage("CC2PL"));
		createCorner2 = createItemStack(Material.STRUCTURE_BLOCK, Messages.getMessage("CC2P"), desc, 0);
		
		desc.clear();
		desc.add(Messages.getMessage("CFL"));
		createFloor = createItemStack(Material.CHEST, Messages.getMessage("CF"), desc, 0);
		
		inv.setItem(0, createSpawn);
		inv.setItem(1, createLobby);
		inv.setItem(2, createFloor);
		inv.setItem(3, createCorner1);
		inv.setItem(4, createCorner2);
		inv.setItem(5, createNPC);
		inv.setItem(7, createParty);
		inv.setItem(8, leaveEditor);
	}
	
	public static ItemStack createItemStack(Material material, String name, List<String> lores, int data){
		
		ItemStack is = new ItemStack(material, 1, (byte) data);
		ItemMeta im = is.getItemMeta();
		
		im.setDisplayName(name);
		im.setLore(lores);
		
		is.setItemMeta(im);
		
		return is;
	}
	
	public void clear(){
		this.leave();
	}
	
	@SuppressWarnings("deprecation")
	private List<String> convertBlock(Inventory inv)	{
		
		ItemStack[] floor = inv.getContents();
		List<String> floorBlock = new ArrayList<>();
		List<String> materialUse = new ArrayList<>();
		Material m;
		int data;
		String toAdd;
		for (ItemStack i : floor)	{
			
			if (i != null)	{
				
				m = i.getType();
				data = i.getData().getData();
				if (m != Material.WATER_BUCKET)	{
					if (i.getAmount() > 1)	{data = i.getAmount();}
					
					if (data == 0)	{toAdd = m.toString();}
					else {toAdd = m.toString() + "-" + data;}
					
					if (materialUse.contains(m.toString()))	{
						if (floorBlock.remove(m.toString()))	{
							floorBlock.add(m.toString() + "-" + 0);
						} else {
							toAdd = m.toString() + "-" + data;
						}
					}
				} else {
					toAdd = "WATER";
					floorBlock.add("STATIONARY_WATER");
				}
				materialUse.add(m.toString());
				floorBlock.add(toAdd);
			}
		}
		toAdd = Messages.getMessage("CF") + ": §f";
		for (String str : floorBlock)	{
			toAdd += str + ", ";
		}
		this.p.sendMessage(toAdd);
		return floorBlock;
	}
	
	private void saveParty(){
		
		if(this.lobby == null)	{
			this.p.sendMessage(Messages.getMessage("LM"));
		} else if(this.spawn == null)	{
			this.p.sendMessage(Messages.getMessage("SM"));
		} else if(this.manager == null)	{
			this.p.sendMessage(Messages.getMessage("NPCM"));
		} else if(this.corner1Loc == null)	{
			this.p.sendMessage(Messages.getMessage("C1M"));
		} else if(this.corner2Loc == null)	{
			this.p.sendMessage(Messages.getMessage("C2M"));
		} else {
			
			PluginConfiguration config = TheFloorIsLava.instance.getConfigurations().getConfigByName("zones");
			
			ConfigurationSection section = config.createSection(partyName);
			ConfigurationSection info;
			section.set("name", displayName);
			section.set("world", this.lobby.getWorld().getName());
			
			info = section.createSection("game-manager");
			info.set("x", this.manager.getBlockX()+0.5);
			info.set("y", this.manager.getY());
			info.set("z", this.manager.getBlockZ()+0.5);
			info.set("yaw", this.manager.getYaw());
			info.set("pitch", this.manager.getPitch());

			info = section.createSection("teleport-to-manager");
			info.set("x", this.lobby.getBlockX()+0.5);
			info.set("y", this.lobby.getY());
			info.set("z", this.lobby.getBlockZ()+0.5);
			info.set("yaw", this.lobby.getYaw());
			info.set("pitch", this.lobby.getPitch());

			info = section.createSection("teleport-to-arena");
			info.set("x", this.spawn.getBlockX()+0.5);
			info.set("y", this.spawn.getY());
			info.set("z", this.spawn.getBlockZ()+0.5);
			info.set("yaw", this.spawn.getYaw());
			info.set("pitch", this.spawn.getPitch());

			info = section.createSection("corner-1");
			info.set("x", this.corner1Loc.getBlockX());
			info.set("y", this.corner1Loc.getBlockY());
			info.set("z", this.corner1Loc.getBlockZ());

			info = section.createSection("corner-2");
			info.set("x", this.corner2Loc.getBlockX());
			info.set("y", this.corner2Loc.getBlockY());
			info.set("z", this.corner2Loc.getBlockZ());
			
			List<String> floorBlock = convertBlock(floor);
			section.set("floor", floorBlock);
			
			config.save();
			
			Party.createParty(section);
			
			this.p.sendMessage(Messages.getMessage("PSC"));
		}
	}
	
	
	private void leave(){
		this.loadInventory();
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e){
		if(e.getPlayer().equals(this.p)){
			e.setCancelled(true);
			e.getPlayer().sendMessage(Messages.getMessage("QEFA"));
		}
	}
	
	@EventHandler
	public void cantPickupItem(PlayerPickupItemEvent e){
		if(e.getPlayer().equals(this.p)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void cantDropItem(PlayerDropItemEvent e){
		if(e.getPlayer().equals(this.p)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void cantOpenInventory(InventoryOpenEvent e){
		if(e.getPlayer().equals(this.p) && !e.getInventory().equals(floor)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void cantPoseBlock(BlockPlaceEvent e){
		if(e.getPlayer().equals(this.p)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void cantBreakBlock(BlockBreakEvent e){
		if(e.getPlayer().equals(this.p)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void cantTp(PlayerTeleportEvent e){
		if(e.getPlayer().equals(this.p)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent e){
		if(e.getPlayer().equals(this.p)){
			this.leave();
		}
	}
	
	@EventHandler
	public void useItem(PlayerInteractEvent e){
		e.setCancelled(true);
		if(e.getPlayer().equals(this.p) && e.getItem() != null){
			Player p = e.getPlayer();
			if(e.getItem().isSimilar(this.createParty))	{
				
				this.saveParty();
				
			} else if(e.getItem().isSimilar(this.leaveEditor))	{
				
				this.leave();
				p.sendMessage(Messages.getMessage("LEC"));
				
			} else if(e.getItem().isSimilar(this.createLobby))	{
				
				this.lobby = e.getPlayer().getLocation();
				String text = Messages.getMessage("CLPC");
				p.sendMessage(text.replace("{X}", "" + this.lobby.getBlockX()).replace("{Y}", "" + this.lobby.getBlockY()).replace("{Z}", "" + this.lobby.getBlockZ()));
				
			} else if(e.getItem().isSimilar(this.createSpawn))	{
				
				this.spawn = e.getPlayer().getLocation();
				String text = Messages.getMessage("CSPC");
				p.sendMessage(text.replace("{X}", "" + this.spawn.getBlockX()).replace("{Y}", "" + this.spawn.getBlockY()).replace("{Z}", "" + this.spawn.getBlockZ()));
				
			} else if(e.getItem().isSimilar(this.createNPC))	{
				
				this.manager = e.getPlayer().getLocation();
				String text = Messages.getMessage("CNPCPC");
				p.sendMessage(text.replace("{X}", "" + this.manager.getBlockX()).replace("{Y}", "" + this.manager.getBlockY()).replace("{Z}", "" + this.manager.getBlockZ()));
				
			} else if (e.getItem().isSimilar(this.createCorner1))	{
				
				this.corner1Loc = e.getPlayer().getLocation();
				String text = Messages.getMessage("CC1PC");
				p.sendMessage(text.replace("{X}", "" + this.corner1Loc.getBlockX()).replace("{Y}", "" + this.corner1Loc.getBlockY()).replace("{Z}", "" + this.corner1Loc.getBlockZ()));
				
			} else if (e.getItem().isSimilar(this.createCorner2))	{
				
				this.corner2Loc = e.getPlayer().getLocation();
				String text = Messages.getMessage("CC2PC");
				p.sendMessage(text.replace("{X}", "" + this.corner2Loc.getBlockX()).replace("{Y}", "" + this.corner2Loc.getBlockY()).replace("{Z}", "" + this.corner2Loc.getBlockZ()));
				
			} else if (e.getItem().isSimilar(createFloor))	{
				
				p.openInventory(floor);
				
			}
		}
	}
	
}
