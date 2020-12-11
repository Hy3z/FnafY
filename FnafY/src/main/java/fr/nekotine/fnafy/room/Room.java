package fr.nekotine.fnafy.room;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.enums.Animatronic;

public class Room {
	private final String roomName;
	private final RoomType roomType;
	private final Location camLocation;
	private final HashMap<Animatronic,List<ASAnimation>> InRoomAnimations=new HashMap<Animatronic,List<ASAnimation>>();
	//private final ArrayList<Door> doors = new ArrayList<Door>();
	
	public Room(String roomName, RoomType roomType, Location camLocation, HashMap<Animatronic,List<ASAnimation>> InRoomAnimations) {
		this.roomName = roomName;
		this.roomType = roomType;
		this.camLocation = camLocation;
		InRoomAnimations.putAll(InRoomAnimations);
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
}
