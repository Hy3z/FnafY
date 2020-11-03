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
import org.bukkit.util.EulerAngle;

import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.enums.RoomType;
import fr.nekotine.fnafy.objets.Position;

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
	public WeakHashMap<Animatronic, ArrayList<Position>> getPositions(String roomName){
		if (roomConfig != null) {
			WeakHashMap<Animatronic, ArrayList<Position>> positions = new WeakHashMap<>();
			for(String animatronic : roomConfig.getConfigurationSection(roomName+".animPose").getKeys(false)) {
				ArrayList<Position> poseList = new ArrayList<>();
				for(String nbPose : roomConfig.getConfigurationSection(roomName+".animPose."+animatronic).getKeys(false)) {
					Location posLoc =  roomConfig.getLocation(roomName+".animPose."+animatronic+"."+nbPose+".location");
					List<Float> posRot =  roomConfig.getFloatList(roomName+".animPose."+animatronic+"."+nbPose+".rotation");
					List<EulerAngle> posAngle = getAngleList(roomName, animatronic, nbPose, poseTokens);
					poseList.add(new Position(posLoc, posRot, posAngle));
				}
				positions.put(Animatronic.valueOf(animatronic), poseList);
			}
			return positions;
		}
		return null;
	}
	public Set<String> getRoomList(){
		if (roomConfig != null) {
			return roomConfig.getKeys(false);
		}
		return null;
	}
	private List<EulerAngle> getAngleList(String roomName, String animatronic, String nbPose, String[] tokens){
		List<EulerAngle> posAngle = new ArrayList<>();
		for(String token : tokens) {
			List<Double> pose = roomConfig.getDoubleList(roomName+".animPose."+animatronic+"."+nbPose+"."+token);
			posAngle.add(new EulerAngle(pose.get(0), pose.get(1), pose.get(2)));
		}
		return posAngle;
	}
}
