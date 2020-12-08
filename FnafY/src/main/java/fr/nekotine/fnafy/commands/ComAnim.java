package fr.nekotine.fnafy.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.AxisArgument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimEditor;
import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.utils.CustomEulerAngle;
import fr.nekotine.fnafy.utils.Posture;

public class ComAnim {
	
	private static final String[] editablesValues = {"body","head","leftArm","rightArm","leftLeg","rightLeg","location","rotation"};
	public final ArrayList<ASAnimEditor> editors = new ArrayList<ASAnimEditor>();
	private final FnafYMain main;
	private final File animFolder;
	private final HashMap<String,ASAnimation> asanims = new HashMap<String,ASAnimation>();
	private final Predicate<CommandSender> isInEdition = (CommandSender sender)->{
		if (sender instanceof Player) {
			Player p = (Player)sender;
			for (ASAnimEditor edt : editors) {
				if (edt.player==p) {
					return true;
				}
			}
		}
		return false;
	};
	
	public ComAnim(FnafYMain m) {
		main=m;
		animFolder = new File(main.getDataFolder(),"Animations");
		main.getLogger().info("Checking for Anim file");
		if (!animFolder.exists()) {
			animFolder.mkdir();
			main.getLogger().info(ChatColor.GREEN+"Anim file created");
		}
	}
	
	private static final FilenameFilter ymlFileNameFilter = new FilenameFilter(){
        public boolean accept(File dir, String name) {
           String lowercaseName = name.toLowerCase();
           if (lowercaseName.endsWith(".yml")) {
              return true;
           } else {
              return false;
           }
        }
     };
	
