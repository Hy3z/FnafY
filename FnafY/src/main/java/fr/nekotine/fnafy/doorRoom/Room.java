package fr.nekotine.fnafy.doorRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.animatronic.Animatronic;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.GameStopEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.PlayerMoveHeadEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;
import fr.nekotine.fnafy.team.Team;
import fr.nekotine.fnafy.utils.BlockSelection;

public class Room implements Listener{
	private final FnafYMain main;
	private final String roomName;
	private final RoomType roomType;
	private final Location camLocation;
	private final HashMap<Animatronic,List<ASAnimation>> InRoomAnimations=new HashMap<Animatronic,List<ASAnimation>>();
	
	private final BlockSelection AftonSurface;
	private final BlockSelection AftonOutline;
	private final BlockSelection GuardOutline;
	private final BlockSelection GuardSurface;
	private final HashMap<Animatronic,List<ASAnimation>> MinimapPoses=new HashMap<Animatronic,List<ASAnimation>>();
	
	private List<Player> lookingAtAfton = new ArrayList<>();
	private List<Player> lookingAtGuard = new ArrayList<>();
	
	private boolean unlockedByGuard;
	private int aftonCameraPackage;
	
	public Room(FnafYMain main, String roomName, RoomType roomType, Location camLocation, boolean unlockedByGuard, int aftonCameraPackage, HashMap<Animatronic,List<ASAnimation>> InRoomAnimations,
			BlockSelection AftonSurface, BlockSelection AftonOutline, BlockSelection GuardSurface, BlockSelection GuardOutline,
			HashMap<Animatronic,List<ASAnimation>> MinimapPoses) {
		this.main=main;
		this.roomName = roomName;
		this.roomType = roomType;
		this.camLocation = camLocation;
		InRoomAnimations.putAll(InRoomAnimations);
		
		this.AftonSurface=AftonSurface;
		this.AftonOutline=AftonOutline;
		this.GuardOutline=GuardOutline;
		this.GuardSurface=GuardSurface;
		this.MinimapPoses.putAll(MinimapPoses);
		
		this.unlockedByGuard=unlockedByGuard;
		this.aftonCameraPackage=aftonCameraPackage;
		
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onGameStop(GameStopEvent e) {
		HandlerList.unregisterAll(this);
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
		if(lookingAtAfton.contains(e.getPlayer())) {
			Bukkit.getPluginManager().callEvent(new ActionOnRoomEvent(e.getPlayer(), this, e.getAction(),Team.AFTON));
		}else if(lookingAtGuard.contains(e.getPlayer())) {
			Bukkit.getPluginManager().callEvent(new ActionOnRoomEvent(e.getPlayer(), this, e.getAction(),Team.GUARD));
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
	private void updateOutline(Player p) {
		Block block = p.getTargetBlockExact(50, FluidCollisionMode.NEVER);
		if(block!=null) {
			Location loc = block.getLocation();
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
	public List<ASAnimation> getMinimapAnimations(Animatronic anim){
		return MinimapPoses.get(anim);
	}
	public List<ASAnimation> getAnimations(Animatronic anim){
		return InRoomAnimations.get(anim);
	}
}
