package fr.nekotine.fnafy.doors;

import java.util.HashMap;
import java.util.List;

import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.Room;

public class DoorMinimap {
	private final Room room1;
	private final Room room2;
	private HashMap<Animatronic,List<ASAnimation>> minimapPosesToRoom1=new HashMap<Animatronic,List<ASAnimation>>();
	private HashMap<Animatronic,List<ASAnimation>> minimapPosesToRoom2=new HashMap<Animatronic,List<ASAnimation>>();

	public DoorMinimap(Room room1, Room room2,HashMap<Animatronic,List<ASAnimation>> _minimapPosesToRoom1, HashMap<Animatronic,List<ASAnimation>> _minimapPosesToRoom2) {
		this.room1=room1;
		this.room2=room2;
		minimapPosesToRoom1=_minimapPosesToRoom1;
		minimapPosesToRoom2=_minimapPosesToRoom2;
	}
	
	//FONCTIONS
	
	
	
	//GETTERS

	public Room getRoom2() {
		return room2;
	}

	public Room getRoom1() {
		return room1;
	}
}
