package fr.nekotine.fnafy.animation;

import org.bukkit.entity.ArmorStand;

public class ASOrderMP implements ASAnimOrder {

	private final ASOrderMove move;
	private final ASOrderPose pose;
	
	public ASOrderMP(ASOrderMove move, ASOrderPose pose) {
		this.move=move;
		this.pose=pose;
	}
	
	@Override
	public void execute(ArmorStand as) {
		move.execute(as);
		pose.execute(as);
	}

}
