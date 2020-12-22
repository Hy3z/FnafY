package fr.nekotine.fnafy;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class TeamGuard {

	public static final ArrayList<GuardWrapper> playerList = new ArrayList<GuardWrapper>();
	public static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	
}
