package fr.nekotine.fnafy;

import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Location;

import fr.nekotine.fnafy.enums.RoomType;

public class Room {
	Location camLocation;
	List<Float> camRotation;
	RoomType roomType;
	/*WeakHashMap<Animatronique, ArrayList<Position>> positionByAnim;*/
	public Room(YamlReader ymlReader, String roomName) {
		camLocation = ymlReader.getCameraLocation(roomName);
		camRotation = ymlReader.getCameraRotation(roomName);
	}
}
