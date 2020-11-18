package fr.nekotine.fnafy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.commands.ComMapper;
import fr.nekotine.fnafy.events.EventListener;
import fr.nekotine.fnafy.utils.BlockSelectionPart;
import fr.nekotine.fnafy.utils.Posture;

public class FnafYMain extends JavaPlugin {
	
	private EventListener eListener;
	private YamlReader yamlReader;
	private ComAnim animManager = new ComAnim(this);
	private ComMapper commandManager = new ComMapper(this);
	private String mapName = "";
	public void onEnable() {
		super.onEnable();
		//Register serializables//
		ConfigurationSerialization.registerClass(BlockSelectionPart.class, "BlockSelectionPart");
		ConfigurationSerialization.registerClass(Posture.class, "Posture");
		//
		eListener = new EventListener();
		Bukkit.getPluginManager().registerEvents(eListener, this);
		yamlReader = new YamlReader(this);
		//---COMMANDS---//
		animManager.registerAnimCommands();
		commandManager.registerAnimCommands();
		//
		animManager.reloadASAnims();
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
}
