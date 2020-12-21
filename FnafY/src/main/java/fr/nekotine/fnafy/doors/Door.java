package fr.nekotine.fnafy.doors;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.Room;

public class Door {
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
	public Door(String doorName, DoorType doorType, Location doorLoc, Vector length, Room room1, Room room2,
			HashMap<Animatronic,List<ASAnimation>> animToRoom1, HashMap<Animatronic,List<ASAnimation>> animToRoom2,
			HashMap<Animatronic,List<ASAnimation>> minimapToRoom1, HashMap<Animatronic,List<ASAnimation>> minimapToRoom2) {
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
	
	public boolean canMoveFromTo(Room previous, Room next) {
		if(room1.getRoomName()==previous.getRoomName() && room2.getRoomName()==next.getRoomName()) {
			return true;
		}
		if(room2.getRoomName()==previous.getRoomName() && room1.getRoomName()==next.getRoomName()) {
			return true;
		}
		return false;
	}
}
