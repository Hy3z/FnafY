package fr.nekotine.fnafy.animation;

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
	
}
