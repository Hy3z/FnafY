package fr.nekotine.fnafy.animation;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.nekotine.fnafy.commands.DoubleAddOrSet;
import fr.nekotine.fnafy.utils.CustomEulerAngle;
import fr.nekotine.fnafy.utils.Posture;
@SerializableAs("ASAnimOrder")
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
			newLoc=pose.location;
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
		return serialized;
	}
	
	public static ASAnimOrder deserialize(Map<String, Object> args) {
		return new ASAnimOrder((Posture)args.get("pose"),(boolean) args.get("relative"));
	}

	public void modify(String key, EnumSet<Axis> ax, DoubleAddOrSet value) {
		boolean rel=value.isRelative();
		double val=value.getValue();
		CustomEulerAngle angle=CustomEulerAngle.zero();
		switch (key) {
		case "body":
			if (ax.contains(Axis.X)) {
				pose.body=pose.body.setX(rel?pose.body.getX()+val:val);
			}
			if (ax.contains(Axis.Y)) {
				pose.body=pose.body.setY(rel?pose.body.getY()+val:val);
			}
			if (ax.contains(Axis.Z)) {
				pose.body=pose.body.setZ(rel?pose.body.getZ()+val:val);
			}
			return;
		case "head":
			if (ax.contains(Axis.X)) {
				pose.head=pose.head.setX(rel?pose.head.getX()+val:val);
			}
			if (ax.contains(Axis.Y)) {
				pose.head=pose.head.setY(rel?pose.head.getY()+val:val);
			}
			if (ax.contains(Axis.Z)) {
				pose.head=pose.head.setZ(rel?pose.head.getZ()+val:val);
			}
			return;
		case "leftArm":
			if (ax.contains(Axis.X)) {
				pose.leftArm=pose.leftArm.setX(rel?pose.leftArm.getX()+val:val);
			}
			if (ax.contains(Axis.Y)) {
				pose.leftArm=pose.leftArm.setY(rel?pose.leftArm.getY()+val:val);
			}
			if (ax.contains(Axis.Z)) {
				pose.leftArm=pose.leftArm.setZ(rel?pose.leftArm.getZ()+val:val);
			}
			return;
		case "rightArm":
			if (ax.contains(Axis.X)) {
				pose.rightArm=pose.rightArm.setX(rel?pose.rightArm.getX()+val:val);
			}
			if (ax.contains(Axis.Y)) {
				pose.rightArm=pose.rightArm.setY(rel?pose.rightArm.getY()+val:val);
			}
			if (ax.contains(Axis.Z)) {
				pose.rightArm=pose.rightArm.setZ(rel?pose.rightArm.getZ()+val:val);
			}
			return;
		case "leftLeg":
			if (ax.contains(Axis.X)) {
				pose.leftLeg=pose.leftLeg.setX(rel?pose.leftLeg.getX()+val:val);
			}
			if (ax.contains(Axis.Y)) {
				pose.leftLeg=pose.leftLeg.setY(rel?pose.leftLeg.getY()+val:val);
			}
			if (ax.contains(Axis.Z)) {
				pose.leftLeg=pose.leftLeg.setZ(rel?pose.leftLeg.getZ()+val:val);
			}
			return;
		case "rightLeg":
			if (ax.contains(Axis.X)) {
				pose.rightLeg=pose.rightLeg.setX(rel?pose.rightLeg.getX()+val:val);
			}
			if (ax.contains(Axis.Y)) {
				pose.rightLeg=pose.rightLeg.setY(rel?pose.rightLeg.getY()+val:val);
			}
			if (ax.contains(Axis.Z)) {
				pose.rightLeg=pose.rightLeg.setZ(rel?pose.rightLeg.getZ()+val:val);
			}
			return;
		case "location":
			Location loc=pose.location;
			if (ax.contains(Axis.X)) {
				loc.setX(rel?loc.getX()+val:val);
			}
			if (ax.contains(Axis.Y)) {
				loc.setY(rel?loc.getY()+val:val);
			}
			if (ax.contains(Axis.Z)) {
				loc.setZ(rel?loc.getZ()+val:val);
			}
			return;
		case "rotation":
			Location loc1=pose.location;
			if (ax.contains(Axis.X)) {
				loc1.setPitch((float)(rel?loc1.getPitch()+val:val));
			}
			if (ax.contains(Axis.Y)) {
				loc1.setYaw((float) (rel?loc1.getYaw()+val:val));
			}
			return;
		default:
			return;
		}
	}
	
}
