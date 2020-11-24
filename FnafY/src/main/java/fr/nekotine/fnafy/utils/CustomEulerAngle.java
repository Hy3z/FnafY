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
		// TODO Auto-generated constructor stub
	}
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		serialized.put("x", super.getX());
		serialized.put("y", super.getY());
		serialized.put("z", super.getZ());
		return serialized;
	}
	public static EulerAngle deserialize(Map<String, Object> args) {
		 int xx=0;
		 int yy=0;
		 int zz=0;
		 if (args.containsKey("x")){
			 xx=(int)args.get("x");
		 }
		 if (args.containsKey("y")){
			 yy=(int)args.get("y");
		 }
		 if (args.containsKey("z")){
			 zz=(int)args.get("z");
		 }
		 return new EulerAngle(xx, yy, zz);
	 }
}
