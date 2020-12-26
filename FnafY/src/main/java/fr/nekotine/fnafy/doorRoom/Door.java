package fr.nekotine.fnafy.doorRoom;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.animatronic.Animatronic;
import fr.nekotine.fnafy.events.GameStopEvent;
import fr.nekotine.fnafy.events.PlayerMoveToRoomEvent;
import fr.nekotine.fnafy.team.GuardWrapper;

public class Door implements Listener{
	private final FnafYMain main;
	private final String doorName;
	private final DoorType doorType;
	private final Location doorLoc;
	private final Vector length;
	private final Room room1;
	private final Room room2;
	private final HashMap<Animatronic,List<ASAnimation>> animToRoom1=new HashMap<Animatronic,List<ASAnimation>>();
	private final HashMap<Animatronic,List<ASAnimation>> animToRoom2=new HashMap<Animatronic,List<ASAnimation>>();
	
	private final HashMap<Animatronic,List<ASAnimation>> minimapToRoom1=new HashMap<Animatronic,List<ASAnimation>>();
	private final HashMap<Animatronic,List<ASAnimation>> minimapToRoom2=new HashMap<Animatronic,List<ASAnimation>>();
	
	public boolean isClosed = false;
	public Door(FnafYMain main, String doorName, DoorType doorType, Location doorLoc, Vector length, Room room1, Room room2,
			HashMap<Animatronic,List<ASAnimation>> animToRoom1, HashMap<Animatronic,List<ASAnimation>> animToRoom2,
			HashMap<Animatronic,List<ASAnimation>> minimapToRoom1, HashMap<Animatronic,List<ASAnimation>> minimapToRoom2) {
		this.main=main;
		this.doorName=doorName;
		this.doorType=doorType;
		this.doorLoc=doorLoc;
		this.length=length;
		this.room1=room1;
		this.room2=room2;
		this.animToRoom1.putAll(animToRoom1);
		this.animToRoom2.putAll(animToRoom2);
		this.minimapToRoom1.putAll(minimapToRoom1);
		this.minimapToRoom2.putAll(minimapToRoom2);
		
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onGameStop(GameStopEvent e) {
		HandlerList.unregisterAll(this);
	}
	@EventHandler
	public void playerAction(PlayerInteractEvent e) {
		if(!isClosed) {
			if(e.getAction()==Action.LEFT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_BLOCK) {
				GuardWrapper pwrapper = main.teamguard.getWrapper(e.getPlayer().getUniqueId());
				if(pwrapper!=null){
					Block b = e.getClickedBlock();
					if(b.getX()>=doorLoc.getX() && b.getX()<=doorLoc.getX()+length.getX()) {
						if(b.getY()>=doorLoc.getY() && b.getY()<=doorLoc.getY()+length.getY()) {
							if(b.getZ()>=doorLoc.getZ() && b.getZ()<=doorLoc.getZ()+length.getZ()) {
								if(canMoveTo(pwrapper.currentRoom).canGuardEnterRoom) {
									Bukkit.getPluginManager().callEvent(new PlayerMoveToRoomEvent(pwrapper.currentRoom, e.getPlayer(), canMoveTo(pwrapper.currentRoom)));
								}else {
									//message "Access denied!"
								}
							}
						}
					}
				}
			}
		}else {
			//message "La porte semble être fermée par un mécanisme coupe-feux!"
		}
	}
	public String getDoorName() {
		return doorName;
	}

	public DoorType getDoorType() {
		return doorType;
	}

	public Location getDoorLoc() {
		return doorLoc;
	}

	public Vector getLength() {
		return length;
	}

	public Room getRoom1() {
		return room1;
	}

	public Room getRoom2() {
		return room2;
	}
	public List<ASAnimation> getDoorAnimationsToRoom(Room r, Animatronic anim){
		if(r.getRoomName()==room1.getRoomName()) {
			return animToRoom1.get(anim);
		}else {
			return animToRoom2.get(anim);
		}
	}
	public List<ASAnimation> getMinimapDoorAnimationsToRoom(Room r, Animatronic anim){
		if(r.getRoomName()==room1.getRoomName()) {
			return minimapToRoom1.get(anim);
		}else {
			return minimapToRoom2.get(anim);
		}
	}
	public boolean canMoveFromToBoolean(Room previous, Room next) {
		if(room1.getRoomName()==previous.getRoomName() && room2.getRoomName()==next.getRoomName()) {
			return true;
		}
		if(room2.getRoomName()==previous.getRoomName() && room1.getRoomName()==next.getRoomName()) {
			return true;
		}
		return false;
	}
	public Room canMoveTo(Room from) {
		if(room1.getRoomName()==from.getRoomName()) {
			return room2;
		}
		if(room2.getRoomName()==from.getRoomName()) {
			return room1;
		}
		return null;
	}
}
