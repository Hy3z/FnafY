package fr.nekotine.fnafy.minimap;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.doorRoom.Camera;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.GameStopEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;
import fr.nekotine.fnafy.team.Team;

public class GuardMinimapManager implements Listener{
	private final FnafYMain main;
	private Location guardRoomLocation;
	private Location cameraBlockLocation;
	private Location cameraBaseLocation;
	private List<Player> inCameraPlayers = new ArrayList<>();
	private static final BlockData OUTLINE_GREEN = Bukkit.createBlockData(Material.EMERALD_BLOCK);
	private static final BlockData OUTLINE_RED = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
	public GuardMinimapManager(FnafYMain main) {
		this.main=main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onGameStop(GameStopEvent e) {
		HandlerList.unregisterAll(this);
	}
	public void setCameraBlockLocation(Location loc) {
		cameraBlockLocation=loc;
	}
	public Location getCameraBaseLocation() {
		return cameraBaseLocation;
	}
	public void setCameraBaseLocation(Location loc) {
		cameraBaseLocation=loc;
	}
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {
		if(e.getAction()==Action.LEFT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_BLOCK) {
			if(main.teamguard.isPlayerInTeam(e.getPlayer().getUniqueId())) {
				if(!inCameraPlayers.contains(e.getPlayer())) {
					if(e.getClickedBlock().getLocation().distanceSquared(cameraBlockLocation)<=1) {
						enterCamera(e.getPlayer());
					}
				}
			}
		}	
	}
	@EventHandler
	public void playerToggleFlight(PlayerToggleFlightEvent e) {
		if(inCameraPlayers.contains(e.getPlayer())) {
			e.setCancelled(true);
			leaveCamera(e.getPlayer());
		}
	}
	public boolean enterCamera(Player player) {
		if(inCameraPlayers.contains(player)) return false;
		inCameraPlayers.add(player);
		main.getHeadListener().trackPlayer(player);
		player.teleport(cameraBaseLocation);
		player.setAllowFlight(true);
		player.setFlying(true);
		return true;
	}
	public boolean leaveCamera(Player player) {
		if(!inCameraPlayers.contains(player)) return false;
		inCameraPlayers.remove(player);
		main.getHeadListener().untrackPlayer(player);
		player.setFlying(false);
		player.getPlayer().setAllowFlight(false);
		player.getPlayer().teleport(guardRoomLocation);
		return true;
	}
	@EventHandler
	public void unlookAtRoom(UnlookAtRoomEvent e) {
		if(e.getTeam()==Team.GUARD) {
			e.getRoom().hideGuardOutline(e.getPlayer());
		}
	}
	@EventHandler
	public void onLookAtRoom(LookAtRoomEvent e) {
		if(e.getTeam()==Team.GUARD) {
			if(main.teamguard.unlockedCameras.contains(e.getRoom().getRoomName())) {
				e.getRoom().drawGuardOutline(e.getPlayer(), OUTLINE_GREEN);
			}else if(e.getRoom().canGuardUnlockCamera) {
				e.getRoom().drawGuardOutline(e.getPlayer(), OUTLINE_RED);
			}
		}
	}
	@EventHandler
	public void onPlayerActionRoom(ActionOnRoomEvent e) {
		if(e.getTeam()==Team.GUARD) {
			if(main.teamguard.unlockedCameras.contains(e.getRoom().getRoomName())) {
				Bukkit.getPluginManager().registerEvents(new Camera(main, e.getRoom().getCamLocation(), e.getPlayer(), Team.GUARD), main);
			}else if(e.getRoom().canGuardUnlockCamera) {
				//message de refus en mode "vous n'avez pas débloqué cette cam"
			}else {
				//message de refus en mode "caméra non accessible"
			}
		}
	}
	public Location getGuardRoomLocation() {
		return guardRoomLocation;
	}
	public void setGuardRoomLocation(Location guardRoomLocation) {
		this.guardRoomLocation = guardRoomLocation;
	}
}
