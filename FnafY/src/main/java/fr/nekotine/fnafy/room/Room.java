package fr.nekotine.fnafy;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Location;

import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.enums.RoomType;
import fr.nekotine.fnafy.objets.Position;

public class Room {
	private Location camLocation;
	private List<Float> camRotation;
	private RoomType roomType;
	private WeakHashMap<Animatronic, ArrayList<Position>> positions;
	public Room(YamlReader ymlReader, String roomName) {
		camLocation = ymlReader.getCameraLocation(roomName);
		camRotation = ymlReader.getCameraRotation(roomName);
		roomType = ymlReader.getRoomType(roomName);
		positions = ymlReader.getPositions(roomName);
	}
	public Location getCameraLocation() {
		return camLocation;
	}
	public List<Float> getCameraRotation(){
		return camRotation;
	}
	public RoomType getType() {
		return roomType;
	}
	
}
