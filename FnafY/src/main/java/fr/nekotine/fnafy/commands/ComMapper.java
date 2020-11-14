package fr.nekotine.fnafy.commands;

import java.util.LinkedHashMap;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.YamlReader;

public class ComMapper {
	static Argument mapArgument = new PlayerArgument().overrideSuggestions(YamlReader.getMapList());
	public static void registerAnimCommands(FnafYMain main) {
		main.getLogger().info("Registering Mapper commands");
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<String,Argument>();
	    arguments.put("setMap", new LiteralArgument("setMap"));
	    arguments.put("map", mapArgument);
		new CommandAPICommand("fnafy").withArguments(arguments).executes((sender,args)->{
			//setMap
		}).register();
		main.getLogger().info("Mapper Commands registered");
	}
}
