package fr.nekotine.fnafy.animation;

import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class ASOrderPose implements ASAnimOrder {
	
	private final EulerAngle body;
	private final EulerAngle leftArm;
	private final EulerAngle rightArm;
	private final EulerAngle leftLeg;
	private final EulerAngle rightLeg;
	private final EulerAngle head;

	public ASOrderPose(EulerAngle body, EulerAngle leftArm, EulerAngle rightArm, EulerAngle leftLeg, EulerAngle rightLeg, EulerAngle head) {
		this.body=body;
		this.leftArm=leftArm;
		this.rightArm=rightArm;
		this.leftLeg=leftLeg;
		this.rightLeg=rightLeg;
		this.head=head;
	}
	
	@Override
	public void execute(ArmorStand as) {
		as.setBodyPose(body);
		as.setLeftArmPose(leftArm);
		as.setRightArmPose(rightArm);
		as.setLeftLegPose(leftLeg);
		as.setRightLegPose(rightLeg);
		as.setHeadPose(head);
	}

}
