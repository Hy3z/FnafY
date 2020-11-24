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
import fr.nekotine.fnafy.utils.Posture;
@SerializableAs("ASAnimOrder")
public class ASAnimOrder implements ConfigurationSerializable{
	
	public final Posture pose;
	public boolean relative;
	
	public static ASAnimOrder modified(ASAnimOrder order,String change,EnumSet<Axis> axs, DoubleAddOrSet value) {
		ASAnimOrder newO;
		switch (change) {
		case "body":
			if (value.isRelative()) {
				newO = new ASAnimOrder(new Posture(order.pose.body.add(axs.contains(Axis.X)?value.getValue():0,axs.contains(Axis.Y)?value.getValue():0,axs.contains(Axis.Z)?value.getValue():0),
						order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,order.pose.location.clone()),order.relative);
			}else {
				newO = new ASAnimOrder(new Posture(order.pose.body.setX(axs.contains(Axis.X)?value.getValue():order.pose.body.getX())
						.setY(axs.contains(Axis.Y)?value.getValue():order.pose.body.getY()).setZ(axs.contains(Axis.Z)?value.getValue():order.pose.body.getZ()),
						order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,order.pose.location.clone()),order.relative);
			}
			break;
		case "head":
			if (value.isRelative()) {
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,
						order.pose.head.add(axs.contains(Axis.X)?value.getValue():0,axs.contains(Axis.Y)?value.getValue():0,axs.contains(Axis.Z)?value.getValue():0)
						,order.pose.location.clone()),order.relative);
			}else {
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,
						order.pose.head.setX(axs.contains(Axis.X)?value.getValue():order.pose.body.getX()).setY(axs.contains(Axis.Y)?value.getValue():order.pose.body.getY())
						.setZ(axs.contains(Axis.Z)?value.getValue():order.pose.body.getZ()),order.pose.location.clone()),order.relative);
			}
			break;
		case "leftArm":
			if (value.isRelative()) {
				newO = new ASAnimOrder(new Posture(order.pose.body,
						order.pose.leftArm.add(axs.contains(Axis.X)?value.getValue():0,axs.contains(Axis.Y)?value.getValue():0,axs.contains(Axis.Z)?value.getValue():0)
						,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,order.pose.location.clone()),order.relative);
			}else {
				newO = new ASAnimOrder(new Posture(order.pose.body,
						order.pose.leftArm.setX(axs.contains(Axis.X)?value.getValue():order.pose.body.getX()).setY(axs.contains(Axis.Y)?value.getValue():order.pose.body.getY())
						.setZ(axs.contains(Axis.Z)?value.getValue():order.pose.body.getZ())
						,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,order.pose.location.clone()),order.relative);
			}
			break;
		case "rightArm":
			if (value.isRelative()) {
				newO = new ASAnimOrder(new Posture(order.pose.body,
						order.pose.leftArm,order.pose.rightArm.add(axs.contains(Axis.X)?value.getValue():0,axs.contains(Axis.Y)?value.getValue():0,axs.contains(Axis.Z)?value.getValue():0)
						,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,order.pose.location.clone()),order.relative);
			}else {
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,
						order.pose.rightArm.setX(axs.contains(Axis.X)?value.getValue():order.pose.body.getX()).setY(axs.contains(Axis.Y)?value.getValue():order.pose.body.getY())
						.setZ(axs.contains(Axis.Z)?value.getValue():order.pose.body.getZ())
						,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,order.pose.location.clone()),order.relative);
			}
			break;
		case "leftLeg":
			if (value.isRelative()) {
				newO = new ASAnimOrder(new Posture(order.pose.body,
						order.pose.leftArm,order.pose.rightArm,
						order.pose.leftLeg.add(axs.contains(Axis.X)?value.getValue():0,axs.contains(Axis.Y)?value.getValue():0,axs.contains(Axis.Z)?value.getValue():0)
						,order.pose.rightLeg,order.pose.head,order.pose.location.clone()),order.relative);
			}else {
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,order.pose.rightArm,
						order.pose.leftLeg.setX(axs.contains(Axis.X)?value.getValue():order.pose.body.getX()).setY(axs.contains(Axis.Y)?value.getValue():order.pose.body.getY())
						.setZ(axs.contains(Axis.Z)?value.getValue():order.pose.body.getZ())
						,order.pose.rightLeg,order.pose.head,order.pose.location.clone()),order.relative);
			}
			break;
		case "rightLeg":
			if (value.isRelative()) {
				newO = new ASAnimOrder(new Posture(order.pose.body,
						order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg
						,order.pose.rightLeg.add(axs.contains(Axis.X)?value.getValue():0,axs.contains(Axis.Y)?value.getValue():0,axs.contains(Axis.Z)?value.getValue():0)
						,order.pose.head,order.pose.location.clone()),order.relative);
			}else {
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,
						order.pose.rightLeg.setX(axs.contains(Axis.X)?value.getValue():order.pose.body.getX()).setY(axs.contains(Axis.Y)?value.getValue():order.pose.body.getY())
						.setZ(axs.contains(Axis.Z)?value.getValue():order.pose.body.getZ()),order.pose.head,order.pose.location.clone()),order.relative);
			}
			break;
		case "location":
			if (value.isRelative()) {
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,
						order.pose.location.clone().add(axs.contains(Axis.X)?value.getValue():0,axs.contains(Axis.Y)?value.getValue():0,axs.contains(Axis.Z)?value.getValue():0))
						,order.relative);
			}else {
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,
						order.pose.location.clone().add(axs.contains(Axis.X)?value.getValue():order.pose.location.getX(),axs.contains(Axis.Y)?value.getValue():order.pose.location.getY()
								,axs.contains(Axis.Z)?value.getValue():order.pose.location.getZ())),order.relative);
			}
			break;
		case "rotation":
			if (value.isRelative()) {
				Location loc=order.pose.location.clone();
				if (axs.contains(Axis.X)) {loc.setPitch(loc.getPitch()+(float)value.getValue());};
				if (axs.contains(Axis.Z)) {loc.setYaw(loc.getYaw()+(float)value.getValue());};
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,loc),order.relative);
			}else {
				newO = new ASAnimOrder(new Posture(order.pose.body,order.pose.leftArm,order.pose.rightArm,order.pose.leftLeg,order.pose.rightLeg,order.pose.head,
						order.pose.location.clone().add(axs.contains(Axis.X)?value.getValue():order.pose.location.getX(),axs.contains(Axis.Y)?value.getValue():order.pose.location.getY()
								,axs.contains(Axis.Z)?value.getValue():order.pose.location.getZ())),order.relative);
			}
			break;
		default:
			newO=order;
			break;
		}
		return newO;
	}
	
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
		return null;
	}
	
	public static ASAnimOrder deserialize(Map<String, Object> args) {
		return new ASAnimOrder((Posture)args.get("pose"),(boolean)args.get("relative"));
	}
	
}
