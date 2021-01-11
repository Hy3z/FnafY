package fr.nekotine.fnafy.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SerializedBlock implements ConfigurationSerializable {

	private final Location loc;
	private final Material mat;
	
	public SerializedBlock(Location l,Material m) {
		loc=l;
		mat=m;
	}
	
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("location", loc);
		map.put("material", mat.toString());
		return map;
	}
	
	public static SerializedBlock deserialize(Map<String, Object> args) {
		return new SerializedBlock((Location)args.get("location"),Material.getMaterial((String)args.get("material")));
	}

	public Location getLoc() {
		return loc;
	}

	public Material getMat() {
		return mat;
	}
	
	public boolean place() {
		if (mat!=null && loc!=null) {
			loc.getWorld().getBlockAt(loc).setType(mat);
			return true;
		}
		return false;
	}

}
