package fr.nekotine.fnafy.doorRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.animatronic.Animatronic;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.GameStopEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.PlayerMoveHeadEvent;
import fr.nekotine.fnafy.events.PlayerMoveToRoomEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;
import fr.nekotine.fnafy.team.Team;
import fr.nekotine.fnafy.utils.BlockSelection;

public class Room implements Listener{
	private final FnafYMain main;
	private final String roomName;
	private final RoomType roomType;
	private final Location camLocation;
	public final Material roomMaterial;
	private final HashMap<Animatronic,List<ASAnimation>> InRoomAnimations=new HashMap<Animatronic,List<ASAnimation>>();
	
	private final BlockSelection AftonSurface;
	private final BlockSelection AftonOutline;
	private final BlockSelection GuardOutline;
	private final BlockSelection GuardSurface;
	private final HashMap<Animatronic,List<ASAnimation>> MinimapPoses=new HashMap<Animatronic,List<ASAnimation>>();
	
	private List<Player> lookingAtAfton = new ArrayList<>();
	private List<Player> lookingAtGuard = new ArrayList<>();
	
	public final boolean canGuardUnlockCamera;
	public final boolean canGuardEnterRoom;
	private boolean unlockedByGuard;
	private final int aftonCameraPackage;
	
	public Room(FnafYMain main, String roomName, RoomType roomType, Location camLocation, Material roomMaterial, boolean canGuardUnlockCamera, boolean canGuardEnterRoom, boolean unlockedByGuard, int aftonCameraPackage, HashMap<Animatronic,List<ASAnimation>> InRoomAnimations,
			BlockSelection AftonSurface, BlockSelection AftonOutline, BlockSelection GuardSurface, BlockSelection GuardOutline,
			HashMap<Animatronic,List<ASAnimation>> MinimapPoses) {
		this.main=main;
		this.roomName = roomName;
		this.roomType = roomType;
		this.camLocation = camLocation;
		this.roomMaterial = roomMaterial;
		InRoomAnimations.putAll(InRoomAnimations);
		
		this.AftonSurface=AftonSurface;
		this.AftonOutline=AftonOutline;
		this.GuardOutline=GuardOutline;
		this.GuardSurface=GuardSurface;
		this.MinimapPoses.putAll(MinimapPoses);
		
		this.canGuardEnterRoom=canGuardEnterRoom;
		this.canGuardUnlockCamera=canGuardUnlockCamera;
		this.unlockedByGuard=unlockedByGuard;
		this.aftonCameraPackage=aftonCameraPackage;
		
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onGameStop(GameStopEvent e) {
		HandlerList.unregisterAll(this);
	}
	@EventHandler
	public void playerEnterRoom(PlayerMoveToRoomEvent e) {
		if(e.getGoingTo().equals(this)) {
			e.getPlayer().getInventory().clear();
			switch(roomType) {
			case VISION:
				e.getPlayer().getInventory().setItem(8, createItemStack(Material.TRIPWIRE_HOOK,ChatColor.GOLD+"Vision",
						ChatColor.GOLD+"Cliquez pour débloquer une caméra"));
			case INCENDIE:
				setFireDoorItem(e.getPlayer());
			case CAMERA:
				e.getPlayer().getInventory().setItem(8, createItemStack(Material.OBSERVER,ChatColor.RED+"Camera",
						ChatColor.RED+"Cliquez pour utiliser les caméras"));
			default:
				e.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR));
			}
		}
	}
	private void setFireDoorItem(Player p) {
		p.getInventory().clear();
		if(main.doorRoomContainer.fireDoorClosed) {
			p.getInventory().setItem(8, createItemStack(Material.IRON_DOOR,ChatColor.RED+"Portes feux fermées",
				ChatColor.RED+"Cliquez pour ouvrir les portes incendies"));
		}else {
			p.getInventory().setItem(8, createItemStack(Material.OAK_DOOR,ChatColor.GREEN+"Portes feux ouvertes",
					ChatColor.GREEN+"Cliquez pour fermer les portes incendies"));
		}
	}
	private ItemStack createItemStack(Material m, String name, String... lore) {
		ItemStack is = new ItemStack(m);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		List<String> lorelist = new ArrayList<String>();
		for(String s : lore) {
			lorelist.add(s);
		}
		im.setLore(lorelist);
		return is;
	}
	public String getRoomName() {
		return roomName;
	}
	public RoomType getRoomType() {
		return roomType;
	}
	public Location getCamLocation() {
		return camLocation;
	}
	public HashMap<Animatronic,List<ASAnimation>> getInRoomAnimations() {
		return InRoomAnimations;
	}
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {
		if(e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK || e.getAction()==Action.LEFT_CLICK_AIR || e.getAction()==Action.LEFT_CLICK_BLOCK) {
			if(main.teamguard.isPlayerInTeam(e.getPlayer().getUniqueId())) {
				if(lookingAtGuard.contains(e.getPlayer())) {
					Bukkit.getPluginManager().callEvent(new ActionOnRoomEvent(e.getPlayer(), this, e.getAction(),Team.GUARD));
				}else {
					if(e.getMaterial()!=Material.AIR) {
						if(main.teamguard.getWrapper(e.getPlayer().getUniqueId()).currentRoom.equals(this)) {
							switch(roomType) {
							case VISION:
								if(main.doorRoomContainer.visionOnCooldown) {
									e.getPlayer().sendMessage(ChatColor.RED+"Cooldown: "+ChatColor.GOLD+(int)main.doorRoomContainer.visionCooldown/20+ChatColor.WHITE+"s");
								}else {
									main.doorRoomContainer.openVisionInventory(e.getPlayer());
								}
							case CAMERA:
								main.guardMinimapManager.enterCamera(e.getPlayer());
							case INCENDIE:
								main.doorRoomContainer.switchFireDoors();
								setFireDoorItem(e.getPlayer());
							default:
								return;
							}
						}
					}
				}
			}else if(lookingAtAfton.contains(e.getPlayer())) {
				Bukkit.getPluginManager().callEvent(new ActionOnRoomEvent(e.getPlayer(), this, e.getAction(),Team.AFTON));
			}
		}
	}
	private void updateOutline(Player p) {
		Block block = p.getTargetBlockExact(50, FluidCollisionMode.NEVER);
		if(block!=null) {
			Location loc = block.getLocation();
			if(main.teamafton.isPlayerInTeam(p.getUniqueId())) {
				if(lookingAtAfton.contains(p)) {
					if(!AftonSurface.isOneSelected(loc)) {
						Bukkit.getPluginManager().callEvent(new UnlookAtRoomEvent(p, this, Team.AFTON));
						lookingAtAfton.remove(p);
					}
					return;
				}else if(AftonSurface.isOneSelected(loc)){
					Bukkit.getPluginManager().callEvent(new LookAtRoomEvent(p, this, Team.AFTON));
					lookingAtAfton.add(p);
					return;
				}
			}else {
				if(lookingAtGuard.contains(p)) {
					if(!GuardSurface.isOneSelected(loc)) {
						Bukkit.getPluginManager().callEvent(new UnlookAtRoomEvent(p, this, Team.GUARD));
						lookingAtGuard.remove(p);
					}
					return;
				}else if(GuardSurface.isOneSelected(loc)){
					Bukkit.getPluginManager().callEvent(new LookAtRoomEvent(p, this, Team.GUARD));
					lookingAtGuard.add(p);
					return;
				}
			}
		}
	}
	@EventHandler
	public void playerMoveHeadEvent(PlayerMoveHeadEvent e) {
		updateOutline(e.getPlayer());
	}
	public int getAftonCameraPackage() {
		return aftonCameraPackage;
	}
	public boolean isUnlockedByGuard() {
		return unlockedByGuard;
	}
	public void setUnlockedByGuard(boolean b) {
		unlockedByGuard=b;
	}
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent e) {
		if(main.getHeadListener().getTrackedPlayers().contains(e.getPlayer())){
			updateOutline(e.getPlayer());
		}
	}
	public void drawGuardOutline(Player p, BlockData outline) {
		GuardOutline.outline(p, outline);
	}
	public void hideGuardOutline(Player p) {
		GuardOutline.disOutline(p);
	}
	public void drawAftonOutline(Player p, BlockData outline) {
		AftonOutline.outline(p, outline);
	}
	public void hideAftonOutline(Player p, BlockData outline) {
		AftonOutline.disOutline(p);
	}
	public List<ASAnimation> getMinimapAnimations(Animatronic anim){
		return MinimapPoses.get(anim);
	}
	public List<ASAnimation> getAnimations(Animatronic anim){
		return InRoomAnimations.get(anim);
	}
}
