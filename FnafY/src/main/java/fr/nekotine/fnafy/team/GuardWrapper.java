package fr.nekotine.fnafy.team;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;

import fr.nekotine.fnafy.doorRoom.Room;
import fr.nekotine.fnafy.events.AnimatronicEnterRoomEvent;

public class GuardWrapper implements Listener{
	//register event on game start
	public final UUID playerid;
	public Room currentRoom;
	public GuardWrapper(UUID playerid) {
		this.playerid=playerid;
	}
	@EventHandler
	public void animatronicEnterRoom(AnimatronicEnterRoomEvent e) {
		if(e.getRoom().getRoomName()==currentRoom.getRoomName()) {
			//scream
		}
	}
	public void showScoreboard(Scoreboard scbrd) {
		Player p = Bukkit.getPlayer(playerid);
		if (p!=null) {
			p.setScoreboard(scbrd);
		}
	}
	
}
