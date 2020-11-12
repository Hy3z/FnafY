package fr.nekotine.fnafy.doors;

import java.util.HashMap;

import org.bukkit.Location;

import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.Room;

public class Door {
	
	//VARS
	private final Location AftonBlock;
	private final Room room1;
	private final Room room2;
	private final HashMap<Animatronic,ASAnimOrder> InRoomPoses=new HashMap<Animatronic,ASAnimOrder>();
	private final HashMap<Animatronic,ASAnimOrder> MinimapPoses=new HashMap<Animatronic,ASAnimOrder>();
	
	//CONSTRUCTEURS
	
	public Door(Room room1, Room room2, Location displayBlock,HashMap<Animatronic,ASAnimOrder> rmpses,HashMap<Animatronic,ASAnimOrder> aftses) {
		this.room1=room1;
		this.room2=room2;
		AftonBlock=displayBlock;
		InRoomPoses.putAll(rmpses);
		MinimapPoses.putAll(aftses);
		
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

	public HashMap<Animatronic,ASAnimOrder> getInRoomPoses() {
		return InRoomPoses;
	}

	public HashMap<Animatronic,ASAnimOrder> getMinimapPoses() {
		return MinimapPoses;
	}
	
}
