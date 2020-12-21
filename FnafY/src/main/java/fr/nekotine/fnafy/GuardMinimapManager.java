package fr.nekotine.fnafy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.nekotine.fnafy.enums.Team;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;

public class GuardMinimapManager implements Listener{
	private final FnafYMain main;
	private List<Player> inCameraPlayers = new ArrayList<>();
	private static final BlockData OUTLINE_GREEN = Bukkit.createBlockData(Material.EMERALD_BLOCK);
	private static final BlockData OUTLINE_RED = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
	public GuardMinimapManager(FnafYMain main) {
		this.main=main;
	}
	public boolean enterCamera(Player player) {
		if(inCameraPlayers.contains(player)) return false;
		inCameraPlayers.add(player);
		main.getHeadListener().trackPlayer(player);
		return true;
	}
	public boolean leaveCamera(Player player) {
		if(!inCameraPlayers.contains(player)) return false;
		inCameraPlayers.remove(player);
		main.getHeadListener().untrackPlayer(player);
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
			if(true) {
				//Le test si les guard poss�dent cette cam�ra
				e.getRoom().drawGuardOutline(e.getPlayer(), OUTLINE_GREEN);
			}else {
				e.getRoom().drawGuardOutline(e.getPlayer(), OUTLINE_RED);
			}
		}
	}
	@EventHandler
	public void onPlayerActionRoom(ActionOnRoomEvent e) {
		if(e.getTeam()==Team.GUARD) {
			if(true) {
				//Le test si les guard poss�dent cette cam�ra
				//acc�der � la cam�ra
			}else {
				//message de refus
			}
		}
	}
}
