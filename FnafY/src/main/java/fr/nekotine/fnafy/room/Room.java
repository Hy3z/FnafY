package fr.nekotine.fnafy.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;

import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.doors.Door;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.utils.BlockSelection;

public class Room {
	
	//VARS
	private final Location camLocation;
	private final RoomType roomType;
	private final BlockSelection AftonSurface;
	private final BlockSelection AftonOutline;
	private final BlockSelection MinimapOutline;
	private final BlockSelection MinimapSurface;
	private final String displayName;
	private final HashMap<Animatronic,List<ASAnimation>> InRoomPoses=new HashMap<Animatronic,List<ASAnimation>>();
	private final HashMap<Animatronic,List<ASAnimation>> MinimapPoses=new HashMap<Animatronic,List<ASAnimation>>();
	private final ArrayList<Door> doors = new ArrayList<Door>();
	
	//CONSTRUCTEURS
	
	public Room(String roomName ,RoomType type,BlockSelection aftonsurf,BlockSelection aftonoutl,BlockSelection minmsurf,BlockSelection minmoutl,Location camloc,HashMap<Animatronic,List<ASAnimation>> rmpses,HashMap<Animatronic,List<ASAnimation>> aftses) {
		displayName = roomName;
		roomType = type;
		AftonSurface = aftonsurf;
		AftonOutline = aftonoutl;
		MinimapSurface = minmsurf;
		MinimapOutline = minmoutl;
		camLocation = camloc;
		InRoomPoses.putAll(rmpses);
		MinimapPoses.putAll(aftses);
	}
	
	//FONCTIONS
	
	public Set<Animatronic> getAllowedAnimatronics(){
		return InRoomPoses.keySet();
	}
	
	//GETTERS

	public HashMap<Animatronic,List<ASAnimation>> getMinimapPoses() {
		return MinimapPoses;
	}

	public HashMap<Animatronic,List<ASAnimation>> getInRoomPoses() {
		return InRoomPoses;
	}

	public String getDisplayName() {
		return displayName;
	}

	public BlockSelection getMinimapSurface() {
		return MinimapSurface;
	}

	public BlockSelection getMinimapOutline() {
		return MinimapOutline;
	}

	public BlockSelection getAftonOutline() {
		return AftonOutline;
	}

	public BlockSelection getAftonSurface() {
		return AftonSurface;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public Location getCamLocation() {
		return camLocation;
	}

	public ArrayList<Door> getDoorsList() {
		return doors;
	}
}
