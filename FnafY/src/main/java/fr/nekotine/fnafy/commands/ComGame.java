package fr.nekotine.fnafy.commands;

import java.util.LinkedHashMap;

import org.bukkit.ChatColor;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import fr.nekotine.fnafy.FnafYMain;

public class ComGame {
	FnafYMain main;
	public ComGame(FnafYMain _main) {
		main=_main;
	}
	private void setAutoCompleteArgument(LinkedHashMap<String, Argument> argument, String hinted) {
		argument.put(hinted, new LiteralArgument(hinted));
	}
	public void registerGameCommands() {
		main.getLogger().info("Registering Game commands");
		LinkedHashMap<String, Argument> argument = new LinkedHashMap<String,Argument>();
		
		setAutoCompleteArgument(argument,"game");
		setAutoCompleteArgument(argument,"start");
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.startGame()) {
				sender.sendMessage(ChatColor.GREEN+"Game started!");
			}else {
				sender.sendMessage(ChatColor.RED+"Error on game start");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"game");
		setAutoCompleteArgument(argument,"getMap");
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			sender.sendMessage("Current Map: "+ChatColor.GOLD+main.getMapName());
		}).register();
		argument.clear();
		
		main.getLogger().info("Game Commands registered");
	}
}
