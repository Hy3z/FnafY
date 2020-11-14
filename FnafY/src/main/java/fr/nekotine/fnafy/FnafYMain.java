package fr.nekotine.fnafy;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.events.EventListener;
import fr.nekotine.fnafy.utils.BlockSelectionPart;

public class FnafYMain extends JavaPlugin {
	
	public final File animFile = new File(getDataFolder(),"Animations");
	public final HashMap<String,ASAnimation> asanims = new HashMap<String,ASAnimation>();
	
	public EventListener eListener;
	
	public void onEnable() {
		super.onEnable();
		//Register serializable//
		ConfigurationSerialization.registerClass(BlockSelectionPart.class, "BlockSelectionPart");
		
		eListener = new EventListener();
		Bukkit.getPluginManager().registerEvents(eListener, this);
		getLogger().info("Checking for Anim file");
		if (!animFile.exists()) {
			animFile.getParentFile().mkdirs();
			getLogger().info(ChatColor.GREEN+"Anim file created");
		 }
		//---COMMANDS---//
		ComAnim.registerAnimCommands();
	}
	
	public ASAnimation getASAnim(String animName) {
		return asanims.get(animName);
	}
}
