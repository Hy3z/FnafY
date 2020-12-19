package fr.nekotine.fnafy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.doors.Door;
import fr.nekotine.fnafy.doors.DoorType;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.Room;
import fr.nekotine.fnafy.room.RoomType;
import fr.nekotine.fnafy.utils.BlockSelection;

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
				doorConfig.set(doorName+".doorType", DoorType.UNKNOWN.toString());
				doorConfig.set(doorName+".doorLoc", "");
				doorConfig.set(doorName+".length", "");
				doorConfig.set(doorName+".room1Name", "");
				doorConfig.set(doorName+".room2Name", "");
				for (Animatronic anim : Animatronic.values()){
					doorConfig.set(doorName+".animPose.room1."+anim.toString(), new String[0]);
					doorConfig.set(doorName+".animPose.room2."+anim.toString(), new String[0]);
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
	public List<String> getLinkedRoomsNames(String mapName, String doorName){
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				List<String> roomsNames = new ArrayList<>();
				roomsNames.add(doorConfig.getString(doorName+".room1Name"));
				roomsNames.add(doorConfig.getString(doorName+".room2Name"));
				return roomsNames;
			}
		}
		return null;
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
	private int getRoomNumberFromName(String mapName, String doorName, String roomName) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				if(roomExist(mapName, roomName)) {
					if(getLinkedRoomsNames(mapName, doorName).contains(roomName)) {
						if(getLinkedRoomName(mapName, doorName, 1).equals(roomName)) {
							return 1;
						}
						return 2;
					}
				}
			}
		}
		return 0;
	}
	public boolean addDoorAnimation(String mapName, String doorName, String roomName, Animatronic anim, String animation) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				if(roomExist(mapName, roomName)) {
					if(getLinkedRoomsNames(mapName, doorName).contains(roomName)) {
						if(anim!=null) {
							if(main.getAnimManager().getAsanims().containsKey(animation)) {
								List<String> animationList = getDoorRoomAnimation(mapName, doorName, roomName, anim);
								if(!animationList.contains(animation)) {
									animationList.add(animation);
									doorConfig.set(doorName+".animPose.room"+getRoomNumberFromName(mapName, doorName, roomName)+"."+anim.toString(), animationList);
									saveDoorConfig(mapName, doorConfig);
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	public boolean removeDoorAnimation(String mapName, String doorName, String roomName, Animatronic anim, String animation) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				if(roomExist(mapName, roomName)) {
					if(getLinkedRoomsNames(mapName, doorName).contains(roomName)) {
						if(anim!=null) {
							if(main.getAnimManager().getAsanims().containsKey(animation)) {
								List<String> animationList = getDoorRoomAnimation(mapName, doorName, roomName, anim);
								if(animationList.contains(animation)) {
									animationList.remove(animation);
									doorConfig.set(doorName+".animPose.room"+getRoomNumberFromName(mapName, doorName, roomName)+"."+anim.toString(), animationList);
									saveDoorConfig(mapName, doorConfig);
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	public List<String> getDoorRoomAnimation(String mapName, String doorName, String roomName, Animatronic anim){
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				if(roomExist(mapName, roomName)) {
					int roomNum = getRoomNumberFromName(mapName, doorName, roomName);
					if(roomNum!=0) {
						return doorConfig.getStringList(doorName+".animPose.room"+roomNum+"."+anim.toString());
					}
				}
			}
		}
		return null;
	}
	public List<Door> getDoorObjectList(){
		List<Door> doorList = new ArrayList<>();
		/*if(mapExist(main.getMapName())&&configFilesExists(main.getMapName())) {
			for(String doorName : getDoorList(main.getMapName())) {
				DoorType type = getDoorType(main.getMapName(), doorName);
				Location doorLoc = getDoorLocation(main.getMapName(), doorName);
				Vector length = getDoorLength(main.getMapName(), doorName);
				Room room1 = main.getRoomsHashMap().get(getLinkedRoomName(main.getMapName(), doorName, 1));
				Room room2 = main.getRoomsHashMap().get(getLinkedRoomName(main.getMapName(), doorName, 2));
				
				HashMap<Animatronic,List<ASAnimation>> animToRoom1 =new HashMap<Animatronic,List<ASAnimation>>();
				HashMap<Animatronic,List<ASAnimation>> animToRoom2 =new HashMap<Animatronic,List<ASAnimation>>();
				for(Animatronic animatronic : Animatronic.values()) {
					List<ASAnimation> tempList = new ArrayList<>();
					List<String> temp = getDoorRoomAnimation(main.getMapName(), doorName, getLinkedRoomName(main.getMapName(), doorName, 1), animatronic);
					if(temp.isEmpty()) {
						break;
					}
					for(String animation : temp) {
						tempList.add(main.getAnimManager().getAsanims().get(animation));
					}
					animToRoom1.put(animatronic, tempList);
					
					tempList.clear();
					temp = getDoorRoomAnimation(main.getMapName(), doorName, getLinkedRoomName(main.getMapName(), doorName, 2), animatronic);
					if(temp.isEmpty()) {
						break;
					}
					for(String animation : temp) {
						tempList.add(main.getAnimManager().getAsanims().get(animation));
					}
					animToRoom2.put(animatronic, tempList);
				}
				if(type!=DoorType.UNKNOWN && doorLoc!=null && length!=null && room1!=null && room2!=null
						&& animToRoom1.keySet().containsAll(Arrays.asList(Animatronic.values()))
						&& animToRoom2.keySet().containsAll(Arrays.asList(Animatronic.values()))){
					doorList.add(new Door(doorName, type, doorLoc, length, room1, room2, animToRoom1, animToRoom2));
				}
			}
		}*/
		return doorList;
	}
	//--------------------------------------------------------------------------------------
	public boolean addRoomAnimation(String mapName, String roomName, Animatronic anim, String animation) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				if(anim!=null) {
					if(main.getAnimManager().getAsanims().containsKey(animation)) {
						List<String> animationList = getRoomAnimation(mapName, roomName, anim);
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
	public boolean removeRoomAnimation(String mapName, String roomName, Animatronic anim, String animation) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				if(anim!=null) {
					if(main.getAnimManager().getAsanims().containsKey(animation)) {
						List<String> animationList = getRoomAnimation(mapName, roomName, anim);
						if(animationList.contains(animation)) {
							animationList.remove(animation);
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
	public List<String> getRoomAnimation(String mapName, String roomName, Animatronic anim){
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				if(anim!=null) {
					return roomConfig.getStringList(roomName+".animPose."+anim.toString());
				}
			}
		}
		return null;
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
				roomConfig.set(roomName+".roomType", RoomType.UNKNOWN.toString());
				roomConfig.set(roomName+".camLoc", "");
				for (Animatronic anim : Animatronic.values()){
					roomConfig.set(roomName+".animPose."+anim.toString(), new String[0]);
				}
				roomConfig.set(roomName+".aftonSurface", "");
				roomConfig.set(roomName+".aftonOutline", "");
				roomConfig.set(roomName+".guardOutline", "");
				roomConfig.set(roomName+".guardSurface", "");
				for (Animatronic anim : Animatronic.values()){
					roomConfig.set(roomName+".minimapPose."+anim.toString(), new String[0]);
				}
				saveRoomConfig(mapName,roomConfig);
				return true;
			}
		}
		return false;
	}
	public boolean setBlockSelection(String mapName, String roomName, String outline, BlockSelection bs) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				roomConfig.set(roomName+"."+outline, bs);
				saveRoomConfig(mapName, roomConfig);
				return true;
			}
		}
		return false;
	}
	public BlockSelection getAftonSurface(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				return (BlockSelection)roomConfig.get(roomName+".aftonSurface");
			}
		}
		return null;
	}
	public BlockSelection getAftonOutline(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				return (BlockSelection)roomConfig.get(roomName+".aftonOutline");
			}
		}
		return null;
	}
	public BlockSelection getGuardOutline(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				return (BlockSelection)roomConfig.get(roomName+".guardOutline");
			}
		}
		return null;
	}
	public BlockSelection getGuardSurface(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				return (BlockSelection)roomConfig.get(roomName+".guardSurface");
			}
		}
		return null;
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
	public HashMap<String, Room> getRoomObjectsHash() {
		HashMap<String, Room> rooms = new HashMap<String, Room>();
		if(mapExist(main.getMapName())&&configFilesExists(main.getMapName())) {
			for(String roomName : getRoomList(main.getMapName())) {
				RoomType type = getRoomType(main.getMapName(), roomName);
				Location camLoc = getCameraLocation(main.getMapName(), roomName);
				HashMap<Animatronic,List<ASAnimation>> inRoomAnimation =new HashMap<Animatronic,List<ASAnimation>>();
				for(Animatronic animatronic : Animatronic.values()) {
					List<ASAnimation> tempList = new ArrayList<>();
					List<String> temp = getRoomAnimation(main.getMapName(), roomName, animatronic);
					if(temp.isEmpty()) {
						break;
					}
					for(String animation : temp) {
						tempList.add(main.getAnimManager().getAsanims().get(animation));
					}
					inRoomAnimation.put(animatronic, tempList);
				}
				if(type!=RoomType.UNKNOWN && camLoc!=null && inRoomAnimation.keySet().containsAll(Arrays.asList(Animatronic.values()))){
					//rooms.put(roomName, new Room(roomName, type, camLoc, inRoomAnimation));
				}
			}
		}
		return rooms;
	}
}
