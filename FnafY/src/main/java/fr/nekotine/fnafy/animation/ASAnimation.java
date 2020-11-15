package fr.nekotine.fnafy.animation;

import java.util.HashMap;

import org.bukkit.entity.ArmorStand;

public class ASAnimation {
	
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
}
