package fr.nekotine.fnafy.animation;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.nekotine.fnafy.utils.Posture;

public class ASAnimOrder {
	
	public final Posture pose;
	public boolean relative;
	
	public ASAnimOrder(Posture pose,boolean relative) {
		this.pose=pose;
		this.relative=relative;
	}
	
	public void execute(ArmorStand as) {
		double angle = Math.toRadians(as.getLocation().getYaw());
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		Location newLoc = as.getLocation().clone()/*.add(x*cos+z*sin,y,x*sin+z*cos*/;
		as.teleport(newLoc,TeleportCause.PLUGIN);
		as.setBodyPose(pose.body);
		as.setLeftArmPose(pose.leftArm);
		as.setRightArmPose(pose.rightArm);
		as.setLeftLegPose(pose.leftLeg);
		as.setRightLegPose(pose.rightLeg);
		as.setHeadPose(pose.head);
	}
	
}
