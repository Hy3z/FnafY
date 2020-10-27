package fr.nekotine.fnafy;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

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
	public Set<String> getRoomList(){
		if (roomConfig != null) {
			return roomConfig.getKeys(false);
		}
		return null;
	}
}
