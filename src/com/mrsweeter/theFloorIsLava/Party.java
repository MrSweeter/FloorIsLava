package com.mrsweeter.theFloorIsLava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.mrsweeter.theFloorIsLava.Listeners.InteractManager;

import Utils.Triplet;

public class Party {
	
	public static HashMap<String, Party> PARTY_LIST = new HashMap<String, Party>();
	
	public static Party createParty(ConfigurationSection config)	{
		
		if (config != null)	{
			World w = Bukkit.getWorld(config.getString("world"));
			
			double x = config.getDouble("game-manager.x"),
					y = config.getDouble("game-manager.y"),
					z = config.getDouble("game-manager.z"),
					yaw = config.getDouble("game-manager.yaw"),
					pitch = config.getDouble("game-manager.pitch");
			
			return new Party(
					config.getString("name"),
					w,
					new Location(w, config.getDouble("corner-1.x"), config.getDouble("corner-1.y"), config.getDouble("corner-1.z")),
					new Location(w, config.getDouble("corner-2.x"), config.getDouble("corner-2-y"), config.getDouble("corner-2.z")),
					new Location(w, config.getDouble("teleport-to-manager.x"), config.getDouble("teleport-to-manager.y"), config.getDouble("teleport-to-manager.z"), (float) config.getDouble("teleport-to-manager.yaw"), (float) config.getDouble("teleport-to-manager.pitch")),
					new Location(w, config.getDouble("teleport-to-arena.x"), config.getDouble("teleport-to-arena.y"), config.getDouble("teleport-to-arena.z"), (float) config.getDouble("teleport-to-arena.yaw"), (float) config.getDouble("teleport-to-arena.pitch")),
					config.getStringList("floor"),
					new Location(w, x, y, z, (float) yaw, (float) pitch)
					);
		}
		return null;
	}
	
	public static HashMap<UUID, Party> players = new HashMap<>();
	
	private String name;
	private World world;
	private Location corner1;
	private Location corner2;
	private Location lobby;
	private Location arena;
	private boolean isStarted;
	private boolean isEnable;
	private HashMap<Material, List<Integer>> floorLava = new HashMap<>();
	private HashMap<Location, ItemStack> floorNormal = new HashMap<>();
	private HashMap<UUID, Player> playerParty = new HashMap<>();
	private HashMap<UUID, Boolean> playerReady = new HashMap<>();
	private HashMap<UUID, Triplet> playerAntiAFK = new HashMap<>();
	private BukkitTask task;
	private CreateNPCGui gui;
	private int round;
	
	private Party(String name, World w, Location c1, Location c2, Location lobby, Location arena, List<String> list, Location npc) {
		this.name = name;
		this.world = w;
		
		if (c1.getBlockX() > c2.getBlockX())	{int temp = c2.getBlockX(); c2.setX(c1.getBlockX()); c1.setX(temp);}
		if (c1.getBlockY() < c2.getBlockY())	{int temp = c2.getBlockY(); c2.setY(c1.getBlockY()); c1.setY(temp);}
		if (c1.getBlockZ() > c2.getBlockZ())	{int temp = c2.getBlockZ(); c2.setZ(c1.getBlockZ()); c1.setZ(temp);}
		
		this.corner1 = c1;
		this.corner2 = c2;
		this.lobby = lobby;
		this.arena = arena;
		
		this.isEnable = false;
		this.isStarted = false;
		
		for (String str : list)	{
			List<Integer> data = new ArrayList<>();
			data.add(-1);
			
			if (str.contains(":"))	{
				
				int pos = str.indexOf(":");
				Material mat = Material.getMaterial(str.substring(0, pos).toUpperCase());
				data.clear();
				if (floorLava.containsKey(mat))	{
					data = floorLava.get(mat);
				}
				
				data.add(Integer.parseInt(str.substring(pos+1)));
				floorLava.put(mat, data);
				
			} else {
				floorLava.put(Material.getMaterial(str.toUpperCase()), data);
			}
		}
		
		gui = new CreateNPCGui(npc);
		gui.createGUI(this);

		PARTY_LIST.put(name, this);
	}

	public void updateGUI(Inventory inventory)	{
		gui.updateGUI(inventory, this);
	}
	
