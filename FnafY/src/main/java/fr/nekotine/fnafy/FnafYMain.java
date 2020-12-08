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
	public boolean loadGame() {
		if(loadFiles()) {
			return true;
		}
		return false;
	}
	private boolean loadFiles() {
		if(yamlReader.mapExist(mapName)&&yamlReader.configFilesExists(mapName)) {
			//load door
			/*for(String doorName : yamlReader.getDoorList(mapName)) {
				DoorType doorType = yamlReader.getDoorType(mapName, doorName);
				Location doorLocation = yamlReader.getDoorLocation(mapName, doorName);
				Vector doorLength = yamlReader.getDoorLength(mapName, doorName);
				String room1Name = yamlReader.getLinkedRoomName(mapName, doorName, 1);
				String room2Name = yamlReader.getLinkedRoomName(mapName, doorName, 2);
				HashMap<Animatronic,List<ASAnimation>> room1Animation=new HashMap<Animatronic,List<ASAnimation>>();
				HashMap<Animatronic,List<ASAnimation>> room2Animation=new HashMap<Animatronic,List<ASAnimation>>();
				for(Animatronic animatronic : Animatronic.values()) {
					List<ASAnimation> tempList = new ArrayList<>();
					List<String> temp = yamlReader.getDoorRoomAnimation(mapName, doorName, room1Name, animatronic);
					if(temp.isEmpty()) {
						break;
					}
					for(String animation : temp) {
						tempList.add(animManager.getAsanims().get(animation));
					}
					room1Animation.put(animatronic, tempList);
					
					tempList.clear();
					temp = yamlReader.getDoorRoomAnimation(mapName, doorName, room2Name, animatronic);
					if(temp.isEmpty()) {
						break;
					}
					for(String animation : temp) {
						
						tempList.add(animManager.getAsanims().get(animation));
					}
					room2Animation.put(animatronic, tempList);
				}
				if(doorType!=DoorType.UNKNOWN && doorLocation!=null && doorLength!=null && !room1Name.isEmpty() && !room2Name.isEmpty() 
						&& room1Animation.keySet().containsAll(Arrays.asList(Animatronic.values())) 
						&& room2Animation.keySet().containsAll(Arrays.asList(Animatronic.values()))) {
					new Door(room1Name, room2Name, doorLocation, room1Animation, room2Animation, aftses);
				}
			}
			//load door end
			
			for(String roomName : yamlReader.getRoomList(mapName)) {
				RoomType type = yamlReader.getRoomType(mapName, roomName);
				BlockSelection aftonsurf;
				BlockSelection aftonoutl;
				BlockSelection minmsurf;
				BlockSelection minmoutl;
				Location camLoc = yamlReader.getCameraLocation(mapName, roomName);
				HashMap<Animatronic,List<ASAnimation>> inRoomAnimation =new HashMap<Animatronic,List<ASAnimation>>();
				for(Animatronic animatronic : Animatronic.values()) {
					List<ASAnimation> tempList = new ArrayList<>();
					List<String> temp = yamlReader.getRoomAnimation(mapName, roomName, animatronic);
					if(temp.isEmpty()) {
						break;
					}
					for(String animation : temp) {
						tempList.add(animManager.getAsanims().get(animation));
					}
					inRoomAnimation.put(animatronic, tempList);
				}
				if(type!=RoomType.UNKNOWN && /*aftonsurfetcnonnulle camLoc!=null 
						&& inRoomAnimation.keySet().containsAll(Arrays.asList(Animatronic.values()))
						&& inRoomAnimation.keySet().containsAll(Arrays.asList(Animatronic.values()))) {
					
				}
			}*/
		}
		return false;
	}
	@Override
	public void onDisable() {
		super.onDisable();
		animManager.disable();
	}
}
