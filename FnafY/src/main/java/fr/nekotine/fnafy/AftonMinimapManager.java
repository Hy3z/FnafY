package fr.nekotine.fnafy;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import fr.nekotine.fnafy.doors.Door;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.enums.Team;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.AnimatronicMoveAtDoorEvent;
import fr.nekotine.fnafy.events.GameStopEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;
import fr.nekotine.fnafy.room.Room;
import ru.xezard.glow.data.glow.Glow;

public class AftonMinimapManager implements Listener{
	private final FnafYMain main;
	private static final BlockData OUTLINE_GREEN = Bukkit.createBlockData(Material.EMERALD_BLOCK);
	private static final BlockData OUTLINE_RED = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
	private static final BlockData OUTLINE_GOLD = Bukkit.createBlockData(Material.GOLD_BLOCK);
	private Location cameraBaseLocation;
	private MinimapAnimatronic bonnie;
	private MinimapAnimatronic freddy;
	private MinimapAnimatronic chica;
	private MinimapAnimatronic foxy;
	private MinimapAnimatronic mangle;
	private MinimapAnimatronic springtrap;
	Glow glow = Glow.builder().animatedColor(ChatColor.WHITE).name("minimapGlow").build();
	public AftonMinimapManager(FnafYMain main) {
		this.main=main;
	}
	public void setCameraBaseLocation(Location loc) {
		cameraBaseLocation=loc;
	}
	public Location getCameraBaseLocation() {
		return cameraBaseLocation;
	}
	public Material getPlayerMaterialInHand(Player p) {
		return p.getInventory().getItemInMainHand().getType();
	}
	public boolean isCameraUnlocked(Room r) {
		return main.teamafton.getUnlockedPackages().contains(r.getAftonCameraPackage());
	}
	public Room getRoomFromWool(Player p) {
		return main.getRoomManager().getRoom(main.teamafton.getAnimatronic(Animatronic.getFromWool(getPlayerMaterialInHand(p))).currentRoom);
	}
	public boolean canMoveFromTo(Room prev, Room next) {
		return main.getRoomManager().canMoveFromToBool(prev,next);
	}
	
	public void drawAftonOutline(Room r, Player p, BlockData outline) {
		r.drawAftonOutline(p, outline);
	}
	@EventHandler
	public void itemHeldEvent(PlayerItemHeldEvent e) {
		Material previousMat = e.getPlayer().getInventory().getItem(e.getPreviousSlot()).getType();
		Material newMat = e.getPlayer().getInventory().getItem(e.getNewSlot()).getType();
		if(previousMat!=newMat) {
			clearAllOutline(e.getPlayer());
			glow.removeHolders(getAnimatronic(Animatronic.getFromWool(previousMat)).animator.as);
			if(!newMat.equals(Material.AIR)) {
				glow.addHolders(getAnimatronic(Animatronic.getFromWool(newMat)).animator.as);
				glow.display(e.getPlayer());
				Room r = getRoomFromWool(e.getPlayer());
				drawAftonOutline(r, e.getPlayer(), OUTLINE_GOLD);
				for(Room room : main.getRoomManager().cannotMoveFromList(r)) {
					drawAftonOutline(room, e.getPlayer(), OUTLINE_RED);
				}
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
			if(getPlayerMaterialInHand(e.getPlayer()).equals(Material.AIR)) {
				e.getRoom().hideGuardOutline(e.getPlayer());
			}else if(canMoveFromTo(getRoomFromWool(e.getPlayer()),e.getRoom())) {
				e.getRoom().hideGuardOutline(e.getPlayer());
			}
		}
	}
	@EventHandler
	public void onLookAtRoom(LookAtRoomEvent e) {
		if(e.getTeam()==Team.AFTON) {
			if(getPlayerMaterialInHand(e.getPlayer()).equals(Material.AIR)) {
				if(isCameraUnlocked(e.getRoom())) {
					drawAftonOutline(e.getRoom(), e.getPlayer(), OUTLINE_GREEN);
				}else {
					drawAftonOutline(e.getRoom(), e.getPlayer(), OUTLINE_RED);
				}
			}else if(canMoveFromTo(getRoomFromWool(e.getPlayer()),e.getRoom())) {
				drawAftonOutline(e.getRoom(), e.getPlayer(), OUTLINE_GREEN);
			}
		}
	}
	@EventHandler
	public void onPlayerActionRoom(ActionOnRoomEvent e) {
		if(e.getTeam()==Team.AFTON) {
			Material inHand = getPlayerMaterialInHand(e.getPlayer());
			if(inHand.equals(Material.AIR)) {
				if(isCameraUnlocked(e.getRoom())) {
					Bukkit.getPluginManager().registerEvents(new Camera(main, e.getRoom().getCamLocation(), e.getPlayer(), Team.AFTON), main);
				}else {
					//message de refus
				}
			}else if(canMoveFromTo(getRoomFromWool(e.getPlayer()),e.getRoom())) {
				Animatronic anim = Animatronic.getFromWool(inHand);
				List<Door> canMoveUsing = main.getRoomManager().canMoveFromToDoorList(main.getRoomManager().getRoom(main.teamafton.getAnimatronic(anim).currentRoom), e.getRoom());
				int chosenDoor = (int)Math.random()*canMoveUsing.size();
				Bukkit.getPluginManager().callEvent(new AnimatronicMoveAtDoorEvent(canMoveUsing.get(chosenDoor), anim, e.getRoom()));
			}else {
				//message de refus
			}
		}
	}
	@EventHandler
	public void onGameEnd(GameStopEvent e) {
		bonnie=null;
		freddy=null;
		chica=null;
		foxy=null;
		mangle=null;
		springtrap=null;
	}
	public MinimapAnimatronic getAnimatronic(Animatronic anim) {
		switch(anim) {
		case BONNIE: return bonnie;
		case FREDDY:return freddy;
		case CHICA: return chica;
		case FOXY: return foxy;
		case MANGLE: return mangle;
		case SPRINGTRAP: return springtrap;
		default: return null;
		}
	}
}
