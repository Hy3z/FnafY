package fr.nekotine.fnafy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.doors.DoorType;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.RoomType;

public class YamlReader {
	private FnafYMain main;
	private File mapFolder;
	private final String roomConfigName = "roomConfig";
	private final String doorConfigName = "doorConfig";
	public YamlReader(FnafYMain _main) {
		main=_main;
		mapFolder = new File(main.getDataFolder(),"Maps");
		if (!mapFolder.exists()) {
			mapFolder.mkdirs();
			main.getLogger().info(ChatColor.GREEN+"Maps file created");
		 }
	}
	//--------------------------------------------------------------------------------------
	private YamlConfiguration getConfig(String mapName, String configName) {
		if(mapExist(mapName)) {
			File f = new File(mapFolder.getPath()+"/"+mapName,configName+".yml");
			if(f.exists()) {
				YamlConfiguration config = new YamlConfiguration();
				try {
					config.load(f);
					return config;
				}catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	private boolean saveConfig(String mapName, String configName, YamlConfiguration config) {
		File f = new File(mapFolder.getPath()+"/"+mapName,configName+".yml");
		try {
			config.save(f);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	private YamlConfiguration getRoomConfig(String mapName) {
		return getConfig(mapName,roomConfigName);
	}
	private YamlConfiguration getDoorConfig(String mapName) {
		return getConfig(mapName,doorConfigName);
	}
	private boolean saveDoorConfig(String mapName, YamlConfiguration config) {
		return saveConfig(mapName, doorConfigName, config);
	}
	private boolean saveRoomConfig(String mapName, YamlConfiguration config) {
		return saveConfig(mapName, roomConfigName, config);
	}
	public boolean configFilesExists(String mapName) {
		File room = new File(mapFolder.getPath()+"/"+mapName,roomConfigName+".yml");
		File door = new File(mapFolder.getPath()+"/"+mapName,doorConfigName+".yml");
		if(room.exists() && door.exists()) {
			return true;
		}
		return false;
	}
	//--------------------------------------------------------------------------------------
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
	public boolean mapExist(String mapName) {
		return new File(mapFolder,mapName).exists();
	}
	public String[] getMapList() {
		return mapFolder.list();//
	}
	//--------------------------------------------------------------------------------------
	public boolean doorExist(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			return getDoorList(mapName).contains(doorName);
		}
		return false;
	}
	public boolean addDoor(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(!doorExist(mapName, doorName)) {
				doorConfig.set(doorName+".doorType", "UNKNOWN");
				doorConfig.set(doorName+".doorLoc", "");
				doorConfig.set(doorName+".length", "");
				doorConfig.set(doorName+".room1Name", "");
				doorConfig.set(doorName+".room2Name", "");
				for (Animatronic anim : Animatronic.values()){
					doorConfig.set(doorName+".animPose.room1."+anim.toString(), "");
					doorConfig.set(doorName+".animPose.room2."+anim.toString(), "");
				}
				saveDoorConfig(mapName,doorConfig);
				return true;
			}
		}
		return false;
	}
	public boolean removeDoor(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				doorConfig.set(doorName, null);
				saveDoorConfig(mapName,doorConfig);
				return true;
			}
		}
		return false;
	}
	public DoorType getDoorType(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				return DoorType.fromString(doorConfig.getString(doorName+".doorType"));
				}
			}
		return DoorType.UNKNOWN;
	}
	public boolean setDoorType(String mapName, String doorName, DoorType doorType) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				doorConfig.set(doorName+".doorType", doorType.toString());
				saveDoorConfig(mapName,doorConfig);
				return true;
			}
		}
		return false;
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
	public String getLinkedRoomName(String mapName, String doorName, int doorNum) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				return doorConfig.getString(doorName+".room"+doorNum+"Name");
			}
		}
		return "";
	}
	private boolean setDoorLocation(String mapName, String doorName, Location loc) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				doorConfig.set(doorName+".doorLoc", loc);
				saveDoorConfig(mapName, doorConfig);
				return true;
			}
		}
		return false;
	}
	public Location getDoorLocation(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				return doorConfig.getLocation(doorName+".doorLoc");
			}
		}
		return null;
	}
	public Vector getDoorLength(String mapName, String doorName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				return doorConfig.getVector(doorName+".length");
			}
		}
		return null;
	}
	public boolean setDoorLocationAndLength(String mapName, String doorName, Location doorLoc) {
		if(doorLoc!=null) {
			if(doorExist(mapName, doorName)) {
				if(((Bisected)doorLoc.getBlock().getBlockData()).getHalf().equals(Half.TOP)){
					doorLoc.add(0, -1, 0);
				}
				int minusX = +numberOfDoor(doorLoc.clone(), new Vector(-1, 0, 0));
				int minusY = +numberOfDoor(doorLoc.clone(), new Vector(0, -2, 0));
				int minusZ = +numberOfDoor(doorLoc.clone(), new Vector(0, 0, -1));
				int x = numberOfDoor(doorLoc.clone(), new Vector(1, 0, 0))+minusX;
				int y = numberOfDoor(doorLoc.clone(), new Vector(0, 2, 0))+minusY;
				int z = numberOfDoor(doorLoc.clone(), new Vector(0, 0, 1))+minusZ;
				setDoorLocation(mapName, doorName, doorLoc.add(-minusX, -minusY*2, -minusZ));
				setDoorLength(mapName, doorName, new Vector(x,y,z));
				return true;
			}
		}
		return false;
	}
	private boolean setDoorLength(String mapName, String doorName, Vector vec) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				doorConfig.set(doorName+".length", vec);
				saveDoorConfig(mapName, doorConfig);
				return true;
			}
		}
		return false;
	}
	private int numberOfDoor(Location doorLoc, Vector vec) {
		doorLoc.add(vec);
		Block b = doorLoc.getBlock();
		if(b.getType().toString().contains("DOOR")) {
			return 1+numberOfDoor(doorLoc,vec);
		}
		return 0;
	}
	//--------------------------------------------------------------------------------------
	public boolean addRoomAnimation(String mapName, String roomName, Animatronic anim, String animation) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				if(anim!=null) {
					if(main.getAnimManager().getAsanims().containsKey(animation)) {
						List<String> animationList = roomConfig.getStringList(roomName+".animPose."+anim.toString());
						if(!animationList.contains(animation)) {
							animationList.add(animation);
							roomConfig.set(roomName+".animPose."+anim.toString(), animationList);
							saveRoomConfig(mapName, roomConfig);
							return true;
						}
					}
				}
			}
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
	public boolean addRoom(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(!roomExist(mapName,roomName)) {
				roomConfig.set(roomName+".roomType", "UNKNOWN");
				roomConfig.set(roomName+".camLoc", "");
				for (Animatronic anim : Animatronic.values()){
					roomConfig.set(roomName+".animPose."+anim.toString(), "");
				}
				saveRoomConfig(mapName,roomConfig);
				return true;
			}
		}
		return false;
	}
	public boolean removeRoom(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				roomConfig.set(roomName, null);
				saveRoomConfig(mapName,roomConfig);
				return true;
			}
		}
		return false;
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
				return RoomType.fromString(roomConfig.getString(roomName+".roomType"));
				}
			}
		return RoomType.UNKNOWN;
	}
	public boolean setRoomType(String mapName, String roomName, RoomType roomType) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				roomConfig.set(roomName+".roomType", roomType.toString());
				saveRoomConfig(mapName,roomConfig);
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
	public HashMap<Animatronic,ASAnimOrder> getInRoomPose(String mapName, String roomName){
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				HashMap<Animatronic,ASAnimOrder> InRoomPoses = new HashMap<Animatronic,ASAnimOrder>();
				for(Animatronic anim : Animatronic.values()) {
					InRoomPoses.put(anim, (ASAnimOrder)roomConfig.get(roomName+".animPose."+anim.toString()));
				}
			}	
		}
		return null;
	}
}
