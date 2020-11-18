package fr.nekotine.fnafy.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.util.EulerAngle;

public class Posture {
	
	public final EulerAngle body;
	public final EulerAngle leftArm;
	public final EulerAngle rightArm;
	public final EulerAngle leftLeg;
	public final EulerAngle rightLeg;
	public final EulerAngle head;
	public final Location location;
	
	public Posture(EulerAngle body, EulerAngle leftArm, EulerAngle rightArm, EulerAngle leftLeg, EulerAngle rightLeg, EulerAngle head, Location loc) {
		this.body=body;
		this.leftArm=leftArm;
		this.rightArm=rightArm;
		this.leftLeg=leftLeg;
		this.rightLeg=rightLeg;
		this.head=head;
		this.location=loc;
	}
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		//TODO
		serialized.put("body", body);
		serialized.put("leftArm", leftArm);
		serialized.put("rightArm", rightArm);
		serialized.put("leftLeg", leftLeg);
		serialized.put("rightLeg", rightLeg);
		serialized.put("head", head);
		return serialized;
	}
	public static Posture deserialize(Map<String, Object> args) {
		EulerAngle eBody = (EulerAngle)args.get("body");
		EulerAngle eLeftArm = (EulerAngle)args.get("leftArm");
		EulerAngle eRightArm = (EulerAngle)args.get("rightArm");
		EulerAngle eLeftLeg = (EulerAngle)args.get("leftLeg");
		EulerAngle eRightLeg = (EulerAngle)args.get("rightLeg");
		EulerAngle eHead = (EulerAngle)args.get("head");
		Location loc = null;//TODO
		return new Posture(eBody, eLeftArm, eRightArm, eLeftLeg, eRightLeg, eHead, loc);
	 }
}
