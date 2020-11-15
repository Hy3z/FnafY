package fr.nekotine.fnafy.animation;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.EulerAngle;

public class ASAnimOrder {
	
	private final EulerAngle body;
	private final EulerAngle leftArm;
	private final EulerAngle rightArm;
	private final EulerAngle leftLeg;
	private final EulerAngle rightLeg;
	private final EulerAngle head;
	private final double x;
	private final double y;
	private final double z;
	
	public ASAnimOrder(EulerAngle body, EulerAngle leftArm, EulerAngle rightArm, EulerAngle leftLeg, EulerAngle rightLeg, EulerAngle head, double x, double y, double z) {
		this.body=body;
		this.leftArm=leftArm;
		this.rightArm=rightArm;
		this.leftLeg=leftLeg;
		this.rightLeg=rightLeg;
		this.head=head;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
public void execute(ArmorStand as) {
	double angle = Math.toRadians(as.getLocation().getYaw());
	double cos = Math.cos(angle);
	double sin = Math.sin(angle);
	Location newLoc = as.getLocation().clone().add(x*cos+z*sin,y,x*sin+z*cos);
	as.teleport(newLoc,TeleportCause.PLUGIN);
	as.setBodyPose(body);
	as.setLeftArmPose(leftArm);
	as.setRightArmPose(rightArm);
	as.setLeftLegPose(leftLeg);
	as.setRightLegPose(rightLeg);
	as.setHeadPose(head);
	}
public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		serialized.put("x", x);
		serialized.put("y", y);
		serialized.put("z", z);
		serialized.put("body", body);
		serialized.put("leftArm", leftArm);
		serialized.put("rightArm", rightArm);
		serialized.put("leftLeg", leftLeg);
		serialized.put("rightLeg", rightLeg);
		serialized.put("head", head);
		return serialized;
	}
public static ASAnimOrder deserialize(Map<String, Object> args) {
		EulerAngle eBody = (EulerAngle)args.get("body");
		EulerAngle eLeftArm = (EulerAngle)args.get("leftArm");
		EulerAngle eRightArm = (EulerAngle)args.get("rightArm");
		EulerAngle eLeftLeg = (EulerAngle)args.get("leftLeg");
		EulerAngle eRightLeg = (EulerAngle)args.get("rightLeg");
		EulerAngle eHead = (EulerAngle)args.get("head");
		double xx=(double)args.get("x");
		double yy=(double)args.get("x");
		double zz=(double)args.get("x");
		return new ASAnimOrder(eBody, eLeftArm, eRightArm, eLeftLeg, eRightLeg, eHead, xx, yy, zz);
	 }
}
