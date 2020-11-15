package fr.nekotine.fnafy.animation;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ASAnimEditor {
	
	public final Player player;
	private final ASAnimation anim;
	private ASAnimator animator;
	private ArmorStand as;
	private final Location baseLoc;
	private boolean valid=false;
	private int currentFrame=0;
	
	public ASAnimEditor(JavaPlugin main, Player p,ASAnimation an) {
		player=p;
		anim=an;
		baseLoc = p.getLocation().clone();
		Entity ent = baseLoc.getWorld().spawnEntity(baseLoc, EntityType.ARMOR_STAND);
		if (ent!=null) {
			try {
				as=(ArmorStand)ent;
				valid=true;
			}catch(ClassCastException e) {
				e.printStackTrace();
			}
		}
		if (valid) {
			animator=new ASAnimator(main,as);
			as.setGravity(false);
			an.play(currentFrame, as);
		}
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
	
}
