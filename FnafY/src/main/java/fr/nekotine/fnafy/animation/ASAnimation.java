package fr.nekotine.fnafy.animation;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ArmorStand;

public class ASAnimation implements ConfigurationSerializable {
	
	public final HashMap<Integer,ASAnimOrder> orders = new HashMap<Integer,ASAnimOrder>();
	private int animSize=0;
	private String name;
	
	public void setOrder(int index,ASAnimOrder order) {
		orders.put(index,order);
		if (animSize<index) animSize=index;
	}
	
	public void play(int index, ArmorStand as) {
		if (orders.containsKey(index)) {
			orders.get(index).execute(as);
		}
	}

	public int getAnimSize() {
		return animSize;
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		serialized.put("name", name);
		for (Integer i : orders.keySet()) {
			serialized.put(i.toString(), orders.get(i));
		}
		return serialized;
	}
	
	public static ASAnimation deserialize(Map<String, Object> args) {
		ASAnimation anim = new ASAnimation();
		anim.setName((String) args.get("name"));
		for (String s : args.keySet()) {
			if ((!s.contains("==")) && (!s.contains("name"))) {
				anim.setOrder(Integer.parseInt(s), (ASAnimOrder) args.get(s));
			}
		}
		return anim;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name=n;
	}
}
