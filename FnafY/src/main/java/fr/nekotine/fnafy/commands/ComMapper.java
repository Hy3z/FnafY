package fr.nekotine.fnafy.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.doors.DoorType;
import fr.nekotine.fnafy.enums.Animatronic;
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
	private void setRoomFinderFromDoorArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("roomsFromDoor", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getYamlReader().getLinkedRoomsNames((String)args[3], (String)args[4]).toArray(new String[0]);
		}));
	}
	private void setAnimationFinderFromDoorArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("animationFromDoor", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getYamlReader().getDoorRoomAnimation((String)args[3], (String)args[4], (String)args[5], Animatronic.valueOf((String)args[6])).toArray(new String[0]);
		}));
	}
	private void setAnimationFinderFromRoomArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("animationFromRoom", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getYamlReader().getRoomAnimation((String)args[3], (String)args[4], Animatronic.valueOf((String)args[5])).toArray(new String[0]);
		}));
	}
	private void setAnimatronicArgument(LinkedHashMap<String, Argument> argument) {
		String[] animatronic = new String[Animatronic.values().length];
		int x=-1;
		for(Animatronic anim : Animatronic.values()) {
			x++;
			animatronic[x] = anim.toString();
		}
		argument.put("animatronic", new StringArgument().overrideSuggestions(animatronic));
	}
	private void setAnimationArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("animationList", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getAnimManager().getAsanims().values().stream().map(ASAnimation::getName).toArray(String[]::new);
		}));
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
	private void sendDoorInfo(CommandSender sender, String mapName, String doorName) {
		sender.sendMessage(ChatColor.WHITE+"-- INFORMATIONS FOR ["+ChatColor.GOLD+doorName+ChatColor.WHITE+"] --");
		sender.sendMessage(ChatColor.WHITE+"doorType: "+ChatColor.GOLD+main.getYamlReader().getDoorType(mapName, doorName).toString());
		Location doorLoc = main.getYamlReader().getDoorLocation(mapName, doorName);
		if(doorLoc!=null) {
			sender.sendMessage(ChatColor.WHITE+"doorLoc: "+ChatColor.GOLD+main.getYamlReader().getDoorLocation(mapName, doorName).toVector());
		}else {
			sender.sendMessage(ChatColor.WHITE+"doorLoc: "+ChatColor.GOLD+"null");
		}
		sender.sendMessage(ChatColor.WHITE+"length: "+ChatColor.GOLD+main.getYamlReader().getDoorLength(mapName, doorName));
		sender.sendMessage(ChatColor.WHITE+"room1Name: "+ChatColor.GOLD+main.getYamlReader().getLinkedRoomName(mapName, doorName, 1));
		sender.sendMessage(ChatColor.WHITE+"room2Name: "+ChatColor.GOLD+main.getYamlReader().getLinkedRoomName(mapName, doorName, 2));
		for(Animatronic anim : Animatronic.values()) {
			sender.sendMessage(ChatColor.WHITE+"room1."+anim+": "+ChatColor.GOLD+main.getYamlReader().getDoorRoomAnimation(mapName, doorName, main.getYamlReader().getLinkedRoomName(mapName, doorName, 1), anim));
			sender.sendMessage(ChatColor.WHITE+"room2."+anim+": "+ChatColor.GOLD+main.getYamlReader().getDoorRoomAnimation(mapName, doorName, main.getYamlReader().getLinkedRoomName(mapName, doorName, 2), anim));
		}
	}
	private void sendRoomInfo(CommandSender sender, String mapName, String roomName) {
		sender.sendMessage(ChatColor.WHITE+"-- INFORMATIONS FOR ["+ChatColor.GOLD+roomName+ChatColor.WHITE+"] --");
		sender.sendMessage(ChatColor.WHITE+"roomType: "+ChatColor.GOLD+main.getYamlReader().getRoomType(mapName, roomName).toString());
		Location camLoc = main.getYamlReader().getCameraLocation(mapName, roomName);
		if(camLoc!=null) {
			sender.sendMessage(ChatColor.WHITE+"camLoc: "+ChatColor.GOLD+main.getYamlReader().getCameraLocation(mapName, roomName).toVector());
		}else {
			sender.sendMessage(ChatColor.WHITE+"camLoc: "+ChatColor.GOLD+"null");
		}
		for(Animatronic anim : Animatronic.values()) {
			sender.sendMessage(ChatColor.WHITE+"Animation."+anim+": "+ChatColor.GOLD+main.getYamlReader().getRoomAnimation(mapName, roomName, anim));
		}
		
	}
	public void registerMapperCommands() {
		main.getLogger().info("Registering Mapper commands");
		LinkedHashMap<String, Argument> argument = new LinkedHashMap<String,Argument>();
		
		setAutoCompleteArgument(argument,"setMap");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().mapExist((String)args[0])) {
				main.setMapName((String)args[0]);
				sender.sendMessage(ChatColor.GREEN+"Map set!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map n'éxiste pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"create");
		setFlatArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().createMap((String)args[0])) {
				mapArray.add((String)args[0]);
				sender.sendMessage(ChatColor.DARK_GREEN+"Map crée!");
			}else {
				sender.sendMessage(ChatColor.RED+"Une map existe déjà avec ce nom");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"delete");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().deleteMap((String)args[0])) {
				mapArray.remove((String)args[0]);
				sender.sendMessage(ChatColor.DARK_GREEN+"Map supprimée!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map n'éxiste pas!");
			}
		}).register();
		argument.clear();
		//--------------------------------------------------------------------------------------
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"add");
		setMapFinderArgument(argument);
		setFlatArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().addDoor((String)args[0],(String)args[1])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] crée");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map n'éxiste pas, ou la porte existe déjà");
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
				sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] supprimée");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map ou la porte n'éxistent pas");
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
			try {
				if(main.getYamlReader().setDoorType((String)args[0],(String)args[1],DoorType.fromString((String)args[2]))) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] mise à jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map ou la porte n'éxistent pas!");
				}
			} catch (IllegalArgumentException e) {
				sender.sendMessage(ChatColor.RED+"Ce type de porte n'éxiste pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"linkToRoom");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		setRoomFinderArgument(argument);
		set1Or2Argument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().linkRoomToDoor((String)args[0],(String)args[1],(String)args[2],(int)args[3])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Salle ["+(String)args[2]+"] liée à porte ["+(String)args[1]+"] à la sortie n°"+(int)args[3]);
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte, la salle ou le numéro n'existent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"getInfo");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().doorExist((String)args[0], (String)args[1])) {
				sendDoorInfo(sender, (String)args[0], (String)args[1]);
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map ou la porte n'existent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"setLoc");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(sender instanceof Player) {
				Block block = ((Player) sender).getTargetBlockExact(10, FluidCollisionMode.NEVER);
				if(block != null) {
					if(block.getType().toString().contains("DOOR")) {
						if(main.getYamlReader().setDoorLocationAndLength((String)args[0], (String)args[1],block.getLocation())) {
							sender.sendMessage(ChatColor.GREEN+"Location mise à jour!");
						}else {
							sender.sendMessage(ChatColor.RED+"Cette map ou la porte n'existent pas!");
						}
					}else {
						sender.sendMessage(ChatColor.RED+"Visez une porte en faisant cette commande!");
					}
				}else {
					sender.sendMessage(ChatColor.RED+"Visez la porte en faisant cette commande!");
				}
			}else {
				sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"addAnimation");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		setRoomFinderFromDoorArgument(argument);
		setAnimatronicArgument(argument);
		setAnimationArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().addDoorAnimation((String)args[0], (String)args[1], (String)args[2], Animatronic.valueOf((String)args[3]), (String)args[4])) {
				sender.sendMessage(ChatColor.GREEN+"Animation set pour l'animatronic dans la porte précisée vers la salle précisée!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte, la salle, l'animatronic ou l'animation n'existent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"removeAnimation");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		setRoomFinderFromDoorArgument(argument);
		setAnimatronicArgument(argument);
		setAnimationFinderFromDoorArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().removeDoorAnimation((String)args[0], (String)args[1], (String)args[2], Animatronic.valueOf((String)args[3]), (String)args[4])) {
				sender.sendMessage(ChatColor.GREEN+"Animation enlevée pour l'animatronic dans la porte précisée vers la salle précisée!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte, la salle, l'animatronic ou l'animation n'existent pas (ou l'animation n'est pas présente) !");
			}
		}).register();
		argument.clear();
		//--------------------------------------------------------------------------------------
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"add");
		setMapFinderArgument(argument);
		setFlatArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().addRoom((String)args[0],(String)args[1])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Salle ["+(String)args[1]+"] crée");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map n'éxiste pas, ou la salle existe déjà");
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
				sender.sendMessage(ChatColor.DARK_GREEN+"Salle ["+(String)args[1]+"] supprimée");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'éxistent pas");
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
			try {
				if(main.getYamlReader().setRoomType((String)args[0],(String)args[1],RoomType.fromString((String)args[2]))) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] mise à jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'existent pas!");
				}
			} catch (IllegalArgumentException e) {
				sender.sendMessage(ChatColor.RED+"Ce type de salle n'éxiste pas!");
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
					sender.sendMessage(ChatColor.DARK_GREEN+"Caméra de la salle ["+(String)args[1]+"] mise à jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'existent pas!");
				}
				return;
			}
			sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"getInfo");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().roomExist((String)args[0], (String)args[1])) {
				sendRoomInfo(sender, (String)args[0], (String)args[1]);
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'existent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"addAnimation");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setAnimatronicArgument(argument);
		setAnimationArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().addRoomAnimation((String)args[0], (String)args[1], Animatronic.valueOf((String)args[2]), (String)args[3])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Animation: ["+(String)args[3]+"] ajoutée à l'animatronic "+(String)args[2]+" dans la salle ["+(String)args[1]+"]");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la salle, l'animatronic ou l'animation n'existent pas (ou l'animation est déjà présente) !");
			}
		}).register();
		argument.clear();

		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"removeAnimation");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setAnimatronicArgument(argument);
		setAnimationFinderFromRoomArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().removeRoomAnimation((String)args[0], (String)args[1], Animatronic.valueOf((String)args[2]), (String)args[3])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Animation: ["+(String)args[3]+"] enlevée à l'animatronic "+(String)args[2]+" dans la salle ["+(String)args[1]+"]");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la salle, l'animatronic ou l'animation n'existent pas (ou l'animation n'est pas présente) !");
			}
		}).register();
		argument.clear();
		
		main.getLogger().info("Mapper Commands registered");
	}
}
