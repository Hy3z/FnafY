package fr.nekotine.fnafy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.commands.ComGame;
import fr.nekotine.fnafy.commands.ComMapper;
import fr.nekotine.fnafy.doors.Door;
import fr.nekotine.fnafy.events.EventListener;
import fr.nekotine.fnafy.room.Room;
import fr.nekotine.fnafy.room.RoomMinimapManager;
import fr.nekotine.fnafy.utils.BlockSelectionPart;
import fr.nekotine.fnafy.utils.CustomEulerAngle;
import fr.nekotine.fnafy.utils.Posture;

public class FnafYMain extends JavaPlugin {
	
	private EventListener eListener;
	private YamlReader yamlReader;
	private ComAnim animManager = new ComAnim(this);
	private ComMapper mapManager = new ComMapper(this);
	private ComGame gameManager = new ComGame(this);
	
	private String mapName = "";
	private HashMap<String, Room> roomsHashMap = new HashMap<>();
	List<Door> doorList = new ArrayList<>();
	private RoomMinimapManager roomMinimapManager;
	private boolean gameRunnig=false;
	
	public void onEnable() {
		super.onEnable();
		//Register serializables//
		ConfigurationSerialization.registerClass(CustomEulerAngle.class, "CustomEulerAngle");
		ConfigurationSerialization.registerClass(BlockSelectionPart.class, "BlockSelectionPart");
		ConfigurationSerialization.registerClass(Posture.class, "Posture");
		ConfigurationSerialization.registerClass(ASAnimation.class, "ASAnimation");
		ConfigurationSerialization.registerClass(ASAnimOrder.class, "ASAnimOrder");
		//
		eListener = new EventListener();
		Bukkit.getPluginManager().registerEvents(eListener, this);
		yamlReader = new YamlReader(this);
		//---COMMANDS---//
		animManager.registerAnimCommands();
		mapManager.registerMapperCommands();
		gameManager.registerGameCommands();
		//
		animManager.reloadASAnims();
		mapManager.searchForMaps();
	}
	public ComAnim getAnimManager() {
		return animManager;
	}
	public YamlReader getYamlReader() {
		return yamlReader;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String _mapName) {
		mapName=_mapName;
	}
	public boolean isGameRunning() {
		return gameRunnig;
	}
	public boolean startGame() {
		if(loadGame()) {
			return true;
		}
		return false;
	}
	private boolean loadGame() {
		if(loadFiles()) {
			//START GAME
			return true;
		}
		return false;
	}
	private boolean loadFiles() {
		roomsHashMap = yamlReader.getRoomObjectsHash();
		doorList = yamlReader.getDoorObjectList();
		if(!roomsHashMap.isEmpty() && !doorList.isEmpty()) {
			return true;
		}
		return false;
	}
	@Override
	public void onDisable() {
		super.onDisable();
		animManager.disable();
	}
	public HashMap<String, Room> getRoomsHashMap() {
		return roomsHashMap;
	}
	public RoomMinimapManager getRoomMinimapManager() {
		return roomMinimapManager;
	}
}
