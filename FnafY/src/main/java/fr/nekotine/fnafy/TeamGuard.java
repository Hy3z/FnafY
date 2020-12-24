package fr.nekotine.fnafy;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class TeamGuard {

	public static final ArrayList<GuardWrapper> playerList = new ArrayList<GuardWrapper>();
	public final Scoreboard scoreboard;
	public final Objective taskobjective;
	
	public TeamGuard() {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		taskobjective = scoreboard.registerNewObjective("tasks", "dummy", "Tasks");
	}
	
	public void resetScoreboardEntries() {
		for (String entry : scoreboard.getEntries()) {
			scoreboard.resetScores(entry);
		}
	}
	
}
