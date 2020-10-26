package fr.nekotine.fnafy;

import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import fr.nekotine.fnafy.events.EventListener;

public class FnafYMain extends JavaPlugin {
	
	public EventListener eListener;
	
	public void onEnable() {
		super.onEnable();
		eListener = new EventListener();
		Bukkit.getPluginManager().registerEvents(eListener, this);
		//---COMMANDS---//
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<String,Argument>();
	    arguments.put("join", new LiteralArgument("join"));
		new CommandAPICommand("fnafy").withArguments(arguments).executesPlayer((player,args)->{
			//command here
		}).register();
		//----
	}
}