	@SuppressWarnings("unchecked")
	public void registerAnimCommands() {
		main.getLogger().info("Registering Animatronic commands");
		//fnafy reload animas
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<String,Argument>();
	    arguments.put("reload", new LiteralArgument("reload"));
	    arguments.put("relanim", new LiteralArgument("animas"));
		new CommandAPICommand("fnafy").withArguments(arguments).executes((sender,args)->{
			sender.sendMessage(ChatColor.LIGHT_PURPLE+"Rechargement des animations de fnafy...");
			reloadASAnims();
			sender.sendMessage(ChatColor.GREEN+"Rechargement terminé!");
		}).register();
		//anime open <animName>
		arguments.clear();
	    arguments.put("open", new LiteralArgument("open").withRequirement(isInEdition.negate()));
	    arguments.put("animName", new StringArgument().overrideSuggestions((sender) -> {Set<String> temp = asanims.keySet();return temp.toArray(new String[temp.size()]);}));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimation anim = asanims.get((String)args[0]);
			if (anim!=null) {
				editors.add(new ASAnimEditor(main,player,anim,this));
				CommandAPI.updateRequirements(player);
			}else {
				player.sendMessage(ChatColor.RED+"Cette Animation n'existe pas. Essayez de recharger les animations.");
			}
		}).register();
		//anime close
		arguments.clear();
	    arguments.put("close", new LiteralArgument("close").withRequirement(isInEdition));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				e.close();
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime exit
		arguments.clear();
	    arguments.put("exit", new LiteralArgument("exit").withRequirement(isInEdition));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				removeEditor(e);
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime save
		arguments.clear();
	    arguments.put("save", new LiteralArgument("save").withRequirement(isInEdition));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				e.save();
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime stop
		arguments.clear();
	    arguments.put("stop", new LiteralArgument("stop").withRequirement(isInEdition));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				e.stop();
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime play
		arguments.clear();
	    arguments.put("play", new LiteralArgument("play").withRequirement(isInEdition));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				e.play();
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime pause
		arguments.clear();
	    arguments.put("pause", new LiteralArgument("pause").withRequirement(isInEdition));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				e.pause();
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime edit <editableValue> <axis> <add/setValue>
		arguments.clear();
		arguments.put("edit", new LiteralArgument("edit").withRequirement(isInEdition));
	    arguments.put("editableValue", BodyPartArgument().withRequirement(isInEdition).overrideSuggestions(editablesValues));
	    arguments.put("axis", new AxisArgument().withRequirement(isInEdition));
	    arguments.put("add/setValue",DoubleAddSetValue().withRequirement(isInEdition));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				e.setFrameOrder(ASAnimOrder.modified(e.getFrameOrder(), (String)args[0],(EnumSet<Axis>)args[1], (DoubleAddOrSet)args[2]));
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime setFrame <IntegerAddOrSet>
		arguments.clear();
	    arguments.put("setframe", new LiteralArgument("setframe").withRequirement(isInEdition));
	    arguments.put("add/setValue",IntAddSetValue());
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				IntegerAddOrSet a = (IntegerAddOrSet) args[0];
				e.setFrame(a.isRelative()?e.getCurrentFrame()+a.getValue():a.getValue());
				e.refreshPose();
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime create <name>
		arguments.clear();
	    arguments.put("create", new LiteralArgument("create"));
	    arguments.put("name",new StringArgument());
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			String name = (String)args[0];
			if (asanims.containsKey(name)) {
				player.sendMessage(ChatColor.RED+"Une animation de ce nom existe déja.");
			}else if (Arrays.asList(getAnimFiles()).contains(name)) {
				player.sendMessage(ChatColor.RED+"Une animation de ce nom existe déja mais n'est pas chargée.");
			}else {
				ASAnimation anim=new ASAnimation();
				anim.setName(name);
				anim.setOrder(0, new ASAnimOrder(
						new Posture(CustomEulerAngle.zero(),CustomEulerAngle.zero(),CustomEulerAngle.zero(),
								CustomEulerAngle.zero(),CustomEulerAngle.zero(),CustomEulerAngle.zero(),new Location(player.getWorld(),0,0,0,0,0)),true));
				asanims.put(name, anim);
				save(anim);
				player.sendMessage(ChatColor.DARK_GREEN+"L'animation a bien été crée et enregistrée.");
			}
		}).register();
		//anime loop <boolean>
		arguments.clear();
	    arguments.put("loop", new LiteralArgument("loop").withRequirement(isInEdition));
	    arguments.put("value",new BooleanArgument());
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				boolean loop = (boolean) args[0];
				e.setLoop(loop);
				player.sendMessage(ChatColor.LIGHT_PURPLE+"Le bouclage de l'aperçu de l'animation "+(loop?"est bouclé":"est non bouclé")+".");
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime setrelative <boolean>
		arguments.clear();
	    arguments.put("setrelative", new LiteralArgument("setrelative").withRequirement(isInEdition));
	    arguments.put("value",new BooleanArgument());
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				boolean rel = (boolean) args[0];
				e.getFrameOrder().relative=rel;
				player.sendMessage(ChatColor.LIGHT_PURPLE+"La position est devenue "+(rel?"relative":"non relative")+".");
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//
		main.getLogger().info("Animatronics Commands registered");
	}
	
	public void reloadASAnims() {
		main.getLogger().info("Reloading ArmorStand's Animations (.yml files)");
		asanims.clear();
		File[] fileslist = animFolder.listFiles(ymlFileNameFilter);
		for (File file : fileslist) {
			YamlConfiguration config = getConfig(file);
			if (config!=null) {
				asanims.put(file.getName().replace(".yml", ""),(ASAnimation)config.get("animation"));
			}else {
				main.getLogger().warning("The file "+file.getName()+" failed to load.");
			}
		}
		main.getLogger().info(ChatColor.GREEN+"ArmorStand's Animations reloaded.");
	}
	
	private YamlConfiguration getConfig(File file) {
		if(file.exists()) {
			YamlConfiguration yamlConfig = new YamlConfiguration();
			try {
				yamlConfig.load(file);
				return yamlConfig;
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void removeEditor(Player p) {
		ASAnimEditor Editor=null;
		for (ASAnimEditor edt : editors) {
			if (edt.player.equals(p)) {Editor=edt;};
		}
		if (Editor!=null) {
			Editor.exit();
			editors.remove(Editor);
			CommandAPI.updateRequirements(p);
		};
	}
	
	public void removeEditor(ASAnimEditor edt) {
		edt.exit();
		editors.remove(edt);
		CommandAPI.updateRequirements(edt.player);
		edt.player.sendMessage(ChatColor.GREEN+"Vous quittez le mode édition");
	}
	
	public void disable() {
		for (ASAnimEditor edt : editors) {
			removeEditor(edt);
		}
	}
	
	public void onPlayerDC(PlayerQuitEvent evt) {
		removeEditor(evt.getPlayer());
	}
	
	public String[] getAnimFiles() {
		ArrayList<String> list = new ArrayList<String>();
		for (String s : animFolder.list()) {
			if (s.endsWith(".yml")) {
				list.add(s.replace(".yml", ""));
			}
		}
		String[] finalList = new String[list.size()];
		for (int i=0;i<list.size();i++) {
			finalList[i]=list.get(i);
		}
		return finalList;
	}
	
	public boolean save(ASAnimation anim) {
		File f = new File(animFolder, anim.getName()+".yml");
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (f.exists()) {
			YamlConfiguration cfg = getConfig(f);
			cfg.set("animation", anim);
			try {
				cfg.save(f);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	private Argument DoubleAddSetValue() {
		return new CustomArgument<DoubleAddOrSet>((String input)->{
			boolean rel=false;
			double value=0.0;
			if (input.length()>0) {
				if (input.startsWith("+")) {
					rel=true;
					input.replace("+","");
				}
				try {
				value = Double.parseDouble(input);
				}catch(NullPointerException|NumberFormatException e) {
					throw new CustomArgumentException(new MessageBuilder("Invalid double: ").appendArgInput().appendHere());
				}
			}
			return new DoubleAddOrSet(value, rel);
		});
	}
	
	private Argument IntAddSetValue() {
		return new CustomArgument<IntegerAddOrSet>((String input)->{
			boolean rel=false;
			int value=0;
			if (input.length()>0) {
				if (input.startsWith("+")) {
					rel=true;
					input.replace("+","");
				}
				try {
				value = Integer.parseInt(input);
				}catch(NullPointerException|NumberFormatException e) {
					throw new CustomArgumentException(new MessageBuilder("Invalid int: ").appendArgInput().appendHere());
				}
			}
			return new IntegerAddOrSet(value, rel);
		});
	}
	
	private Argument BodyPartArgument() {
		return new CustomArgument<String>((String input)->{
			if (ArrayUtils.contains(editablesValues, input)) {
				return input;
			}else {
				throw new CustomArgumentException(new MessageBuilder("Invalide value part: ").appendArgInput().appendHere());
			}
		});
	}
	
	public HashMap<String,ASAnimation> getAsanims(){
		return asanims;
	}
}
