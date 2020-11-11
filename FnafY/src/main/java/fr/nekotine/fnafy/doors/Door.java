package fr.nekotine.fnafy.doors;

import org.bukkit.Location;

import fr.nekotine.fnafy.room.Room;

public class Door {
	
	//VARS
	private final Location AftonBlock;
	private final Room room1;
	private final Room room2;
	
	//CONSTRUCTEURS
	
	public Door(Room room1, Room room2, Location displayBlock) {
		this.room1=room1;
		this.room2=room2;
		AftonBlock=displayBlock;
		
	}
	
	//FONCTIONS
	
	
	
	//GETTERS

	public Room getRoom2() {
		return room2;
	}

	public Room getRoom1() {
		return room1;
	}

	public Location getAftonBlock() {
		return AftonBlock;
	}
	
}
