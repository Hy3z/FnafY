package fr.nekotine.fnafy.animation;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class ASOrderMove implements ASAnimOrder {

	private final double x,y,z;
	
	public ASOrderMove(double x, double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	@Override
	public void execute(ArmorStand as) {
		double angle = Math.toRadians(as.getLocation().getYaw());
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		Location newLoc = as.getLocation().clone().add(x*cos+z*sin,y,x*sin+z*cos);
		as.teleport(newLoc,TeleportCause.PLUGIN);
	}

}