package fr.nekotine.fnafy.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.EulerAngle;
@SerializableAs("CustomEulerAngle")
public class CustomEulerAngle extends EulerAngle implements ConfigurationSerializable{

	public CustomEulerAngle(double x, double y, double z) {
		super(x, y, z);
	}
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		serialized.put("x", super.getX());
		serialized.put("y", super.getY());
		serialized.put("z", super.getZ());
		return serialized;
	}
	public static CustomEulerAngle deserialize(Map<String, Object> args) {
		double xx=0;
		double yy=0;
		double zz=0;
		 if (args.containsKey("x")){
			 xx=(double)args.get("x");
		 }
		 if (args.containsKey("y")){
			 yy=(double)args.get("y");
		 }
		 if (args.containsKey("z")){
			 zz=(double)args.get("z");
		 }
		 return new CustomEulerAngle(xx, yy, zz);
	 }
	
	public static final CustomEulerAngle zero() {
		return new CustomEulerAngle(0,0,0);
	}
}
