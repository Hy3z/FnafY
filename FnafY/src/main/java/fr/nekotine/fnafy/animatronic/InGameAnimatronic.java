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
import fr.nekotine.fnafy.events.GameTickEvent;

public class InGameAnimatronic implements Listener{
	public final FnafYMain main;
	public final Animatronic anim;
	public final ASAnimator animator;
	public String currentRoom;
	
	private String movingTo;
	private int TICKDELAYCROSSROOM = 100;
	private int currentDelay = 0;
	private boolean isAtDoor = false;
	public InGameAnimatronic(FnafYMain main, Animatronic anim) {
		this.main=main;
		this.anim=anim;
		
		String roomName = main.getYamlReader().getAnimatronicBaseRoomName(main.getMapName(), anim);
		this.currentRoom = roomName;
		
		List<String> animations = main.getYamlReader().getRoomAnimation(main.getMapName(), roomName, anim);
		ASAnimation animation =  main.getAnimManager().getAsanims().get(animations.get((int)Math.random()*animations.size()));
		Location animLoc = animation.getFrameOrder(0).pose.location;
		
		this.animator=new ASAnimator(main, (ArmorStand) animLoc.getWorld().spawnEntity(animLoc, EntityType.ARMOR_STAND));
		animator.setLooping(true);
		playAnimation(animation);
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void gameTick(GameTickEvent e) {
		if(isAtDoor) {
			currentDelay++;
			if(currentDelay==TICKDELAYCROSSROOM) {
				isAtDoor=false;
				currentDelay=0;
				currentRoom=movingTo;
				Bukkit.getPluginManager().callEvent(new AnimatronicEnterRoomEvent(main.doorRoomContainer.getRoom(currentRoom), anim));
				List<ASAnimation> animations = main.doorRoomContainer.getRoom(currentRoom).getAnimations(anim);
				playAnimation(animations.get((int)Math.random()*animations.size()));
			}
		}
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
		isAtDoor=true;
		movingTo = e.getGoingTo().getRoomName();
		List<ASAnimation> animations = e.getDoor().getDoorAnimationsToRoom(e.getGoingTo(), anim);
		playAnimation(animations.get((int)Math.random()*animations.size()));
	}
	
}
