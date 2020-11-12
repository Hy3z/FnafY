package fr.nekotine.fnafy.doors;

import java.util.HashMap;

import org.bukkit.Location;

import fr.nekotine.fnafy.animation.ASOrderMP;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.Room;

public class Door {
	
	//VARS
	private final Location AftonBlock;
	private final Room room1;
	private final Room room2;
	private final HashMap<Animatronic,ASOrderMP> InRoomPoses=new HashMap<Animatronic,ASOrderMP>();
	private final HashMap<Animatronic,ASOrderMP> MinimapPoses=new HashMap<Animatronic,ASOrderMP>();
	
	//CONSTRUCTEURS
	
	public Door(Room room1, Room room2, Location displayBlock,HashMap<Animatronic,ASOrderMP> rmpses,HashMap<Animatronic,ASOrderMP> aftses) {
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

	public HashMap<Animatronic,ASOrderMP> getInRoomPoses() {
		return InRoomPoses;
	}

	public HashMap<Animatronic,ASOrderMP> getMinimapPoses() {
		return MinimapPoses;
	}
	
}
