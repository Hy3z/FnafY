package fr.nekotine.fnafy.team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.nekotine.fnafy.animatronic.Animatronic;
import fr.nekotine.fnafy.animatronic.InGameAnimatronic;

public class TeamAfton {

	public static final ArrayList<AftonWrapper> playerList = new ArrayList<AftonWrapper>();
	private List<Integer> unlockedPackages = new ArrayList<>();
	private InGameAnimatronic bonnie;
	private InGameAnimatronic freddy;
	private InGameAnimatronic chica;
	private InGameAnimatronic foxy;
	private InGameAnimatronic mangle;
	private InGameAnimatronic springtrap;
	public boolean unlockPackage(int p) {
		if(unlockedPackages.contains(p)) return false;
		unlockedPackages.add(p);
		return true;
	}
	
	public boolean isPlayerInTeam(UUID id) {
		for (AftonWrapper w : playerList) {
			if (w.playerid.equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public AftonWrapper getWrapper(UUID id) {
		for (AftonWrapper w : playerList) {
			if (w.playerid.equals(id)){
				return w;
			}
		}
		return null;
	}
	
	public List<Integer> getUnlockedPackages(){
		return unlockedPackages;
	}
	public InGameAnimatronic getAnimatronic(Animatronic anim) {
		switch(anim) {
		case BONNIE: return bonnie;
		case FREDDY:return freddy;
		case CHICA: return chica;
		case FOXY: return foxy;
		case MANGLE: return mangle;
		case SPRINGTRAP: return springtrap;
		default: return null;
		}
	}
}
