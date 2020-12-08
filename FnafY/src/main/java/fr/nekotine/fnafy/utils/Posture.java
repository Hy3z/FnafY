package fr.nekotine.fnafy.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
@SerializableAs("Posture")
public class Posture implements ConfigurationSerializable {
	
	public CustomEulerAngle body;
	public CustomEulerAngle leftArm;
	public CustomEulerAngle rightArm;
	public CustomEulerAngle leftLeg;
	public CustomEulerAngle rightLeg;
	public CustomEulerAngle head;
	public final Location location;
	
	public Posture(CustomEulerAngle body, CustomEulerAngle leftArm, CustomEulerAngle rightArm, CustomEulerAngle leftLeg, CustomEulerAngle rightLeg, CustomEulerAngle head, Location loc) {
		this.body=body;
		this.leftArm=leftArm;
		this.rightArm=rightArm;
		this.leftLeg=leftLeg;
		this.rightLeg=rightLeg;
		this.head=head;
		this.location=loc;
		System.out.println("Posture created-------");
		System.out.println("body="+body);
		System.out.println("leftArm="+leftArm);
		System.out.println("rightArm="+rightArm);
		System.out.println("leftLeg="+leftLeg);
		System.out.println("rightLeg="+rightLeg);
		System.out.println("head="+head);
	}
	
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		serialized.put("location",location);
		serialized.put("body", body);
		serialized.put("leftArm", leftArm);
		serialized.put("rightArm", rightArm);
		serialized.put("leftLeg", leftLeg);
		serialized.put("rightLeg", rightLeg);
		serialized.put("head", head);
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
	
	public static Posture zero(World w) {
		return new Posture(CustomEulerAngle.zero(),CustomEulerAngle.zero(),CustomEulerAngle.zero(),
				CustomEulerAngle.zero(),CustomEulerAngle.zero(),CustomEulerAngle.zero(),new Location(w,0,0,0));
	}
}
