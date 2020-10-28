package fr.nekotine.fnafy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.enums.RoomType;
import fr.nekotine.fnafy.objets.Position;

public class YamlReader {
	String PATH_TO_DOOR = "doorConfig.yml";
	String PATH_TO_ROOM = "roomConfig.yml";
	YamlConfiguration doorConfig;
	YamlConfiguration roomConfig;
	public YamlReader() {
		doorConfig = getConfig(PATH_TO_DOOR);
		roomConfig = getConfig(PATH_TO_ROOM);
	}
	private YamlConfiguration getConfig(String yamlPath) {
		File yamlFile = new File(getClass().getClassLoader().getResource(yamlPath).getFile());
		if(yamlFile.exists()) {
			YamlConfiguration yamlConfig = new YamlConfiguration();
			try {
				yamlConfig.load(yamlFile);
				return yamlConfig;
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public Location getCameraLocation(String roomName) {
		if (roomConfig != null) {
			return roomConfig.getLocation(roomName+".camera.location");
		}
		return null;
	}
	public List<Float> getCameraRotation(String roomName) {
		if (roomConfig != null) {
			return roomConfig.getFloatList(roomName+".camera.rotation");
		}
		return null;
	}
	public RoomType getRoomType(String roomName) {
		if (roomConfig != null) {
			return RoomType.valueOf(roomConfig.getString(roomName+".roomType"));
		}
		return null;
	}
	public WeakHashMap<Animatronic, ArrayList<Position>> getPositions(String roomName){
		if (roomConfig != null) {
			WeakHashMap<Animatronic, ArrayList<Position>> positions = new WeakHashMap<>();
			for (String animatronic : roomConfig.getStringList(roomName+".animPose")) {
				/*positions.put(Animatronic.valueOf(animatronic),);*/
			}
			
		}
		return null;
	}
	public Set<String> getRoomList(){
		if (roomConfig != null) {
			return roomConfig.getKeys(false);
		}
		return null;
	}
}
