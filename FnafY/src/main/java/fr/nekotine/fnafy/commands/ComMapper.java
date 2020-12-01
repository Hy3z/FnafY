package fr.nekotine.fnafy.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.doors.DoorType;
import fr.nekotine.fnafy.room.RoomType;

public class ComMapper {
	private FnafYMain main;
	private List<String> mapArray = new ArrayList<String>();
	public ComMapper(FnafYMain _main) {
		main=_main;
	}
	private void setMapFinderArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("mapList", new StringArgument().overrideSuggestions((sender) -> {return mapArray.toArray(new String[mapArray.size()]);}));
	}
	public void searchForMaps() {
		for(String s : main.getYamlReader().getMapList()) {
			mapArray.add(s);
		}
	}
	private void setDoorTypeArgument(LinkedHashMap<String, Argument> argument) {
		String[] doorType = new String[DoorType.values().length];
		int x=-1;
		for(DoorType type : DoorType.values()) {
			x++;
			doorType[x] = type.toString();
		}
		argument.put("doorType", new StringArgument().overrideSuggestions(doorType));
	}
	private void setRoomTypeArgument(LinkedHashMap<String, Argument> argument) {
		String[] roomType = new String[RoomType.values().length];
		int x=-1;
		for(RoomType type : RoomType.values()) {
			x++;
			roomType[x] = type.toString();
		}
		argument.put("roomType", new StringArgument().overrideSuggestions(roomType));
	}
	private void set1Or2Argument(LinkedHashMap<String, Argument> argument) {
		argument.put("roomNumber", new IntegerArgument().overrideSuggestions(new String[]{"1","2"}));
	}
	private void setDoorFinderArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("doorList", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getYamlReader().getDoorList((String)args[3]).toArray(new String[0]);
		}));	
	}
	private void setRoomFinderArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("roomList", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getYamlReader().getRoomList((String)args[3]).toArray(new String[0]);
		}));	
	}
	private void setAutoCompleteArgument(LinkedHashMap<String, Argument> argument, String hinted) {
		argument.put(hinted, new LiteralArgument(hinted));
	}
	private void setFlatArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("arg", new StringArgument());
	}
	public void registerMapperCommands() {
		main.getLogger().info("Registering Mapper commands");
		LinkedHashMap<String, Argument> argument = new LinkedHashMap<String,Argument>();
		
		setAutoCompleteArgument(argument,"setMap");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			main.setMapName((String)args[0]);
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"create");
		setFlatArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().createMap((String)args[0])) {
				mapArray.add((String)args[0]);
				sender.sendMessage(ChatColor.DARK_GREEN+"Map cr�e!");
			}else {
				sender.sendMessage(ChatColor.RED+"Une map existe d�j� avec ce nom");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"delete");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().deleteMap((String)args[0])) {
				mapArray.remove((String)args[0]);
				sender.sendMessage(ChatColor.DARK_GREEN+"Map supprim�e!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map n'�xiste pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"add");
		setMapFinderArgument(argument);
		setFlatArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().addDoor((String)args[0],(String)args[1])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] cr�e");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map n'�xiste pas, ou la porte existe d�j�");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"delete");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().removeDoor((String)args[0],(String)args[1])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] supprim�e");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map ou la porte n'�xistent pas");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"add");
		setMapFinderArgument(argument);
		setFlatArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().addRoom((String)args[0],(String)args[1])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Salle ["+(String)args[1]+"] cr�e");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map n'�xiste pas, ou la salle existe d�j�");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"delete");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().removeRoom((String)args[0],(String)args[1])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Salle ["+(String)args[1]+"] supprim�e");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'�xistent pas");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"setType");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		setDoorTypeArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().setDoorType((String)args[0],(String)args[1],DoorType.valueOf((String)args[2]))) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] mise � jour!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte ou le type de porte n'existent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setType");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setRoomTypeArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().setRoomType((String)args[0],(String)args[1],RoomType.valueOf((String)args[2]))) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] mise � jour!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la salle ou le type de salle n'existent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setCamLoc");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(sender instanceof Player) {
				if(main.getYamlReader().setCameraLocation((String)args[0], (String)args[1], ((Player) sender).getLocation())) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Cam�ra de la salle ["+(String)args[1]+"] mise � jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'existent pas!");
				}
				return;
			}
			sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"LinkToRoom");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		setRoomFinderArgument(argument);
		set1Or2Argument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().linkRoomToDoor((String)args[0],(String)args[1],(String)args[2],(int)args[3])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Salle ["+(String)args[2]+"] li�e � porte ["+(String)args[1]+"] � la sortie n�"+(int)args[3]);
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte, la salle ou le num�ro n'existent pas!");
			}
		}).register();
		argument.clear();
		
		main.getLogger().info("Mapper Commands registered");
	}
}
