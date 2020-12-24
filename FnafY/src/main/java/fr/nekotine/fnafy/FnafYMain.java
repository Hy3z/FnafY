package fr.nekotine.fnafy;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.commands.ComGame;
import fr.nekotine.fnafy.commands.ComMapper;
import fr.nekotine.fnafy.doors.Door;
import fr.nekotine.fnafy.doors.DoorManager;
import fr.nekotine.fnafy.events.GameStartEvent;
import fr.nekotine.fnafy.events.GameStopEvent;
import fr.nekotine.fnafy.events.GameTickEvent;
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
	private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
	
	private String mapName = "";
	private PlayerMoveHeadListener headListener = new PlayerMoveHeadListener();
	private RoomManager roomManager = new RoomManager(this);
	private DoorManager doorManager = new DoorManager(this);
	private AftonMinimapManager aftonMinimapManager = new AftonMinimapManager(this);
	private GuardMinimapManager guardMinimapManager = new GuardMinimapManager(this);
	public TeamGuard teamguard;
	public TeamAfton teamafton;
	private boolean gameRunning=false;
	
	private int scheduler;
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
		teamguard = new TeamGuard();
		teamafton = new TeamAfton();
		//---COMMANDS---//
		animManager.registerAnimCommands();
		mapManager.registerMapperCommands();
		ComGame.registerGameCommands(this);
		//
		animManager.reloadASAnims();
		mapManager.searchForMaps();
	}
	public ComAnim getAnimManager() {
		return animManager;
	}
	public ProtocolManager getProtocolManager() {
		return protocolManager;
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
	public boolean addPlayer(UUID id) {
		if (isPlayerInGame(id)) return false;
		teamguard.addPlayer(new GuardWrapper(id));
		return true;
	}
	public boolean isGameRunning() {
		return gameRunning;
	}
	public AftonMinimapManager getAftonMinimapManager() {
		return aftonMinimapManager;
	}
	public boolean isPlayerInGame(UUID id) {
		return teamguard.isPlayerInTeam(id) || teamafton.isPlayerInTeam(id);
	}
	public boolean startGame() {
		if (gameRunning) {
			Bukkit.getPluginManager().callEvent(new GameStopEvent());
			gameRunning=false;
		}
		if(loadGame()) {
			GameStartEvent evt = new GameStartEvent();
			Bukkit.getPluginManager().callEvent(evt);
			if (evt.isCancelled()) {
				return false;
			}
			gameRunning=true;
			tick();
			return true;
		}
		return false;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		UUID id = evt.getPlayer().getUniqueId();
		if (isPlayerInGame(id) && gameRunning) {
			if (teamafton.isPlayerInTeam(id)){
				//START FOR PLAYER
			}else {
				teamguard.getWrapper(id).showScoreboard(teamguard.scoreboard);
			}
		}
	}
	@EventHandler
	public void onGameEnd(GameStopEvent e) {
		getServer().getScheduler().cancelTask(scheduler);
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
		Bukkit.getPluginManager().callEvent(new GameStopEvent());
		animManager.disable();
		super.onDisable();
	}
	public GuardMinimapManager getGuardMinimapManager() {
		return guardMinimapManager;
	}
	private void tick() {
		scheduler = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	        @Override
	        public void run() {
	        	GameTickEvent e = new GameTickEvent();
    			Bukkit.getPluginManager().callEvent(e);
	        }
	    }, 0, 1);
	}
}
