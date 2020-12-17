package fr.nekotine.fnafy.room;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.events.PlayerMoveHeadListener;

public class RoomManager extends PlayerMoveHeadListener implements Listener{
	private final List<Room> roomList = new ArrayList<>();
	public RoomManager(FnafYMain main, List<Room> roomList) {
		super(main);
		this.roomList.addAll(roomList);
	}
	@Override
	public void playerMoveHeadEvent(Player p) {
		for(Room r : roomList) {
			r.playerMoveHeadEvent(p);
		}
	}
	public boolean trackPlayer(Player p) {
		return super.trackPlayer(p);
	}
	public boolean untrackPlayer(Player p) {
		return super.untrackPlayer(p);
	}
}