package fr.nekotine.fnafy.room;

import org.bukkit.Location;

import fr.nekotine.fnafy.enums.RoomType;

public class Room {
	private Location camLocation;
	private RoomType roomType;
	public Room(String roomName ,RoomType type) {
		
	}
	public Location getCameraLocation() {
		return camLocation;
	}
	public RoomType getType() {
		return roomType;
	}
	
}
