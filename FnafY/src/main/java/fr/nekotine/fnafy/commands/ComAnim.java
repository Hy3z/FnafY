package fr.nekotine.fnafy.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.EulerAngle;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimEditor;
import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;

public class ComAnim {
	
	public final ArrayList<ASAnimEditor> editors = new ArrayList<ASAnimEditor>();
	private final FnafYMain main;
	private final File animFolder;
	private final HashMap<String,ASAnimation> asanims = new HashMap<String,ASAnimation>();
	
	public ComAnim(FnafYMain m) {
		main=m;
		animFolder = new File(main.getDataFolder(),"Animations");
		main.getLogger().info("Checking for Anim file");
		if (!animFolder.exists()) {
			animFolder.getParentFile().mkdirs();
			main.getLogger().info(ChatColor.GREEN+"Anim file created");
		}
	}
	
	private static final FilenameFilter asanimFileNameFilter = new FilenameFilter(){
        public boolean accept(File dir, String name) {
           String lowercaseName = name.toLowerCase();
           if (lowercaseName.endsWith(".asanim")) {
              return true;
           } else {
              return false;
           }
        }
     };
	
	public void registerAnimCommands() {
		main.getLogger().info("Registering Animatronic commands");
		//fnafy reload animas
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<String,Argument>();
	    arguments.put("reload", new LiteralArgument("reload"));
	    arguments.put("relanim", new LiteralArgument("animas"));
		new CommandAPICommand("fnafy").withArguments(arguments).executes((sender,args)->{
			main.getLogger().info("Reloading ArmorStand's Animations (.asanim files)");
			reloadASAnims();
		}).register();
		//anime open <animName>
		arguments.clear();
	    arguments.put("open", new LiteralArgument("open"));
	    arguments.put("animName", new StringArgument().overrideSuggestions((sender) -> {return (String[]) asanims.keySet().toArray();}));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimation anim = asanims.get((String)args[1]);
			if (anim!=null) {
				editors.add(new ASAnimEditor(main,player,anim,this));
			}else {
				player.sendMessage(ChatColor.RED+"Cette Animation n'existe pas. Essayez de recharger les animations.");
			}
		}).register();
		//anime close
		arguments.clear();
	    arguments.put("close", new LiteralArgument("close"));
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
	    arguments.put("exit", new LiteralArgument("exit"));
		new CommandAPICommand("anime").withArguments(arguments).executesPlayer((player,args)->{
			ASAnimEditor e=null;
			for (ASAnimEditor edt : editors) {
				if (edt.player.equals(player)) {
					e=edt;
				}
			}
			if (e!=null) {
				e.exit();
			}else {
				player.sendMessage(ChatColor.RED+"Vous n'êtes pas en mode édition.");
			}
		}).register();
		//anime save
		arguments.clear();
	    arguments.put("save", new LiteralArgument("save"));
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
	    arguments.put("stop", new LiteralArgument("stop"));
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
	    arguments.put("play", new LiteralArgument("play"));
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
			    arguments.put("pause", new LiteralArgument("pause"));
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
		//
		main.getLogger().info("Animatronics Commands registered");
	}
	
	public void reloadASAnims() {
		asanims.clear();
		File[] fileslist = animFolder.listFiles(asanimFileNameFilter);
		for (File file : fileslist) {
			YamlConfiguration config = getConfig(file);
			if (config!=null) {
				A
				asanims.put("",config.get(path))
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
	
	public void addEditor(Player p) {
		
	}
	
	public void removeEditor(Player p) {
		ASAnimEditor Editor=null;
		for (ASAnimEditor edt : editors) {
			if (edt.player.equals(p)) {Editor=edt;};
		}
		if (Editor!=null) {editors.remove(Editor);};
	}
	
	public void onPlayerDC(PlayerQuitEvent evt) {
		removeEditor(evt.getPlayer());
	}
	
	public String[] getAnimFiles() {
		ArrayList<String> list = new ArrayList<String>();
		for (String s : animFolder.list()) {
			if (s.endsWith(".anim")) {
				list.add(s.replace(".anim", ""));
			}
		}
		String[] finalList = new String[list.size()];
		for (int i=0;i<list.size();i++) {
			finalList[i]=list.get(i);
		}
		return finalList;
	}
	
	public boolean save(ASAnimation anim) {
		return false;
	}
	
}
