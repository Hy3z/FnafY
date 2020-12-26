package fr.nekotine.fnafy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import animatronic.Animatronic;
import doorRoom.Door;
import doorRoom.DoorType;
import doorRoom.Room;
import doorRoom.RoomType;
import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.utils.BlockSelection;

public class YamlReader {
	private FnafYMain main;
	private File mapFolder;
	private final String roomConfigName = "roomConfig";
	private final String doorConfigName = "doorConfig";
	private final String minimapConfigName = "minimapConfig";
	private final String animatronicConfigName = "animatronicConfig";
	public YamlReader(FnafYMain _main) {
		main=_main;
		mapFolder = new File(main.getDataFolder(),"Maps");
		if (!mapFolder.exists()) {
			mapFolder.mkdirs();
			main.getLogger().info(ChatColor.GREEN+"Maps file created");
		 }
	}
	//--------------------------------------------------------------------------------------
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
	private YamlConfiguration getMinimapConfig(String mapName) {
		return getConfig(mapName,minimapConfigName);
	}
	private YamlConfiguration getAnimatronicConfig(String mapName) {
		return getConfig(mapName,animatronicConfigName);
	}
	private boolean saveDoorConfig(String mapName, YamlConfiguration config) {
		return saveConfig(mapName, doorConfigName, config);
	}
	private boolean saveRoomConfig(String mapName, YamlConfiguration config) {
		return saveConfig(mapName, roomConfigName, config);
	}
	private boolean saveMinimapConfig(String mapName, YamlConfiguration config) {
		return saveConfig(mapName, minimapConfigName, config);
	}
	private boolean saveAnimatrnicConfig(String mapName, YamlConfiguration config) {
		return saveConfig(mapName, animatronicConfigName, config);
	}
	public boolean configFilesExists(String mapName) {
		File room = new File(mapFolder.getPath()+"/"+mapName,roomConfigName+".yml");
		File door = new File(mapFolder.getPath()+"/"+mapName,doorConfigName+".yml");
		File minimap = new File(mapFolder.getPath()+"/"+mapName,minimapConfigName+".yml");
		File animatronic = new File(mapFolder.getPath()+"/"+mapName,animatronicConfigName+".yml");
		if(room.exists() && door.exists() && minimap.exists() && animatronic.exists()) {
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
				new File(mapConfigFolder,"minimapConfig.yml").createNewFile();
				new File(mapConfigFolder,"animatronicConfig.yml").createNewFile();
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
					doorConfig.set(doorName+".minimapPose.room1."+anim.toString(), new String[0]);
					doorConfig.set(doorName+".minimapPose.room2."+anim.toString(), new String[0]);
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
	public boolean addDoorMinimapAnimation(String mapName, String doorName, String roomName, Animatronic anim, String animation) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				if(roomExist(mapName, roomName)) {
					if(getLinkedRoomsNames(mapName, doorName).contains(roomName)) {
						if(anim!=null) {
							if(main.getAnimManager().getAsanims().containsKey(animation)) {
								List<String> animationList = getDoorRoomMinimapAnimation(mapName, doorName, roomName, anim);
								if(!animationList.contains(animation)) {
									animationList.add(animation);
									doorConfig.set(doorName+".minimapPose.room"+getRoomNumberFromName(mapName, doorName, roomName)+"."+anim.toString(), animationList);
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
	public boolean removeDoorMinimapAnimation(String mapName, String doorName, String roomName, Animatronic anim, String animation) {
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				if(roomExist(mapName, roomName)) {
					if(getLinkedRoomsNames(mapName, doorName).contains(roomName)) {
						if(anim!=null) {
							if(main.getAnimManager().getAsanims().containsKey(animation)) {
								List<String> animationList = getDoorRoomMinimapAnimation(mapName, doorName, roomName, anim);
								if(animationList.contains(animation)) {
									animationList.remove(animation);
									doorConfig.set(doorName+".minimapPose.room"+getRoomNumberFromName(mapName, doorName, roomName)+"."+anim.toString(), animationList);
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
	public List<String> getDoorRoomMinimapAnimation(String mapName, String doorName, String roomName, Animatronic anim){
		YamlConfiguration doorConfig = getDoorConfig(mapName);
		if (doorConfig != null) {
			if(doorExist(mapName, doorName)) {
				if(roomExist(mapName, roomName)) {
					int roomNum = getRoomNumberFromName(mapName, doorName, roomName);
					if(roomNum!=0) {
						return doorConfig.getStringList(doorName+".minimapPose.room"+roomNum+"."+anim.toString());
					}
				}
			}
		}
		return null;
	}
	public HashMap<String, Door> getDoorObjectHash(HashMap<String, Room> rm){
		HashMap<String, Door> doors = new HashMap<>();
		if(mapExist(main.getMapName())&&configFilesExists(main.getMapName())) {
			for(String doorName : getDoorList(main.getMapName())) {
				DoorType type = getDoorType(main.getMapName(), doorName);
				Location doorLoc = getDoorLocation(main.getMapName(), doorName);
				Vector length = getDoorLength(main.getMapName(), doorName);
				Room room1 =rm.get(getLinkedRoomName(main.getMapName(), doorName, 1));
				Room room2 =rm.get(getLinkedRoomName(main.getMapName(), doorName, 2));
				HashMap<Animatronic,List<ASAnimation>> animToRoom1 =new HashMap<Animatronic,List<ASAnimation>>();
				HashMap<Animatronic,List<ASAnimation>> animToRoom2 =new HashMap<Animatronic,List<ASAnimation>>();
				HashMap<Animatronic,List<ASAnimation>> minimapToRoom1 =new HashMap<Animatronic,List<ASAnimation>>();
				HashMap<Animatronic,List<ASAnimation>> minimapToRoom2 =new HashMap<Animatronic,List<ASAnimation>>();
				for(Animatronic animatronic : Animatronic.values()) {
					for(int x=1;x<=2;x++) {
						List<ASAnimation> tempList = new ArrayList<>();
						List<String> temp = getDoorRoomAnimation(main.getMapName(), doorName, getLinkedRoomName(main.getMapName(), doorName, x), animatronic);
						if(temp.isEmpty()) {
							System.out.println("InRoomPose was empty for room"+x);
							return null;
						}
						for(String animation : temp) {
							tempList.add(main.getAnimManager().getAsanims().get(animation));
						}
						if(x==1) {
							animToRoom1.put(animatronic, tempList);
						}else {
							animToRoom2.put(animatronic, tempList);
						}
						//
						tempList.clear();
						temp = getDoorRoomMinimapAnimation(main.getMapName(), doorName, getLinkedRoomName(main.getMapName(), doorName, x), animatronic);
						if(temp.isEmpty()) {
							System.out.println("MinimapPose was empty for room"+x);
							return null;
						}
						for(String animation : temp) {
							tempList.add(main.getAnimManager().getAsanims().get(animation));
						}
						if(x==1) {
							minimapToRoom1.put(animatronic, tempList);
						}else {
							minimapToRoom2.put(animatronic, tempList);
						}
					}
				}
				if(type!=DoorType.UNKNOWN && doorLoc!=null && length!=null && room1!=null && room2!=null){
					doors.put(doorName, new Door(doorName, type, doorLoc, length, room1, room2, animToRoom1, animToRoom2, minimapToRoom1, minimapToRoom2));
				}else {
					return null;
				}
			}
		}
		return doors;
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
	public List<String> getRoomMinimapAnimation(String mapName, String roomName, Animatronic anim){
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				if(anim!=null) {
					return roomConfig.getStringList(roomName+".minimapPose."+anim.toString());
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
				
				roomConfig.set(roomName+".defaultGuardCamera", false);
				roomConfig.set(roomName+".aftonCameraPackage", 0);
				
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
	public int getAftonCameraPackage(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				return roomConfig.getInt(roomName+".aftonCameraPackage");
			}
		}
		return 0;
	}
	public boolean setAftonCameraPackage(String mapName, String roomName, int p) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				roomConfig.set(roomName+".aftonCameraPackage", p);
				saveRoomConfig(mapName, roomConfig);
				return true;
			}
		}
		return false;
	}
	public boolean getIfDefaultGuardCamera(String mapName, String roomName) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				return roomConfig.getBoolean(roomName+".defaultGuardCamera");
			}
		}
		return false;
	}
	public boolean setIfDefaultGuardCamera(String mapName, String roomName, boolean b) {
		YamlConfiguration roomConfig = getRoomConfig(mapName);
		if (roomConfig != null) {
			if(roomExist(mapName, roomName)) {
				roomConfig.set(roomName+".defaultGuardCamera", b);
				saveRoomConfig(mapName, roomConfig);
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
				boolean isDefaultGuardCamera = getIfDefaultGuardCamera(main.getMapName(), roomName);
				int aftonCameraPackage = getAftonCameraPackage(main.getMapName(), roomName);
				BlockSelection aO = getAftonOutline(main.getMapName(), roomName);
				BlockSelection aS = getAftonSurface(main.getMapName(), roomName);
				BlockSelection gO = getGuardOutline(main.getMapName(), roomName);
				BlockSelection gS = getGuardSurface(main.getMapName(), roomName);
				HashMap<Animatronic,List<ASAnimation>> inRoomAnimation =new HashMap<Animatronic,List<ASAnimation>>();
				HashMap<Animatronic,List<ASAnimation>> inMinimapAnimation =new HashMap<Animatronic,List<ASAnimation>>();
				for(Animatronic animatronic : Animatronic.values()) {
					List<ASAnimation> tempList = new ArrayList<>();
					List<String> temp = getRoomAnimation(main.getMapName(), roomName, animatronic);
					if(temp.isEmpty()) {
						return null;
					}
					for(String animation : temp) {
						tempList.add(main.getAnimManager().getAsanims().get(animation));
					}
					inRoomAnimation.put(animatronic, tempList);
					
					tempList.clear();
					temp = getRoomMinimapAnimation(main.getMapName(), roomName, animatronic);
					if(temp.isEmpty()) {
						return null;
					}
					for(String animation : temp) {
						tempList.add(main.getAnimManager().getAsanims().get(animation));
					}
					inMinimapAnimation.put(animatronic, tempList);
				}
				if(type!=RoomType.UNKNOWN && camLoc!=null && aftonCameraPackage>0 && aO!=null && aS!=null && gO!=null && gS!=null){
					rooms.put(roomName, new Room(main, roomName, type, camLoc, isDefaultGuardCamera, aftonCameraPackage, inRoomAnimation, aS, aO, gS, gO, inMinimapAnimation));
				}else {
					return null;
				}
			}
		}
		return rooms;
	}
	public boolean setGuardRoomLocation(String mapName, Location loc) {
		YamlConfiguration minimapConfig = getMinimapConfig(mapName);
		if (minimapConfig != null) {
			minimapConfig.set("guardRoomLocation", loc);
			saveMinimapConfig(mapName, minimapConfig);
			return true;
		}
		return false;
	}
	public Location getGuardRoomLocation(String mapName) {
		YamlConfiguration minimapConfig = getMinimapConfig(mapName);
		if (minimapConfig != null) {
			return minimapConfig.getLocation("guardRoomLocation");
		}
		return null;
	}
	//--------------------------------------------------------------------------------------
	public boolean setGuardCameraBlockLocation(String mapName, Location loc) {
		YamlConfiguration minimapConfig = getMinimapConfig(mapName);
		if (minimapConfig != null) {
			minimapConfig.set("guardCameraBlockLocation", loc);
			saveMinimapConfig(mapName, minimapConfig);
			return true;
		}
		return false;
	}
	public Location getGuardCameraBlockLocation(String mapName) {
		YamlConfiguration minimapConfig = getMinimapConfig(mapName);
		if (minimapConfig != null) {
			return minimapConfig.getLocation("guardCameraBlockLocation");
		}
		return null;
	}
	public boolean setGuardCameraBaseLocation(String mapName, Location loc) {
		YamlConfiguration minimapConfig = getMinimapConfig(mapName);
		if (minimapConfig != null) {
			minimapConfig.set("guardCameraBaseLocation", loc);
			saveMinimapConfig(mapName, minimapConfig);
			return true;
		}
		return false;
	}
	public Location getGuardCameraBaseLocation(String mapName) {
		YamlConfiguration minimapConfig = getMinimapConfig(mapName);
		if (minimapConfig != null) {
			return minimapConfig.getLocation("guardCameraBaseLocation");
		}
		return null;
	}
	public boolean setAftonCameraBaseLocation(String mapName, Location loc) {
		YamlConfiguration minimapConfig = getMinimapConfig(mapName);
		if (minimapConfig != null) {
			minimapConfig.set("aftonCameraBaseLocation", loc);
			saveMinimapConfig(mapName, minimapConfig);
			return true;
		}
		return false;
	}
	public Location getAftonCameraBaseLocation(String mapName) {
		YamlConfiguration minimapConfig = getMinimapConfig(mapName);
		if (minimapConfig != null) {
			return minimapConfig.getLocation("aftonCameraBaseLocation");
		}
		return null;
	}
	//--------------------------------------------------------------------------------------
	public boolean setAnimatronicBaseRoomName(String mapName, String roomName, Animatronic anim) {
		YamlConfiguration animatronicConfig = getAnimatronicConfig(mapName);
		if (animatronicConfig != null) {
			animatronicConfig.set("baseRoom."+anim.toString(), roomName);
			saveAnimatrnicConfig(mapName, animatronicConfig);
			return true;
		}
		return false;
	}
	public String getAnimatronicBaseRoomName(String mapName, Animatronic anim) {
		YamlConfiguration animatronicConfig = getAnimatronicConfig(mapName);
		if (animatronicConfig != null) {
			return animatronicConfig.getString("baseRoom."+anim.toString());
		}
		return null;
	}
}
