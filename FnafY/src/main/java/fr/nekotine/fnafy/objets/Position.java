package fr.nekotine.fnafy.objets;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.EulerAngle;

public class Position {
	private Location posLoc;
	private List<Float> posRot;
	private List<EulerAngle> posAngle;
	public Position(Location _posLoc, List<Float> _posRot, List<EulerAngle> _posAngle) {
		posLoc=_posLoc;
		posRot=_posRot;
		posAngle=_posAngle;
	}
	public Location getLocation() {
		return posLoc;
	}
	public List<Float> getRotation(){
		return posRot;
	}
	public List<EulerAngle> getAngles(){
		return posAngle;
	}
}
