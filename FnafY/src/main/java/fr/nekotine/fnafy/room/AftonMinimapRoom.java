package fr.nekotine.fnafy.room;

import java.util.HashMap;

import fr.nekotine.fnafy.animation.ASOrderMove;
import fr.nekotine.fnafy.enums.Animatronic;

public class AftonMinimapRoom extends MinimapRoom {
	
	public AftonMinimapRoom(String name, RoomOutline outl, RoomOutline selSur) {
		super(name, outl, selSur);
	}

	private final HashMap<Animatronic,ASOrderMove> animPoses = new HashMap<Animatronic,ASOrderMove>();

	public HashMap<Animatronic,ASOrderMove> getAnimPoses() {
		return animPoses;
	}
}