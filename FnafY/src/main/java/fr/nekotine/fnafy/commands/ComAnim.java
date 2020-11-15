package fr.nekotine.fnafy.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.EulerAngle;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimEditor;
import fr.nekotine.fnafy.animation.ASAnimOrder;
import fr.nekotine.fnafy.animation.ASAnimation;

public class ComAnim {
	
	public final ArrayList<ASAnimEditor> editors = new ArrayList<ASAnimEditor>();
	
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
	
	public static void registerAnimCommands(FnafYMain main) {
		main.getLogger().info("Registering Animatronic commands");
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<String,Argument>();
	    arguments.put("reload", new LiteralArgument("reload"));
	    arguments.put("relanim", new LiteralArgument("animas"));
		new CommandAPICommand("fnafy").withArguments(arguments).executes((sender,args)->{
			main.getLogger().info("Reloading ArmorStand's Animations (.asanim files)");
			ComAnim.reloadASAnims(main);
		}).register();
		main.getLogger().info("Animatronics Commands registered");
		//----
	}
	
	public static void reloadASAnims(FnafYMain main) {
		main.asanims.clear();
		File[] fileslist = main.animFile.listFiles(asanimFileNameFilter);
		for (File file : fileslist) {
			try {
				List<String> content = Files.readAllLines(file.toPath());
				ASAnimation anim = new ASAnimation();
				for (String line : content) {
					String[] parts = line.split(";");
					if (parts.length==23) {
						EulerAngle body = new EulerAngle(Double.parseDouble(parts[2]),Double.parseDouble(parts[3]),Double.parseDouble(parts[4]));
						EulerAngle leftArm = new EulerAngle(Double.parseDouble(parts[5]),Double.parseDouble(parts[6]),Double.parseDouble(parts[7]));
						EulerAngle rightArm = new EulerAngle(Double.parseDouble(parts[8]),Double.parseDouble(parts[9]),Double.parseDouble(parts[10]));
						EulerAngle leftLeg = new EulerAngle(Double.parseDouble(parts[11]),Double.parseDouble(parts[12]),Double.parseDouble(parts[13]));
						EulerAngle rightLeg = new EulerAngle(Double.parseDouble(parts[14]),Double.parseDouble(parts[15]),Double.parseDouble(parts[16]));
						EulerAngle head = new EulerAngle(Double.parseDouble(parts[17]),Double.parseDouble(parts[18]),Double.parseDouble(parts[19]));
						anim.setOrder(Integer.parseInt(parts[1]), new ASAnimOrder(body, leftArm, rightArm, leftLeg, rightLeg, head,Double.parseDouble(parts[20]),Double.parseDouble(parts[21]),Double.parseDouble(parts[22])));
					}else {
						main.getLogger().warning("The line \""+line+"\" is invalid");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		main.getLogger().info(ChatColor.GREEN+"ArmorStand's Animations reloaded.");
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
	
}
