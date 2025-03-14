package fr.nekotine.fnafy.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.animatronic.Animatronic;
import fr.nekotine.fnafy.doorRoom.DoorType;
import fr.nekotine.fnafy.doorRoom.RoomType;
import fr.nekotine.fnafy.utils.BlockSelection;
import fr.nekotine.fnafy.utils.BlockSelectionPart;

public class ComMapper{
	private FnafYMain main;
	private List<String> mapArray = new ArrayList<String>();
	private HashMap<Player, OutlineCreator> outlineContainer = new HashMap<>();
	private String[] outlinePaths = {"aftonOutline","aftonSurface","guardOutline","guardSurface"};
	private String[] animString;
	private String[] doorTypeString;
	private String[] roomTypeString;
	private String[] materialString;
	private String[] armorPiece = {"helmet","chestplate","leggings","boots"};
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
		if(mapArray.size()>0) {
			main.setMapName(mapArray.get(0));
		}
	}
	private void setArmorPieceArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("armorPiece", new StringArgument().overrideSuggestions(armorPiece));
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
	private void setMinimapAnimationFinderFromDoorArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("animationFromMinimapDoor", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getYamlReader().getDoorRoomMinimapAnimation((String)args[3], (String)args[4], (String)args[5], Animatronic.valueOf((String)args[6])).toArray(new String[0]);
		}));
	}
	private void setAnimationFinderFromRoomArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("animationFromRoom", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getYamlReader().getRoomAnimation((String)args[3], (String)args[4], Animatronic.valueOf((String)args[5])).toArray(new String[0]);
		}));
	}
	private void setScreamAnimationFinderFromAnimatronicArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("screamFromAnim", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getYamlReader().getAnimatronicScreamAnimation((String)args[4], Animatronic.valueOf((String)args[5])).toArray(new String[0]);
		}));
	}
	private void setAnimatronicArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("animatronicList", new StringArgument().overrideSuggestions(animString));
	}
	private void setAnimationArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("animationList", new StringArgument().overrideSuggestions((sender, args) -> {
			return main.getAnimManager().getAsanims().values().stream().map(ASAnimation::getName).toArray(String[]::new);
		}));
	}
	private void setDoorTypeArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("doorType", new StringArgument().overrideSuggestions(doorTypeString));
	}
	private void setRoomTypeArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("roomType", new StringArgument().overrideSuggestions(roomTypeString));
	}
	private void setMaterialArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("material", new StringArgument().overrideSuggestions(materialString));
	}
	private void set1Or2Argument(LinkedHashMap<String, Argument> argument) {
		argument.put("roomNumber", new IntegerArgument(1,2));
	}
	private void set1To6Argument(LinkedHashMap<String, Argument> argument) {
		argument.put("1To6", new IntegerArgument(0,6));
	}
	private void setTrueOrFalseArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("boolean", new BooleanArgument());
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
	private void setOutlineArgument(LinkedHashMap<String, Argument> argument) {
		argument.put("outlinePaths", new StringArgument().overrideSuggestions(outlinePaths));
	}
	private void sendDoorInfo(CommandSender sender, String mapName, String doorName) {
		sender.sendMessage(ChatColor.WHITE+"-- INFORMATIONS FOR ["+ChatColor.GOLD+doorName+ChatColor.WHITE+"] --");
		sender.sendMessage(ChatColor.WHITE+"doorType: "+ChatColor.GOLD+main.getYamlReader().getDoorType(mapName, doorName).toString());
		Location doorLoc = main.getYamlReader().getDoorLocation(mapName, doorName);
		if(doorLoc!=null) {
			sender.sendMessage(ChatColor.WHITE+"doorLoc: "+ChatColor.GOLD+doorLoc.toVector());
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
	public void registerOutline(String mapName, String roomName, String outline, Location baseLoc, LinkedList<BlockSelectionPart> parts, Player p) {
		main.getYamlReader().setBlockSelection(mapName, roomName, outline, new BlockSelection(baseLoc,parts));
		stopOutlineCreator(p);
	}
	public void stopOutlineCreator(Player p) {
		HandlerList.unregisterAll(outlineContainer.get(p));
		outlineContainer.remove(p);
		p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
	}
	public FnafYMain getMain() {
		return main;
	}
	public void registerMapperCommands() {
		animString = new String[Animatronic.values().length];
		int x=-1;
		for(Animatronic anim : Animatronic.values()) {
			x++;
			animString[x] = anim.toString();
		}
		doorTypeString = new String[DoorType.values().length];
		x=-1;
		for(DoorType type : DoorType.values()) {
			x++;
			doorTypeString[x] = type.toString();
		}
		roomTypeString = new String[RoomType.values().length];
		x=-1;
		for(RoomType type : RoomType.values()) {
			x++;
			roomTypeString[x] = type.toString();
		}
		materialString = new String[Material.values().length];
		x=-1;
		for(Material mat : Material.values()) {
			x++;
			materialString[x] = mat.toString();
		}
		
		main.getLogger().info("Registering Mapper commands");
		LinkedHashMap<String, Argument> argument = new LinkedHashMap<String,Argument>();
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
		setAutoCompleteArgument(argument,"setMap");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			main.setMapName((String)args[0]);
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
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"setType");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		setDoorTypeArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			try {
				if(main.getYamlReader().setDoorType((String)args[0],(String)args[1],DoorType.fromString((String)args[2]))) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] mise � jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map ou la porte n'�xistent pas!");
				}
			} catch (IllegalArgumentException e) {
				sender.sendMessage(ChatColor.RED+"Ce type de porte n'�xiste pas!");
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
				sender.sendMessage(ChatColor.DARK_GREEN+"Salle ["+(String)args[2]+"] li�e � porte ["+(String)args[1]+"] � la sortie n�"+(int)args[3]);
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte, la salle ou le num�ro n'existent pas!");
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
							sender.sendMessage(ChatColor.GREEN+"Location mise � jour!");
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
				sender.sendMessage(ChatColor.GREEN+"Animation set pour l'animatronic dans la porte pr�cis�e vers la salle pr�cis�e!");
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
				sender.sendMessage(ChatColor.GREEN+"Animation enlev�e pour l'animatronic dans la porte pr�cis�e vers la salle pr�cis�e!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte, la salle, l'animatronic ou l'animation n'existent pas (ou l'animation n'est pas pr�sente) !");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"addMinimapAnimation");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		setRoomFinderFromDoorArgument(argument);
		setAnimatronicArgument(argument);
		setAnimationArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().addDoorMinimapAnimation((String)args[0], (String)args[1], (String)args[2], Animatronic.valueOf((String)args[3]), (String)args[4])) {
				sender.sendMessage(ChatColor.GREEN+"Minimap Animation set pour l'animatronic dans la porte pr�cis�e vers la salle pr�cis�e!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte, la salle, l'animatronic ou l'animation n'existent pas!");
			}
			
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"door");
		setAutoCompleteArgument(argument,"removeMinimapAnimation");
		setMapFinderArgument(argument);
		setDoorFinderArgument(argument);
		setRoomFinderFromDoorArgument(argument);
		setAnimatronicArgument(argument);
		setMinimapAnimationFinderFromDoorArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().removeDoorMinimapAnimation((String)args[0], (String)args[1], (String)args[2], Animatronic.valueOf((String)args[3]), (String)args[4])) {
				sender.sendMessage(ChatColor.GREEN+"Minimap Animation enlev�e pour l'animatronic dans la porte pr�cis�e vers la salle pr�cis�e!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la porte, la salle, l'animatronic ou l'animation n'existent pas (ou l'animation n'est pas pr�sente) !");
			}
			
		}).register();
		argument.clear();
		//--------------------------------------------------------------------------------------
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setCanGuardUnlockCamera");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setTrueOrFalseArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().setCanGuardUnlockCamera((String)args[0], (String)args[1], (boolean)args[2])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Cette cam�ra est d�sormais accesible par les gardes?: "+(boolean)args[2]);
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la salle ou le vrai/faux n'�xistent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setCanGuardEnterRoom");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setTrueOrFalseArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().setCanGuardEnterRoom((String)args[0], (String)args[1], (boolean)args[2])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Cette salle est d�sormais accesible par les gardes?: "+(boolean)args[2]);
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la salle ou le vrai/faux n'�xistent pas!");
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
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setType");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setRoomTypeArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			try {
				if(main.getYamlReader().setRoomType((String)args[0],(String)args[1],RoomType.fromString((String)args[2]))) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Porte ["+(String)args[1]+"] mise � jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'existent pas!");
				}
			} catch (IllegalArgumentException e) {
				sender.sendMessage(ChatColor.RED+"Ce type de salle n'�xiste pas!");
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
				sender.sendMessage(ChatColor.DARK_GREEN+"Animation: ["+(String)args[3]+"] ajout�e � l'animatronic "+(String)args[2]+" dans la salle ["+(String)args[1]+"]");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la salle, l'animatronic ou l'animation n'existent pas (ou l'animation est d�j� pr�sente) !");
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
				sender.sendMessage(ChatColor.DARK_GREEN+"Animation: ["+(String)args[3]+"] enlev�e � l'animatronic "+(String)args[2]+" dans la salle ["+(String)args[1]+"]");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la salle, l'animatronic ou l'animation n'existent pas (ou l'animation n'est pas pr�sente) !");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setMaterial");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setMaterialArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().setRoomMaterial((String)args[0], (String)args[1], Material.valueOf((String)args[2]))) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Material set!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, la salle ou le mat�rial n'�xistent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setOutline");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setOutlineArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(!outlineContainer.containsKey((Player)sender)){
				if(main.getYamlReader().roomExist((String)args[0], (String)args[1])) {
					OutlineCreator oc = new OutlineCreator(this, (String)args[0], (String)args[1], (String)args[2], (Player)sender);
					Bukkit.getPluginManager().registerEvents(oc, main);
					outlineContainer.put((Player)sender, oc);
					((Player)sender).getInventory().setItemInMainHand(new ItemStack(Material.WOODEN_HOE));
					return;
				}
				sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'�xistent pas!");
				return;
			}
			sender.sendMessage(ChatColor.RED+"Vous �tes d�j� en train d'enregistrer une outline!");
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setAftonCameraPackage");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		set1To6Argument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().setAftonCameraPackage((String)args[0], (String)args[1], (int)args[2])) {
				sender.sendMessage(ChatColor.GREEN+"Camera package set!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'�xistent pas, ou le chiffre n'est pas compris entre 1 et 6!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setIfDefaultGuardCamera");
		setMapFinderArgument(argument);
		setRoomFinderArgument(argument);
		setTrueOrFalseArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if((String)args[2]=="true" || (String)args[2]=="false") {
				if(main.getYamlReader().setIfDefaultGuardCamera((String)args[0], (String)args[1], (boolean)args[2])) {
					sender.sendMessage(ChatColor.GREEN+"Camera fait maintenant partie des cam�ras de base gardes!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map ou la salle n'�xistent pas!");
				}
			}else {
				sender.sendMessage(ChatColor.RED+"Il faut renseigner 'true'(oui) ou 'false'(non)!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setGuardRoomLocation");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(sender instanceof Player) {
				if(main.getYamlReader().setGuardRoomLocation((String)args[0], ((Player) sender).getLocation())) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Position de la t�leportation en salle de garde mise � jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map n'existe pas!");
				}
				return;
			}
			sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"room");
		setAutoCompleteArgument(argument,"setCameraBlockLocation");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(sender instanceof Player) {
				Block b = ((Player) sender).getTargetBlockExact(10, FluidCollisionMode.NEVER);
				if(b!=null) {
					if(main.getYamlReader().setGuardCameraBlockLocation((String)args[0], b.getLocation())) {
						sender.sendMessage(ChatColor.DARK_GREEN+"Block d'ouverture de cam�ra mis � jour!!");
					}else {
						sender.sendMessage(ChatColor.RED+"Cette map n'existe pas!");
					}
					sender.sendMessage(ChatColor.RED+"Regardez le block o� vous voulez posez l'emplacement!");
					return;
				}
			}
			sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
		}).register();
		argument.clear();
		//--------------------------------------------------------------------------------------
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"minimap");
		setAutoCompleteArgument(argument,"setGuardCameraBaseLocation");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(sender instanceof Player) {
				if(main.getYamlReader().setGuardCameraBaseLocation((String)args[0], ((Player) sender).getLocation())) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Position de la t�leportation en cam�ra de garde mise � jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map n'existe pas!");
				}
				return;
			}
			sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"minimap");
		setAutoCompleteArgument(argument,"setAftonCameraBaseLocation");
		setMapFinderArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(sender instanceof Player) {
				if(main.getYamlReader().setAftonCameraBaseLocation((String)args[0], ((Player) sender).getLocation())) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Position de la t�leportation en cam�ra d'afton mise � jour!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map n'existe pas!");
				}
				return;
			}
			sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"animatronic");
		setAutoCompleteArgument(argument,"scream");
		setAutoCompleteArgument(argument,"add");
		setMapFinderArgument(argument);
		setAnimatronicArgument(argument);
		setAnimationArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().addScreamAnimation((String)args[0], Animatronic.valueOf((String)args[1]), (String)args[2])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Scream ajout�!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, l'animatronic ou l'animation n'existent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"animatronic");
		setAutoCompleteArgument(argument,"scream");
		setAutoCompleteArgument(argument,"remove");
		setMapFinderArgument(argument);
		setAnimatronicArgument(argument);
		setScreamAnimationFinderFromAnimatronicArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(main.getYamlReader().removeScreamAnimation((String)args[0], Animatronic.valueOf((String)args[1]), (String)args[2])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"Scream retir�!");
			}else {
				sender.sendMessage(ChatColor.RED+"Cette map, l'animatronic ou l'animation n'existent pas!");
			}
		}).register();
		argument.clear();
		
		setAutoCompleteArgument(argument,"map");
		setAutoCompleteArgument(argument,"animatronic");
		setAutoCompleteArgument(argument,"setArmor");
		setMapFinderArgument(argument);
		setAnimatronicArgument(argument);
		setArmorPieceArgument(argument);
		new CommandAPICommand("fnafy").withArguments(argument).executes((sender,args)->{
			if(sender instanceof Player) {
				if(main.getYamlReader().setAnimatronicEquipement((String)args[0], Animatronic.valueOf((String)args[1]), ((Player) sender).getInventory().getItemInMainHand(), (String)args[2])) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Pi�ce d'armure ajout�e � "+(String)args[1]+"!");
				}else {
					sender.sendMessage(ChatColor.RED+"Cette map, l'animatronic ou la piece d'armure n'existent pas!");
				}
			}else {
				sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
			}
		}).register();
		argument.clear();
		main.getLogger().info("Mapper Commands registered");
	}
}
