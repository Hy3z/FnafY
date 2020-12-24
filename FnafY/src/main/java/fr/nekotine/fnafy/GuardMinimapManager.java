package fr.nekotine.fnafy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import fr.nekotine.fnafy.enums.Team;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;

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
		if(!inCameraPlayers.contains(e.getPlayer())) {
			if(e.getClickedBlock().getLocation().distanceSquared(cameraBlockLocation)<=1) {
				enterCamera(e.getPlayer());
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
			}else {
				e.getRoom().drawGuardOutline(e.getPlayer(), OUTLINE_RED);
			}
		}
	}
	@EventHandler
	public void onPlayerActionRoom(ActionOnRoomEvent e) {
		if(e.getTeam()==Team.GUARD) {
			if(main.teamguard.unlockedCameras.contains(e.getRoom().getRoomName())) {
				Bukkit.getPluginManager().registerEvents(new Camera(main, e.getRoom().getCamLocation(), e.getPlayer(), Team.GUARD), main);
			}else {
				//message de refus
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
