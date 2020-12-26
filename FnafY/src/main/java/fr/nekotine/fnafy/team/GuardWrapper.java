package fr.nekotine.fnafy.team;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class GuardWrapper {

	public final UUID playerid;
	
	public GuardWrapper(UUID playerid) {
		this.playerid=playerid;
	}
	
	public void showScoreboard(Scoreboard scbrd) {
		Player p = Bukkit.getPlayer(playerid);
		if (p!=null) {
			p.setScoreboard(scbrd);
		}
	}
	
}
