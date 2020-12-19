package fr.nekotine.fnafy.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;
@SerializableAs("BlockSelectionPart")
public class BlockSelectionPart implements ConfigurationSerializable{
	
	private final int x;
	private final int y;
	private final int z;
	
	public BlockSelectionPart(int x,int y,int z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	public void apply(Location loc) {
		loc.add(x, y, z);
	}

	public Vector toVector() {
		return new Vector(x,y,z);
	}
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		serialized.put("x", this.x);
		serialized.put("y", this.y);
		serialized.put("z", this.z);
		return serialized;
	}
	 public static BlockSelectionPart deserialize(Map<String, Object> args) {
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
		 return new BlockSelectionPart(xx,yy,zz);
	 }
}
