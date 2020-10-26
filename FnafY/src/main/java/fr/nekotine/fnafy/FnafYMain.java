package fr.nekotine.fnafy;

import java.util.LinkedHashMap;

import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;

public class FnafYMain extends JavaPlugin {
	public void onEnable() {
		super.onEnable();
		//---COMMANDS---//
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<String,Argument>();
	    arguments.put("join", new LiteralArgument("join"));
		new CommandAPICommand("fnafy").withArguments(arguments).executesPlayer((player,args)->{
			//command here
		}).register();
		//----
	}
}
