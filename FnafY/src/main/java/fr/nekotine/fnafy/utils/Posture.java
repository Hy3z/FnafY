package fr.nekotine.fnafy.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
@SerializableAs("Posture")
public class Posture implements ConfigurationSerializable {
	
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
	
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		System.out.println("Serializing Posture-----------");
		serialized.put("location",location);
		serialized.put("body", body);
		serialized.put("leftArm", leftArm);
		serialized.put("rightArm", rightArm);
		serialized.put("leftLeg", leftLeg);
		serialized.put("rightLeg", rightLeg);
		serialized.put("head", head);
		System.out.println("Posture serialized.");
		return serialized;
	}
	public static Posture deserialize(Map<String, Object> args) {
		EulerAngle eBody = (EulerAngle)args.get("body");
		EulerAngle eLeftArm = (EulerAngle)args.get("leftArm");
		EulerAngle eRightArm = (EulerAngle)args.get("rightArm");
		EulerAngle eLeftLeg = (EulerAngle)args.get("leftLeg");
		EulerAngle eRightLeg = (EulerAngle)args.get("rightLeg");
		EulerAngle eHead = (EulerAngle)args.get("head");
		Location loc = (Location)args.get("location");
		return new Posture(eBody, eLeftArm, eRightArm, eLeftLeg, eRightLeg, eHead, loc);
	 }
}
