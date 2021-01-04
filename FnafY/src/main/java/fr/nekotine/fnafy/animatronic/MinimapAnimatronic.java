package fr.nekotine.fnafy.animatronic;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.animation.ASAnimator;
import fr.nekotine.fnafy.events.AnimatronicEnterRoomEvent;
import fr.nekotine.fnafy.events.AnimatronicMoveAtDoorEvent;
import fr.nekotine.fnafy.events.GameStopEvent;

public class MinimapAnimatronic implements Listener{
	public final FnafYMain main;
	public final Animatronic anim;
	public final ASAnimator animator;
	public MinimapAnimatronic(FnafYMain main, Animatronic anim) {
		this.main=main;
		this.anim=anim;
		
		String roomName = main.getYamlReader().getAnimatronicBaseRoomName(main.getMapName(), anim);
		List<String> animations = main.getYamlReader().getRoomMinimapAnimation(main.getMapName(), roomName, anim);
		ASAnimation animation =  main.getAnimManager().getAsanims().get(animations.get((int)Math.random()*animations.size()));
		Location animLoc = animation.getFrameOrder(0).pose.location;
		
		ArmorStand as = (ArmorStand) animLoc.getWorld().spawnEntity(animLoc, EntityType.ARMOR_STAND);
		as.getEquipment().setHelmet(main.getYamlReader().getAnimatronicEquipement(main.getMapName(), anim, "helmet"));
		as.getEquipment().setChestplate(main.getYamlReader().getAnimatronicEquipement(main.getMapName(), anim, "chestplate"));
		as.getEquipment().setLeggings(main.getYamlReader().getAnimatronicEquipement(main.getMapName(), anim, "leggings"));
		as.getEquipment().setBoots(main.getYamlReader().getAnimatronicEquipement(main.getMapName(), anim, "boots"));
		
		this.animator=new ASAnimator(main, as);
		animator.setLooping(true);
		playAnimation(animation);
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onGameEnd(GameStopEvent e) {
		HandlerList.unregisterAll(this);
		animator.as.remove();
	}
	private void playAnimation(ASAnimation animation) {
		animator.play(animation);
		if(animation.getAnimSize()==1) {
			animator.pause();
		}
	}
	@EventHandler
	public void animToDoor(AnimatronicMoveAtDoorEvent e) {
		List<ASAnimation> animations = e.getDoor().getMinimapDoorAnimationsToRoom(e.getGoingTo(), anim);
		playAnimation(animations.get((int)Math.random()*animations.size()));
	}
	@EventHandler
	public void animToRoom(AnimatronicEnterRoomEvent e) {
		List<ASAnimation> animations = e.getRoom().getMinimapAnimations(anim);
		playAnimation(animations.get((int)Math.random()*animations.size()));
	}
}
