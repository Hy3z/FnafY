package fr.nekotine.fnafy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.events.EventListener;
import fr.nekotine.fnafy.utils.BlockSelectionPart;

public class FnafYMain extends JavaPlugin {
	
	public EventListener eListener;
	public ComAnim animManager = new ComAnim(this);
	
	public void onEnable() {
		super.onEnable();
		//Register serializables//
		ConfigurationSerialization.registerClass(BlockSelectionPart.class, "BlockSelectionPart");
		//
		eListener = new EventListener();
		Bukkit.getPluginManager().registerEvents(eListener, this);
		//---COMMANDS---//
		animManager.registerAnimCommands();
		//
		animManager.reloadASAnims();
	}
}
