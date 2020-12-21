package fr.nekotine.fnafy.room;

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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.enums.Team;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.PlayerMoveHeadEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;
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
	private List<Player> playerInAftonRoom = new ArrayList<>();
	private List<Player> playerInGuardRoom = new ArrayList<>();
	
	public Room(FnafYMain main, String roomName, RoomType roomType, Location camLocation, HashMap<Animatronic,List<ASAnimation>> InRoomAnimations,
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
		if(playerInAftonRoom.contains(e.getPlayer())) {
			Bukkit.getPluginManager().callEvent(new ActionOnRoomEvent(e.getPlayer(), this, e.getAction(),Team.AFTON));
		}else if(playerInGuardRoom.contains(e.getPlayer())) {
			Bukkit.getPluginManager().callEvent(new ActionOnRoomEvent(e.getPlayer(), this, e.getAction(),Team.GUARD));
		}
	}
	@EventHandler
	public void playerMoveHeadEvent(PlayerMoveHeadEvent e) {
		updateOutline(e.getPlayer());
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
			if(playerInAftonRoom.contains(p)) {
				if(!AftonSurface.isOneSelected(loc)) {
					Bukkit.getPluginManager().callEvent(new UnlookAtRoomEvent(p, this, Team.AFTON));
					playerInAftonRoom.remove(p);
				}
				return;
			}else if(AftonSurface.isOneSelected(loc)){
				Bukkit.getPluginManager().callEvent(new LookAtRoomEvent(p, this, Team.AFTON));
				playerInAftonRoom.add(p);
				return;
			}
			if(playerInGuardRoom.contains(p)) {
				if(!GuardSurface.isOneSelected(loc)) {
					Bukkit.getPluginManager().callEvent(new UnlookAtRoomEvent(p, this, Team.GUARD));
					playerInGuardRoom.remove(p);
				}
				return;
			}else if(GuardSurface.isOneSelected(loc)){
				Bukkit.getPluginManager().callEvent(new LookAtRoomEvent(p, this, Team.GUARD));
				playerInGuardRoom.add(p);
				return;
			}
		}
	}
	public List<ASAnimation> getAnimatronicAnimations(Animatronic anim){
		return MinimapPoses.get(anim);
	}
}
