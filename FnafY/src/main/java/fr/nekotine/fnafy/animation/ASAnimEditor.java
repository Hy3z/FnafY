package fr.nekotine.fnafy.animation;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.commands.ComAnim;
import fr.nekotine.fnafy.utils.CustomEulerAngle;
import fr.nekotine.fnafy.utils.Posture;

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
			as.setBasePlate(false);
			as.setArms(true);
			an.play(currentFrame, as);
		}
		animManager=anmng;
	}
	
	public void setFrame(int frameNb) {
		if (valid) {
			currentFrame=frameNb;
			animator.setFrame(currentFrame);
			player.sendMessage(ChatColor.LIGHT_PURPLE+"L'image passe à: "+currentFrame);
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
		animManager.removeEditor(player);
	}
	
	public void save() {
		if (animManager.save(anim)) {player.sendMessage(ChatColor.DARK_GREEN+"Animation sauvegardée!");}else {
			player.sendMessage(ChatColor.RED+"Impossible de sauvegarder l'animation.");
		}
	}
	
	public void pause() {
		animator.pause();
	}
	
	public ASAnimOrder getFrameOrder() {
		return anim.orders.getOrDefault(currentFrame, new ASAnimOrder(nullPosture(anim.orders.get(0).pose.location.getWorld()), false));
	}
	
	public void setFrameOrder(ASAnimOrder newOrder) {
		anim.orders.put(currentFrame, newOrder);
	}
	
	private Posture nullPosture(World w) {
		return new Posture(CustomEulerAngle.zero(),CustomEulerAngle.zero(),CustomEulerAngle.zero(),
				CustomEulerAngle.zero(),CustomEulerAngle.zero(),CustomEulerAngle.zero(),new Location(w,0,0,0));
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
}
