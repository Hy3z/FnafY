package fr.nekotine.fnafy.animation;

import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ASAnimator {
	
	private ArmorStand as;
	private final JavaPlugin plugin;
	private int currentFrame=0;
	private ASAnimation currentAnim=null;
	private BukkitRunnable runnable;
	private boolean running=false;
	private boolean looping=false;
	
	public ASAnimator(JavaPlugin pluginRef,ArmorStand a) {
		as=a;
		plugin=pluginRef;
	}
	
	public void play(ASAnimation anim) {
		currentFrame=0;
		currentAnim=anim;
		resume();
	}
	
	public void setArmorStand(ArmorStand a) {
		as=a;
	}
	
	public void resume() {
		if (currentAnim!=null && running==false) {
			runnable = new BukkitRunnable() {
				@Override
				public void run() {
					currentAnim.play(currentFrame, as);
					currentFrame++;
					if (currentFrame>currentAnim.getAnimSize()) {
						if (looping) {
							currentFrame=0;
						}else {
							stop();
						}
					}
				}
			};
			runnable.runTaskTimer(plugin, 1, 1);
			running=true;
		}
	}
	
	public void pause() {
		runnable.cancel();
		running=false;
	}
	
	public void setLooping(boolean loop) {
		looping=loop;
	}
	
	public void stop() {
		if (running) {
			runnable.cancel();
			currentAnim=null;
			running=false;
		}
	}
	
	public void setFrame(int index) {
		currentFrame=index;
	}
	
}