	public void enableLava()	{
		
		Block b;
		List<Integer> list;
		floorNormal.clear();
		
		if (!isEnable && isStarted)	{
			for (int i = corner1.getBlockX(); i <= corner2.getBlockX(); i++)	{
				for (int j = corner1.getBlockZ(); j <= corner2.getBlockZ(); j++)	{
					for (int k = corner1.getBlockY(); k >= corner2.getBlockY(); k--)	{
						
						b = world.getBlockAt(new Location(world, i, k, j));
						
						if (floorLava.containsKey(b.getType()))	{
							
							list = floorLava.get(b.getType());
							
							if (list.size() == 1)	{
								inMagmaList(list.get(0), i, k, j, b);
							} else {
								for (Integer v : list)	{
									inMagmaList(v, i, k, j, b);
								}
							}
							
						}
					}
				}
			}
		}
		
		updateBlockAfk();
	}
	
	@SuppressWarnings("deprecation")
	private void inMagmaList(int data, int i, int k, int j, Block b)	{
		if (data == -1 || data == b.getData())	{
			floorNormal.put(new Location(world, i, k, j), new ItemStack(b.getType(), 1, b.getData()));
			b.setType(Material.MAGMA);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void disableLava()	{
		if (isEnable && isStarted)	{
			Block current;
			for (Location b : floorNormal.keySet())	{
				current = world.getBlockAt(b);
				current.setType(floorNormal.get(b).getType());
				current.setData(floorNormal.get(b).getData().getData());
			}
			floorNormal.clear();
		}
	}
	
	public void updateBlockAfk()	{
		UUID uuid;
		Triplet triple;
		for (Player p : playerParty.values())	{
			uuid = p.getUniqueId();
			
			triple = new Triplet(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			
			if (playerAntiAFK.get(uuid) != null && playerAntiAFK.get(uuid).isSame(triple))	{
				p.damage(10);
				p.sendMessage("§cVous devez vous déplacer entre chaque coulée !");
			}
			else {playerAntiAFK.put(uuid, triple);}
			
		}
	}
	
	public int start(){
		if(this.isStarted){
			return 4; //La partie est déjà lancée
		}
		
		if(this.playerParty.size() < 2){
			return 5; //Pas assez de joueur pour lancer la partie
		}
		
		this.isStarted = true;
		this.EnableEvent();
		return 1;
	}

	private void EnableEvent() {
		
		this.spawnPlayer();
		this.sendUfo("§620 secondes avant la première coulée !", true, null);
		this.isEnable = false;
		task = new BukkitRunnable() {
			
			@Override
			public void run() {
				enable();
			}
		}.runTaskLater(TheFloorIsLava.instance, 400);
	}

	private void spawnPlayer() {
		
		for(Player p: this.playerParty.values()){
			p.teleport(this.arena);
			p.setGameMode(GameMode.ADVENTURE);
			p.setFlying(false);
			for(PotionEffect pe: p.getActivePotionEffects()){
				p.removePotionEffect(pe.getType());
			}
		}
	}

	public boolean isStarted() {
		return isStarted;
	}
	
	public boolean isEnable() {
		return isEnable;
	}
	
	public World getWorld()	{
		return world;
	}
	
	public Collection<Player> getPlayers(){
		return this.playerParty.values();
	}
	
	public int getPlayersReady(){
		int pReady = 0;
		for(Player player: getPlayers()){
			if(isReady(player)){
				pReady++;
			}
		}
		return pReady;
	}
	
	public Location getSpawnArena()	{
		return arena;
	}
	
	public Location getLobby()	{
		return lobby;
	}
	
	public void enable()	{
		if (task != null)	{task.cancel();}
		
		enableLava();
		this.isEnable = true;
		
		task = new BukkitRunnable() {
			
			@Override
			public void run() {
				disable();
			}
		}.runTaskLater(TheFloorIsLava.instance, 80);
	}
	
	public void disable()	{
		if (task != null)	{task.cancel();}
		
		round++;
		int time = 20 - 2*round;
		if (time < 3)	{time = 3;}
		
		this.sendUfo("§6" + time + " secondes avant la prochaine coulée !", true, null);
		disableLava();
		this.isEnable = false;
		
		task = new BukkitRunnable() {
			
			@Override
			public void run() {
				enable();
			}
		}.runTaskLater(TheFloorIsLava.instance, time * 20);
	}

	public String getName() {
		return name;
	}
	
	public void stop()	{
		this.endParty();
	}
	
	public boolean isReady(Player p)	{
		if(this.playerParty.get(p.getUniqueId()) != null){
			return this.playerReady.get(p.getUniqueId());
		}else{
			return false;
		}
	}
	
	public int addPlayer(Player p)	{
		
		if(this.isStarted){
			return 6; //party déja commencée
		}
		
		UUID eId = p.getUniqueId();
		
		Party party = players.get(eId);
		if(party != null){
			return 2; // code 2 => déja dans une partie
		}
		
		if (playerParty.size() == 0)	{
			Bukkit.broadcastMessage("§6" + p.getName() + "§e vous attends au §a/warp tfil §e!");
		}
		players.put(eId, this);
		this.playerParty.put(eId, p);
		this.playerReady.put(eId, false);
		return 1;
	}
	
	public int removePlayer(Player p)	{
		UUID eId = p.getUniqueId();
		
		if(this.playerParty.remove(eId) == null){
			return 3; //Il n'est pas dans cette partie
		}
		
		this.playerReady.remove(eId);
		players.replace(eId, null);
		return 1;
	}
	
	public boolean setReady(Player p){
		if (this.playerReady.get(p.getUniqueId()) == null)	{
			p.sendMessage("§cCe n'est pas votre partie !");
			p.closeInventory();
			return false;
		}
		if(this.playerReady.get(p.getUniqueId()))	{
			p.sendMessage("§cVous êtes déjà prêt !");
			p.closeInventory();
			return false;
		}
		this.playerReady.put(p.getUniqueId(), true);
		this.sendUfo("§eLe joueur "+p.getName()+" est prêt !", true, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
		p.closeInventory();
		if(this.canStart()){
			this.start();
		}
		return true;
	}
	
	private boolean canStart(){
		if(this.playerParty.size() <= 1)	{
			return false;
		}
		for(boolean cant: this.playerReady.values()){
			if(!cant){
				return false;
			}
		}
		return true;
	}
	
	private void sendUfo(String msg, boolean title, Sound sound) {
		for(Player p: this.playerParty.values()){
			
			p.sendMessage(msg);
			if(title){
				p.sendTitle("", msg, 20 , 20, 20);
			}
			if(sound != null){
				p.playSound(p.getLocation(), sound, 10, 1);
			}
		}
	}

	private void endParty() {
		String msg = "§e";
		if(this.playerParty.size() == 1){
			
			Iterator<Player> it = playerParty.values().iterator();
			Player win;
			while (it.hasNext())	{
				win = it.next();
				msg += win.getName() + " ";
			}
		} else {
			msg += "Personne ne ";
		}
		msg += "gagne la partie";
		this.sendUfo(msg, true, Sound.BLOCK_NOTE_PLING);
		this.reset();
	}

	public void playerDeath(Player p) {
		sendUfo("§e" + p.getName() + "§6 s'est noyé dans une coulée de lave !", false, Sound.ENTITY_BLAZE_DEATH);
		tpLobby(p);
		endParty();
	}
	
	public void playerLeave(Player p){
		if(this.removePlayer(p) == 1){
			if(this.isStarted){
				
				this.sendUfo("§6Le joueur §b"+p.getName()+"§6 a quitté la partie :(", true, null);
				
				if(this.playerParty.size() == 1){
					this.sendUfo("§6Fin de la partie car il n'y a plus assez de joueur :(", true, null);
					this.reset();
				}
				p.teleport(lobby);
			}
			this.tpLobby(p);
			p.sendMessage("§6Vous avez quitter la partie :(");
		}
		gui.updateGUI(InteractManager.NPC_MENU.get(gui.getUniqueIdNPC()), this);
	}

	private void tpLobby(Player p) {
		p.setHealth(20);
		p.teleport(this.lobby, TeleportCause.PLUGIN);
		for(PotionEffect pe: p.getActivePotionEffects()){
			p.removePotionEffect(pe.getType());
		}
		p.setGameMode(GameMode.SURVIVAL);
		players.replace(p.getUniqueId(), null);
	}

	public void reset() {
		for(Player p: this.playerParty.values()){
			this.tpLobby(p);
		}
		if (task != null)	{
			task.cancel();
		}
		this.disableLava();
		this.playerParty = new HashMap<>();
		this.playerReady = new HashMap<>();
		this.playerAntiAFK = new HashMap<>();
		gui.updateGUI(InteractManager.NPC_MENU.get(gui.getUniqueIdNPC()), this);
		this.isStarted = false;
	}
}
