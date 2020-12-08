package fr.nekotine.fnafy.doors;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.Room;

public class Door {
	
	//VARS
	private final Location AftonBlock;
	private final Room room1;
	private final Room room2;
	private final HashMap<Animatronic,List<ASAnimation>> goingToFirstRoom=new HashMap<Animatronic,List<ASAnimation>>();
	private final HashMap<Animatronic,List<ASAnimation>> goingToSecondRoom=new HashMap<Animatronic,List<ASAnimation>>();
	private final HashMap<Animatronic,List<ASAnimation>> MinimapPoses=new HashMap<Animatronic,List<ASAnimation>>();
	
	//CONSTRUCTEURS
	
	public Door(Room room1, Room room2, Location displayBlock,HashMap<Animatronic,List<ASAnimation>> firstdoor,HashMap<Animatronic,List<ASAnimation>> seconddoor,HashMap<Animatronic,List<ASAnimation>> aftses) {
		this.room1=room1;
		this.room2=room2;
		AftonBlock=displayBlock;
		goingToFirstRoom.putAll(firstdoor);
		goingToSecondRoom.putAll(seconddoor);
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

	public HashMap<Animatronic,List<ASAnimation>> getGoingToRoom1Animation() {
		return goingToFirstRoom;
	}
	public HashMap<Animatronic,List<ASAnimation>> getGoingToRoom2Animation() {
		return goingToSecondRoom;
	}
	public HashMap<Animatronic,List<ASAnimation>> getMinimapPoses() {
		return MinimapPoses;
	}
	
}
