package fr.nekotine.fnafy;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.commands.ComGame;
import fr.nekotine.fnafy.commands.ComMapper;
import fr.nekotine.fnafy.doorRoom.Door;
import fr.nekotine.fnafy.doorRoom.DoorRoomContainer;
import fr.nekotine.fnafy.doorRoom.Room;
import fr.nekotine.fnafy.events.GameStartEvent;
import fr.nekotine.fnafy.events.GameStopEvent;
import fr.nekotine.fnafy.events.GameTickEvent;
import fr.nekotine.fnafy.events.PlayerMoveHeadListener;
import fr.nekotine.fnafy.minimap.AftonMinimapManager;
import fr.nekotine.fnafy.minimap.GuardMinimapManager;
import fr.nekotine.fnafy.task.BaseTask;
import fr.nekotine.fnafy.team.AftonWrapper;
import fr.nekotine.fnafy.team.GuardWrapper;
import fr.nekotine.fnafy.team.Team;
import fr.nekotine.fnafy.team.TeamAfton;
import fr.nekotine.fnafy.team.TeamGuard;
import fr.nekotine.fnafy.utils.BlockSelection;
import fr.nekotine.fnafy.utils.BlockSelectionPart;
import fr.nekotine.fnafy.utils.CustomEulerAngle;
import fr.nekotine.fnafy.utils.Posture;
import fr.nekotine.fnafy.utils.SerializedBlock;

public class FnafYMain extends JavaPlugin {
	private YamlReader yamlReader;
	private ComAnim animManager = new ComAnim(this);
	private ComMapper mapManager = new ComMapper(this);
	private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
	public PluginManager pmanager;
	private String mapName = "";
	private PlayerMoveHeadListener headListener;
	public DoorRoomContainer doorRoomContainer;
	public AftonMinimapManager aftonMinimapManager;
	public GuardMinimapManager guardMinimapManager;
	public TeamGuard teamguard;
	public TeamAfton teamafton;
	private boolean gameRunning=false;
	
	private int scheduler;
	public void onEnable() {
		super.onEnable();
		pmanager=Bukkit.getPluginManager();
		//Register serializables//
		ConfigurationSerialization.registerClass(CustomEulerAngle.class, "CustomEulerAngle");
		ConfigurationSerialization.registerClass(BlockSelectionPart.class, "BlockSelectionPart");
		ConfigurationSerialization.registerClass(BlockSelection.class, "BlockSelection");
		ConfigurationSerialization.registerClass(Posture.class, "Posture");
		ConfigurationSerialization.registerClass(ASAnimation.class, "ASAnimation");
		ConfigurationSerialization.registerClass(ASAnimOrder.class, "ASAnimOrder");
		ConfigurationSerialization.registerClass(SerializedBlock.class,"SerializedBlock");
		BaseTask.registerSerialisables();
		//
		yamlReader = new YamlReader(this);
		teamguard = new TeamGuard();
		teamafton = new TeamAfton();
		//---COMMANDS---//
		animManager.registerAnimCommands();
		mapManager.registerMapperCommands();
		ComGame.registerGameCommands(this);
		
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
	public boolean addPlayer(UUID id, Team t) {
		if (isPlayerInGame(id)) return false;
		if(t==Team.GUARD) {
			teamguard.addPlayer(new GuardWrapper(id));
		}else {
			teamafton.addPlayer(new AftonWrapper(id));
		}
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
			pmanager.callEvent(new GameStopEvent());
			gameRunning=false;
		}
		if(loadGame()) {
			GameStartEvent evt = new GameStartEvent();
			pmanager.callEvent(evt);
			if (evt.isCancelled()) {
				return false;
			}
			headListener = new PlayerMoveHeadListener();
			pmanager.registerEvents(headListener, this);
			aftonMinimapManager = new AftonMinimapManager(this);
			guardMinimapManager = new GuardMinimapManager(this);
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
		gameRunning=false;
		HandlerList.unregisterAll(headListener);
		headListener=null;
		aftonMinimapManager=null;
		guardMinimapManager=null;
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
	private boolean loadFiles() {
		HashMap<String, Room> rooms = yamlReader.getRoomObjectsHash();
		if(rooms!=null) {
			HashMap<String, Door> doors = yamlReader.getDoorObjectHash(rooms);
			if(doors!=null) {
				doorRoomContainer = new DoorRoomContainer(this, rooms, doors);
				return true;
			}
		}
		return false;
	}
	@Override
	public void onDisable() {
		pmanager.callEvent(new GameStopEvent());
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
	        	pmanager.callEvent(e);
	        }
	    }, 0, 1);
	}
}
