package fr.nekotine.fnafy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.nekotine.fnafy.doors.DoorType;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.RoomType;
import fr.nekotine.fnafy.utils.Posture;

public class YamlReader {
	private FnafYMain main;
	private File mapFolder;
	private final String roomConfigName = "roomConfig";
	private final String doorConfigName = "doorConfig";
	private String[] animList= {"Bonnie", "Freddy", "Chica", "Foxy", "Mangle", "Springtrap"};
	public YamlReader(FnafYMain _main) {
		main=_main;
		mapFolder = new File(main.getDataFolder(),"Maps");
		if (!mapFolder.exists()) {
			mapFolder.mkdirs();
			main.getLogger().info(ChatColor.GREEN+"Maps file created");
		 }
	}
	public boolean createMap(String mapName) {
		File mapConfigFolder = new File(mapFolder, mapName);
		if (!mapConfigFolder.exists()) {
			mapConfigFolder.mkdirs();
			try {
				new File(mapConfigFolder,"roomConfig.yml").createNewFile();
				new File(mapConfigFolder,"doorConfig.yml").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			main.getLogger().info(ChatColor.GREEN+"Map: ["+mapName+"] folder created");
			return true;
		 }
		return false;
	}
	public boolean deleteMap(String mapName) {
		File mapConfigFolder = new File(mapFolder, mapName);
		for(File s : mapConfigFolder.listFiles()) {
			s.delete();
		}
		main.getLogger().info(ChatColor.RED+"Map: ["+mapName+"] folder have been deleted! (only if it existed!)");
		return mapConfigFolder.delete();
	}
	public YamlConfiguration getConfig(String mapName, String configName) {
		if(mapExist(mapName)) {
			File f = new File(mapFolder.getPath()+"/"+mapName,configName+".yml");
			if(f.exists()) {
				YamlConfiguration config = new YamlConfiguration();
				try {
					config.load(f);
					return config;
				}catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}
	public boolean mapExist(String mapName) {
		return new File(mapFolder,mapName).exists();
	}
	public boolean doorExist(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			return getDoorList(mapName).contains(doorName);
		}
		return false;
	}
	public boolean roomExist(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			return getRoomList(mapName).contains(roomName);
		}
		return false;
	}
	public boolean saveConfig(String mapName, String configName, YamlConfiguration config) {
		File f = new File(mapFolder.getPath()+"/"+mapName,configName+".yml");
		try {
			config.save(f);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean saveDoorConfig(String mapName, YamlConfiguration config) {
		return saveConfig(mapName, doorConfigName, config);
	}
	public boolean saveRoomConfig(String mapName, YamlConfiguration config) {
		return saveConfig(mapName, roomConfigName, config);
	}
	public boolean addDoor(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(!doorExist(mapName, doorName)) {
				doorConfig.set(doorName+".doorType", "");
				doorConfig.set(doorName+".doorLoc", "");
				doorConfig.set(doorName+".length.x", "");
				doorConfig.set(doorName+".length.y", "");
				doorConfig.set(doorName+".length.z", "");
				doorConfig.set(doorName+".room1Name", "");
				doorConfig.set(doorName+".room2Name", "");
				for (String anim : animList){
					doorConfig.set(doorName+".animPose.room1."+anim, "");
					doorConfig.set(doorName+".animPose.room2."+anim, "");
				}
				saveDoorConfig(mapName,doorConfig);
				return true;
			}
		}
		return false;
	}
	public boolean addRoom(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(!roomExist(mapName,roomName)) {
				roomConfig.set(roomName+".roomType", "");
				roomConfig.set(roomName+".camLoc", "");
				for (String anim : animList){
					roomConfig.set(roomName+".animPose."+anim, "");
				}
				saveRoomConfig(mapName,roomConfig);
				return true;
			}
		}
		return false;
	}
	public YamlConfiguration getRoomConfig(String mapName) {
		return getConfig(mapName,roomConfigName);
	}
	public YamlConfiguration getDoorConfig(String mapName) {
		return getConfig(mapName,doorConfigName);
	}
	public String[] getMapList() {
		return mapFolder.list();//
	}
	public Location getCameraLocation(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				return roomConfig.getLocation(roomName+".camLoc");
			}
		}
		return null;
	}
	public boolean setCameraLocation(String mapName, String roomName, Location loc) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				roomConfig.set(roomName+".camLoc", loc);
				saveRoomConfig(mapName,roomConfig);
				return true;
			}
		}
		return false;
	}
	public RoomType getRoomType(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				return RoomType.valueOf(roomConfig.getString(roomName+".roomType"));
			}
		}
		return null;
	}
	public DoorType getDoorType(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				return DoorType.valueOf(doorConfig.getString(doorName+".roomType"));
			}
		}
		return null;
	}
	public boolean setRoomType(String mapName, String roomName, RoomType roomType) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				roomConfig.set(roomName+".roomType", roomType);
				saveRoomConfig(mapName,roomConfig);
				return true;
			}
		}
		return false;
	}
	public boolean setDoorType(String mapName, String doorName, DoorType doorType) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				doorConfig.set(doorName+".doorType", doorType);
				saveDoorConfig(mapName,doorConfig);
				return true;
			}
		}
		return false;
	}
	public Set<String> getRoomList(String mapName){
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			return roomConfig.getKeys(false);
		}
		return null;
	}
	public Set<String> getDoorList(String mapName){
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			return doorConfig.getKeys(false);
		}
		return null;
	}
	public boolean linkRoomToDoor(String mapName, String doorName, String roomName, int doorNum) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				if(roomExist(mapName, roomName)) {
					if(doorNum==1 || doorNum==2) {
						doorConfig.set(doorName+".room"+doorNum+"Name", roomName);
						saveDoorConfig(mapName,doorConfig);
						return true;
					}
				}
			}
		}
		return false;
	}
	public ArrayList<Posture> getAnimatronicRoomPostures(Animatronic animatronic, String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				ArrayList<Posture> posturesList = new ArrayList<>();
				for(Object obj : roomConfig.getList(roomName+".animPose."+animatronic.toString())) {
					posturesList.add((Posture)obj);
				}
				return posturesList;
			}
		}
		return null;
	}
}
