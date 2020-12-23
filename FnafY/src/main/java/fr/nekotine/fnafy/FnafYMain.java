package fr.nekotine.fnafy;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.commands.ComGame;
import fr.nekotine.fnafy.commands.ComMapper;
import fr.nekotine.fnafy.doors.Door;
import fr.nekotine.fnafy.doors.DoorManager;
import fr.nekotine.fnafy.events.PlayerMoveHeadListener;
import fr.nekotine.fnafy.room.Room;
import fr.nekotine.fnafy.room.RoomManager;
import fr.nekotine.fnafy.utils.BlockSelection;
import fr.nekotine.fnafy.utils.BlockSelectionPart;
import fr.nekotine.fnafy.utils.CustomEulerAngle;
import fr.nekotine.fnafy.utils.Posture;

public class FnafYMain extends JavaPlugin {
	
	private YamlReader yamlReader;
	private ComAnim animManager = new ComAnim(this);
	private ComMapper mapManager = new ComMapper(this);
	private ComGame gameManager = new ComGame(this);
	
	private String mapName = "";
	private PlayerMoveHeadListener headListener = new PlayerMoveHeadListener(this);
	private RoomManager roomManager = new RoomManager(this);
	private DoorManager doorManager = new DoorManager(this);
	private AftonMinimapManager aftonMinimapManager = new AftonMinimapManager(this);
	private GuardMinimapManager guardMinimapManager = new GuardMinimapManager(this);
	public final TeamGuard teamguard = new TeamGuard();
	public final TeamAfton teamafton = new TeamAfton();
	private boolean gameRunnig=false;
	
	public void onEnable() {
		super.onEnable();
		Bukkit.getPluginManager().registerEvents(headListener, this);
		//Register serializables//
		ConfigurationSerialization.registerClass(CustomEulerAngle.class, "CustomEulerAngle");
		ConfigurationSerialization.registerClass(BlockSelectionPart.class, "BlockSelectionPart");
		ConfigurationSerialization.registerClass(BlockSelection.class, "BlockSelection");
		ConfigurationSerialization.registerClass(Posture.class, "Posture");
		ConfigurationSerialization.registerClass(ASAnimation.class, "ASAnimation");
		ConfigurationSerialization.registerClass(ASAnimOrder.class, "ASAnimOrder");
		//
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
	public AftonMinimapManager getAftonMinimapManager() {
		return aftonMinimapManager;
	}
	public boolean startGame() {
		if(loadGame()) {
			headListener.triggerSchedule();
			return true;
		}
		return false;
	}
	private boolean loadGame() {
		if(loadFiles()) {
			return true;
		}
		return false;
	}
	public PlayerMoveHeadListener getHeadListener() {
		return headListener;
	}
	public RoomManager getRoomManager() {
		return roomManager;
	}
	public TeamAfton getTeamAfton() {
		return teamafton;
	}
	public TeamGuard getTeamGuard() {
		return teamguard;
	}
	public DoorManager getDoorManager() {
		return doorManager;
	}
	private boolean loadFiles() {
		HashMap<String, Room> rooms = yamlReader.getRoomObjectsHash();
		if(rooms!=null) {
			roomManager.setRoomHash(rooms);
			HashMap<String, Door> doors = yamlReader.getDoorObjectHash(roomManager);
			if(doors!=null) {
				doorManager.setDoorHash(doors);
				return true;
			}
		}
		return false;
	}
	@Override
	public void onDisable() {
		super.onDisable();
		animManager.disable();
	}
	public GuardMinimapManager getGuardMinimapManager() {
		return guardMinimapManager;
	}
}
