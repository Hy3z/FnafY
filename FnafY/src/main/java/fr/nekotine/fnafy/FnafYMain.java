package fr.nekotine.fnafy;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.animation.ASOrderMove;
import fr.nekotine.fnafy.animation.ASOrderPose;
import fr.nekotine.fnafy.events.EventListener;

public class FnafYMain extends JavaPlugin {
	
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
	
	private final File animFile = new File(getDataFolder(),"Animations");
	private final HashMap<String,ASAnimation> asanims = new HashMap<String,ASAnimation>();
	
	public EventListener eListener;
	
	public void onEnable() {
		super.onEnable();
		eListener = new EventListener();
		Bukkit.getPluginManager().registerEvents(eListener, this);
		getLogger().info("Checking for Anim file");
		if (!animFile.exists()) {
			animFile.getParentFile().mkdirs();
			getLogger().info(ChatColor.GREEN+"Anim file created");
		 }
		//---COMMANDS---//
		getLogger().info("Registering commands");
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<String,Argument>();
	    arguments.put("reload", new LiteralArgument("reload"));
	    arguments.put("relanim", new LiteralArgument("animas"));
		new CommandAPICommand("fnafy").withArguments(arguments).executes((sender,args)->{
			getLogger().info("Reloading ArmorStand's Animations (.asanim files)");
			reloadASAnims();
		}).register();
		getLogger().info("Commands registered");
		//----
		getLogger().info("Loading ArmorStand's Animations (.asanim files)");
		reloadASAnims();
	}
	
	public ASAnimation getASAnim(String animName) {
		return asanims.get(animName);
	}
	
	private void reloadASAnims() {
		asanims.clear();
		File[] fileslist = animFile.listFiles(asanimFileNameFilter);
		for (File file : fileslist) {
			try {
				List<String> content = Files.readAllLines(file.toPath());
				ASAnimation anim = new ASAnimation();
				for (String line : content) {
					String[] parts = line.split(";");
					if (parts.length==20 && parts[0]=="pose") {
						EulerAngle body = new EulerAngle(Double.parseDouble(parts[2]),Double.parseDouble(parts[3]),Double.parseDouble(parts[4]));
						EulerAngle leftArm = new EulerAngle(Double.parseDouble(parts[5]),Double.parseDouble(parts[6]),Double.parseDouble(parts[7]));
						EulerAngle rightArm = new EulerAngle(Double.parseDouble(parts[8]),Double.parseDouble(parts[9]),Double.parseDouble(parts[10]));
						EulerAngle leftLeg = new EulerAngle(Double.parseDouble(parts[11]),Double.parseDouble(parts[12]),Double.parseDouble(parts[13]));
						EulerAngle rightLeg = new EulerAngle(Double.parseDouble(parts[14]),Double.parseDouble(parts[15]),Double.parseDouble(parts[16]));
						EulerAngle head = new EulerAngle(Double.parseDouble(parts[17]),Double.parseDouble(parts[18]),Double.parseDouble(parts[19]));
						anim.addOrder(Integer.parseInt(parts[1]), new ASOrderPose(body, leftArm, rightArm, leftLeg, rightLeg, head));
					}else if (parts.length==4 && parts[0]=="move") {
						anim.addOrder(Integer.parseInt(parts[1]), new ASOrderMove(Double.parseDouble(parts[2]),Double.parseDouble(parts[3]),Double.parseDouble(parts[4])));
					}else {
						getLogger().warning("The line \""+line+"\" is invalid");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		getLogger().info(ChatColor.GREEN+"ArmorStand's Animations reloaded.");
	}
}
