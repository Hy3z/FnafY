package fr.nekotine.fnafy.commands;

import java.util.LinkedHashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.team.Team;

public final class ComGame {
	private static String[] teams = {"GUARD","AFTON"};
	public static void registerGameCommands(FnafYMain main) {
		main.getLogger().info("Registering Game commands");
		LinkedHashMap<String, Argument> argument = new LinkedHashMap<String,Argument>();
		
		argument.put("game", new LiteralArgument("game"));
		argument.put("start", new LiteralArgument("start"));
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.startGame()) {
				sender.sendMessage(ChatColor.GREEN+"Game started!");
			}else {
				sender.sendMessage(ChatColor.RED+"Error on game start");
			}
		}).register();
		argument.clear();
		
		argument.put("game", new LiteralArgument("game"));
		argument.put("getMap", new LiteralArgument("getMap"));
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			sender.sendMessage("Current Map: "+ChatColor.GOLD+main.getMapName());
		}).register();
		argument.clear();
		
		argument.put("game", new LiteralArgument("game"));
		argument.put("join", new LiteralArgument("join"));
		new CommandAPICommand("fnafy").withArguments(argument).executesPlayer((player,args)->{
			main.addPlayer(player.getUniqueId());
		}).register();
		argument.clear();
		
		argument.put("game", new LiteralArgument("game"));
		argument.put("join", new LiteralArgument("join"));
		argument.put("teams", new StringArgument().overrideSuggestions(teams));
		new CommandAPICommand("fnafy").withArguments(argument).executesPlayer((player,args)->{
			main.addPlayer(player.getUniqueId(),(Team)args[0]);
		}).register();
		argument.clear();
		
		argument.put("game", new LiteralArgument("game"));
		argument.put("join", new LiteralArgument("join"));
		argument.put("players", new PlayerArgument());
		argument.put("teams", new StringArgument().overrideSuggestions(teams));
		new CommandAPICommand("fnafy").withArguments(argument).executesPlayer((player,args)->{
			main.addPlayer(((Player)args[0]).getUniqueId(),(Team)args[1]);
		}).register();
		argument.clear();
		
		argument.put("game", new LiteralArgument("game"));
		argument.put("join", new LiteralArgument("join"));
		argument.put("player", new PlayerArgument());
		new CommandAPICommand("fnafy").withArguments(argument).executesPlayer((player,args)->{
			main.addPlayer(((Player)args[0]).getUniqueId());
		}).register();
		argument.clear();
		main.getLogger().info("Game Commands registered");
	}
}
