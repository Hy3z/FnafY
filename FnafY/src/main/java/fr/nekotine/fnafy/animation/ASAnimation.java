package fr.nekotine.fnafy.animation;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ArmorStand;

public class ASAnimation implements ConfigurationSerializable {
	
	private final HashMap<Integer,ASAnimOrder> orders = new HashMap<Integer,ASAnimOrder>();
	private int animSize=0;
	
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
		serialized.put("orders", orders);
		return serialized;
	}
	
	public static ASAnimation deserialize(Map<String, Object> args) {
		ASAnimation anim = new ASAnimation();
		@SuppressWarnings("unchecked")
		HashMap<Integer,ASAnimOrder> ord = (HashMap<Integer, ASAnimOrder>) args.get("orders");
		for (Integer i : ord.keySet()) {
			anim.setOrder(i, ord.get(i));
		}
		return anim;
	}
}
