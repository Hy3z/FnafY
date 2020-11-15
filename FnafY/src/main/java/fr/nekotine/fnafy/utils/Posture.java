package fr.nekotine.fnafy.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import fr.nekotine.fnafy.animation.ASAnimOrder;

public class Posture {
	
	private ASAnimOrder pose;
	private Location loc;
	
	public Posture (ASAnimOrder pose, Location location) {
		this.pose = pose;
		loc = location;
	}
	
	public void apply(ArmorStand armorstand) {
		armorstand.teleport(loc);
		pose.execute(armorstand);
	}
	
}
