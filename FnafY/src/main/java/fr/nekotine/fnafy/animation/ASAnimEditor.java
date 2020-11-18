package fr.nekotine.fnafy.animation;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.commands.ComAnim;

public class ASAnimEditor {
	
	public final Player player;
	private final ASAnimation anim;
	private ASAnimator animator;
	private ArmorStand as;
	private final Location baseLoc;
	private boolean valid=false;
	private int currentFrame=0;
	private final ComAnim animManager;
	
	public ASAnimEditor(FnafYMain main, Player p,ASAnimation an,ComAnim anmng) {
		player=p;
		anim=an;
		baseLoc = p.getLocation().clone();
		Entity ent = baseLoc.getWorld().spawnEntity(baseLoc, EntityType.ARMOR_STAND);
		if (ent!=null) {
			try {
				as=(ArmorStand)ent;
				valid=true;
			}catch(ClassCastException e) {
				p.sendMessage(ChatColor.RED+"Impossible de faire apparaitre le manequin pour l'édition");
				e.printStackTrace();
			}
		}
		if (valid) {
			p.sendMessage(ChatColor.GREEN+"Vous passez en mode édition.");
			animator=new ASAnimator(main,as);
			as.setGravity(false);
			an.play(currentFrame, as);
		}
		animManager=anmng;
	}
	
	public void setFrame(int frameNb) {
		if (valid) {
			currentFrame=frameNb;
			animator.setFrame(currentFrame);
		}
	}
	
	public void play() {
		if (valid) {
			animator.play(anim);
		}
	}
	
	public void stop() {
		if (valid) {
			animator.stop();
		}
	}
	
	public void close() {
		save();
		exit();
	}
	
	public void exit() {
		animator.stop();
		as.remove();
		player.sendMessage(ChatColor.GREEN+"Vous quittez le mode édition");
	}
	
	public void save() {
		if (animManager.save(anim)) {player.sendMessage(ChatColor.DARK_GREEN+"Animation sauvegardée!");}
	}
	
	public void pause() {
		animator.pause();
	}
	
}
