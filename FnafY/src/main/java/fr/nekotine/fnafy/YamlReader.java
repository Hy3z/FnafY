package fr.nekotine.fnafy;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.nekotine.fnafy.room.RoomType;

public class YamlReader {
	String PATH_TO_DOOR = "doorConfig.yml";
	String PATH_TO_ROOM = "roomConfig.yml";
	YamlConfiguration doorConfig;
	YamlConfiguration roomConfig;
	String[] poseTokens = {"head","leftArm","rightArm","body","leftLeg","rightLeg"};
	public YamlReader() {
		doorConfig = getConfig(PATH_TO_DOOR);
		roomConfig = getConfig(PATH_TO_ROOM);
	}
	public static String[] getMapList() {
		return null;
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
	public RoomType getRoomType(String roomName) {
		if (roomConfig != null) {
			return RoomType.valueOf(roomConfig.getString(roomName+".roomType"));
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
