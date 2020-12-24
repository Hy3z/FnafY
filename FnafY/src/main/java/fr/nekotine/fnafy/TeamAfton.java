package fr.nekotine.fnafy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.Room;

public class TeamAfton {

	public static final ArrayList<GuardWrapper> playerList = new ArrayList<GuardWrapper>();
	private List<Integer> unlockedPackages = new ArrayList<>();
	private HashMap<Animatronic, String> animatronicLocation = new HashMap<>();
	public boolean unlockPackage(int p) {
		if(unlockedPackages.contains(p)) return false;
		unlockedPackages.add(p);
		return true;
	}
	
	public boolean isPlayerInGame(UUID id) {
		for (GuardWrapper w : playerList) {
			if (w.playerid.equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Integer> getUnlockedPackages(){
		return unlockedPackages;
	}
	public String getAnimatronicRoomLocationName(Animatronic anim) {
		return animatronicLocation.get(anim);
	}
	public void setAnimatronicRoomLocation(Animatronic anim, Room r) {
		animatronicLocation.put(anim, r.getRoomName());
	}
}
