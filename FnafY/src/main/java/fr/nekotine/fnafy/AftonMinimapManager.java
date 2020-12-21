package fr.nekotine.fnafy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;

import fr.nekotine.fnafy.enums.Team;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;
import fr.nekotine.fnafy.room.Room;

public class AftonMinimapManager {
	private final FnafYMain main;
	private List<Player> inCameraPlayers = new ArrayList<>();
	private static final BlockData OUTLINE_GREEN = Bukkit.createBlockData(Material.EMERALD_BLOCK);
	private static final BlockData OUTLINE_RED = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
	private static final BlockData OUTLINE_GOLD = Bukkit.createBlockData(Material.GOLD_BLOCK);
	public AftonMinimapManager(FnafYMain main) {
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
	public void itemHeldEvent(PlayerItemHeldEvent e) {
		Material previousMat = e.getPlayer().getInventory().getItem(e.getPreviousSlot()).getType();
		Material newMat = e.getPlayer().getInventory().getItem(e.getNewSlot()).getType();
		if(previousMat!=newMat) {
			clearAllOutline(e.getPlayer());
			if(!e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
				/*salleOùIlYALanimatronic.drawAftonOutline(e.getPlayer(), OUTLINE_GOLD);
				for(Room r : roomOùTuNePeuxPasDeplacerLanimatronic) {
					r.drawAftonOutline(e.getPlayer(), OUTLINE_RED);
				}*/
			}
		}
	}
	private void clearAllOutline(Player p) {
		for(Room r : main.getRoomManager().getRooms().values()) {
			r.hideGuardOutline(p);
		}
	}
	@EventHandler
	public void unlookAtRoom(UnlookAtRoomEvent e) {
		if(e.getTeam()==Team.AFTON) {
			if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
				e.getRoom().hideGuardOutline(e.getPlayer());
			}else if(true) {
				//si on peut déplacer l'animatronic dans la salle en question en fonction de la laine
				e.getRoom().hideGuardOutline(e.getPlayer());
			}
		}
	}
	@EventHandler
	public void onLookAtRoom(LookAtRoomEvent e) {
		if(e.getTeam()==Team.AFTON) {
			if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
				if(true) {
					//Le test si les anim possèdent cette caméra
					e.getRoom().drawAftonOutline(e.getPlayer(), OUTLINE_GREEN);
				}else {
					e.getRoom().drawAftonOutline(e.getPlayer(), OUTLINE_RED);
				}
			}else if(true) {
				//si on peut déplacer l'animatronic dans la salle en question en fonction de la laine. Method: door.canMoveFromTo
				e.getRoom().drawAftonOutline(e.getPlayer(), OUTLINE_GREEN);
			}
		}
	}
	@EventHandler
	public void onPlayerActionRoom(ActionOnRoomEvent e) {
		if(e.getTeam()==Team.AFTON) {
			if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
				if(true) {
					//si les anim possèdent la caméra
					//entrer dans la caméra
				}else {
					//message de refus
				}
			}else if(true) {
				//si cet animatronic peut se déplacer à la salle visée
				//déplacer l'animatronic
			}else {
				//message de refus
			}
		}
	}
	
}
