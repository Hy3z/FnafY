package fr.nekotine.fnafy;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Location;

import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.enums.RoomType;
import fr.nekotine.fnafy.objets.Position;

public class Room {
	Location camLocation;
	List<Float> camRotation;
	RoomType roomType;
	WeakHashMap<Animatronic, ArrayList<Position>> positions;
	public Room(YamlReader ymlReader, String roomName) {
		camLocation = ymlReader.getCameraLocation(roomName);
		camRotation = ymlReader.getCameraRotation(roomName);
		roomType = ymlReader.getRoomType(roomName);
		positions = ymlReader.getPositions(roomName);
	}
}
