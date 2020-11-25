package fr.nekotine.fnafy.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
@SerializableAs("Posture")
public class Posture implements ConfigurationSerializable {
	
	public final CustomEulerAngle body;
	public final CustomEulerAngle leftArm;
	public final CustomEulerAngle rightArm;
	public final CustomEulerAngle leftLeg;
	public final CustomEulerAngle rightLeg;
	public final CustomEulerAngle head;
	public final Location location;
	
	public Posture(CustomEulerAngle body, CustomEulerAngle leftArm, CustomEulerAngle rightArm, CustomEulerAngle leftLeg, CustomEulerAngle rightLeg, CustomEulerAngle head, Location loc) {
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
		CustomEulerAngle eBody = (CustomEulerAngle)args.get("body");
		CustomEulerAngle eLeftArm = (CustomEulerAngle)args.get("leftArm");
		CustomEulerAngle eRightArm = (CustomEulerAngle)args.get("rightArm");
		CustomEulerAngle eLeftLeg = (CustomEulerAngle)args.get("leftLeg");
		CustomEulerAngle eRightLeg = (CustomEulerAngle)args.get("rightLeg");
		CustomEulerAngle eHead = (CustomEulerAngle)args.get("head");
		Location loc = (Location)args.get("location");
		return new Posture(eBody, eLeftArm, eRightArm, eLeftLeg, eRightLeg, eHead, loc);
	 }
}
