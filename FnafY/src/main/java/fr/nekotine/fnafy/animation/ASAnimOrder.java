package fr.nekotine.fnafy.animation;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.nekotine.fnafy.utils.Posture;

public class ASAnimOrder implements ConfigurationSerializable{
	
	public final Posture pose;
	public boolean relative;
	
	public ASAnimOrder(Posture pose,boolean relative) {
		this.pose=pose;
		this.relative=relative;
	}
	
	public void execute(ArmorStand as) {
		Location newLoc;
		if (relative) {
			double angle = Math.toRadians(as.getLocation().getYaw()+pose.location.getYaw());
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);
			newLoc = as.getLocation().clone().add(pose.location.getX()*cos+pose.location.getZ()*sin,pose.location.getY(),pose.location.getX()*sin+pose.location.getZ()*cos);
		}else {
			newLoc=pose.location.clone();
		}
		as.teleport(newLoc,TeleportCause.PLUGIN);
		as.setBodyPose(pose.body);
		as.setLeftArmPose(pose.leftArm);
		as.setRightArmPose(pose.rightArm);
		as.setLeftLegPose(pose.leftLeg);
		as.setRightLegPose(pose.rightLeg);
		as.setHeadPose(pose.head);
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		serialized.put("pose",pose);
		serialized.put("relative", relative);
		return null;
	}
	
	public static ASAnimOrder deserialize(Map<String, Object> args) {
		return new ASAnimOrder((Posture)args.get("pose"),(boolean)args.get("relative"));
	}
	
}
