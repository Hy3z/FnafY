package fr.nekotine.fnafy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.commands.ComGame;
import fr.nekotine.fnafy.commands.ComMapper;
import fr.nekotine.fnafy.events.EventListener;
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
	private boolean gameRunnig=false;
	//private ArrayList<Door> doorList = new ArrayList<Door>();
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
	/*public boolean loadGame() {
		if(loadFiles()) {
			return true;
		}
		return false;
	}
	private boolean loadFiles() {
		if(yamlReader.mapExist(mapName)&&yamlReader.configFilesExists(mapName)) {
			HashMap<String, Room> roomHashMap = new HashMap<>();
			for(String roomName : yamlReader.getRoomList(mapName)) {
				yamlReader.getCameraLocation(mapName, roomName);
				yamlReader.getRoomType(mapName, roomName);
				
			}
		}
		return false;
	}*/
	@Override
	public void onDisable() {
		super.onDisable();
		animManager.disable();
	}
}
