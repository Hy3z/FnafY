package fr.nekotine.fnafy.animation;

import java.util.HashMap;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;

public class ASAnimation {
	
	private final HashMap<Integer,ArrayList<ASAnimOrder>> orders = new HashMap<Integer,ArrayList<ASAnimOrder>>();
	private int animSize=0;
	
	public void addOrder(int index,ASAnimOrder order) {
		if (!orders.containsKey(index)) orders.put(index,new ArrayList<ASAnimOrder>());
		orders.get(index).add(order);
		if (animSize<index) animSize=index;
	}
	
	public void play(int index, ArmorStand as) {
		for (ASAnimOrder order : orders.get(index)) {
			order.execute(as);
		}
	}

	public int getAnimSize() {
		return animSize;
	}
}
